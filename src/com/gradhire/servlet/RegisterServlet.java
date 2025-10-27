package com.gradhire.servlet;

import com.gradhire.dao.AdminDAO;
import com.gradhire.dao.StudentDAO;
import com.gradhire.model.Admin;
import com.gradhire.model.Student;
import com.gradhire.util.PasswordHasher;
import com.gradhire.util.Validator;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * RegisterServlet handles user registration for both students and recruiters.
 * 
 * URL Pattern: /register
 * Methods: GET, POST
 */
public class RegisterServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(RegisterServlet.class.getName());
    private StudentDAO studentDAO;
    private AdminDAO adminDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        studentDAO = new StudentDAO();
        adminDAO = new AdminDAO();
        logger.info("RegisterServlet initialized");
    }
    
    /**
     * GET /register - Display registration page
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if user is already logged in
        if (request.getSession(false) != null && 
            request.getSession(false).getAttribute("user") != null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }
        
        // Get type parameter (student or recruiter)
        String type = request.getParameter("type");
        request.setAttribute("type", type);
        
        // Forward to registration page
        request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
    }
    
    /**
     * POST /register - Process registration
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get common parameters
        String userType = request.getParameter("userType"); // "student" or "recruiter"
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // Debug logging
        logger.info("Registration attempt - userType: " + userType + ", firstName: " + firstName + ", lastName: " + lastName);
        
        // Validate common fields
        if (!validateCommonFields(request, firstName, lastName, email, phone, password, confirmPassword)) {
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
            return;
        }
        
        // Check if userType is null or empty
        if (userType == null || userType.trim().isEmpty()) {
            logger.warning("UserType is null or empty - setting default to student");
            userType = "student"; // Default to student
        }
        
        try {
            // Register based on user type
            if ("student".equals(userType)) {
                registerStudent(request, response, firstName, lastName, email, phone, password);
            } else if ("recruiter".equals(userType)) {
                registerRecruiter(request, response, firstName, lastName, email, phone, password);
            } else {
                request.setAttribute("error", "Invalid user type");
                request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error during registration", e);
            request.setAttribute("error", "An error occurred during registration. Please try again.");
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
        }
    }
    
    /**
     * Validate common registration fields
     */
    private boolean validateCommonFields(HttpServletRequest request, String firstName, String lastName,
                                        String email, String phone, String password, String confirmPassword) {
        
        // Check for null or empty fields
        if (firstName == null || firstName.trim().isEmpty() ||
            lastName == null || lastName.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            phone == null || phone.trim().isEmpty() ||
            password == null || password.isEmpty() ||
            confirmPassword == null || confirmPassword.isEmpty()) {
            
            request.setAttribute("error", "All fields are required");
            return false;
        }
        
        // Validate email format
        if (!Validator.isValidEmail(email)) {
            request.setAttribute("error", "Invalid email format");
            return false;
        }
        
        // Validate password length
        if (password.length() < 8) {
            request.setAttribute("error", "Password must be at least 8 characters long");
            return false;
        }
        
        // Check password match
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match");
            return false;
        }
        
        return true;
    }
    
    /**
     * Register a new student
     */
    private void registerStudent(HttpServletRequest request, HttpServletResponse response,
                                 String firstName, String lastName, String email, String phone, String password)
            throws ServletException, IOException {
        
        // Get student-specific parameters
        String university = request.getParameter("university");
        String major = request.getParameter("major");
        String graduationYearStr = request.getParameter("graduationYear");
        String gpaStr = request.getParameter("gpa");
        
        // Validate student-specific fields
        if (university == null || university.trim().isEmpty() ||
            major == null || major.trim().isEmpty() ||
            graduationYearStr == null || graduationYearStr.trim().isEmpty()) {
            
            request.setAttribute("error", "University, major, and graduation year are required");
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
            return;
        }
        
        // Check if email already exists
        if (studentDAO.findByEmail(email) != null) {
            request.setAttribute("error", "Email already registered");
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
            return;
        }
        
        // Create student object
        Student student = new Student();
        student.setFirstName(firstName.trim());
        student.setLastName(lastName.trim());
        student.setEmail(email.trim());
        student.setPhone(phone.trim());
        student.setPasswordHash(PasswordHasher.hashPassword(password));
        student.setUniversity(university.trim());
        student.setMajor(major.trim());
        try {
            student.setGraduationYear(Integer.parseInt(graduationYearStr.trim()));
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid graduation year format");
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
            return;
        }
        
        // Set GPA if provided
        if (gpaStr != null && !gpaStr.trim().isEmpty()) {
            try {
                student.setGpa(new BigDecimal(gpaStr));
            } catch (NumberFormatException e) {
                logger.warning("Invalid GPA format: " + gpaStr);
            }
        }
        
        // Save to database
        Integer studentId = studentDAO.create(student);
        
        if (studentId != null) {
            logger.info("Student registered successfully: " + email);
            
            // Set success message in session
            request.getSession().setAttribute("success", "Account created successfully! Please login.");
            
            // Redirect to login page
            response.sendRedirect(request.getContextPath() + "/login");
        } else {
            request.setAttribute("error", "Failed to create account. Please try again.");
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
        }
    }
    
    /**
     * Register a new recruiter/admin
     */
    private void registerRecruiter(HttpServletRequest request, HttpServletResponse response,
                                   String firstName, String lastName, String email, String phone, String password)
            throws ServletException, IOException {
        
        // Get recruiter-specific parameters
        String company = request.getParameter("company");
        String position = request.getParameter("position");
        
        // Validate recruiter-specific fields
        if (company == null || company.trim().isEmpty() ||
            position == null || position.trim().isEmpty()) {
            
            request.setAttribute("error", "Company and position are required");
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
            return;
        }
        
        // Check if email already exists
        if (adminDAO.findByEmail(email) != null) {
            request.setAttribute("error", "Email already registered");
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
            return;
        }
        
        // Create admin object
        Admin admin = new Admin();
        admin.setName(firstName.trim() + " " + lastName.trim());
        admin.setEmail(email.trim());
        admin.setPasswordHash(PasswordHasher.hashPassword(password));
        admin.setCompany(company.trim());
        admin.setRole("Recruiter"); // Default role
        
        // Save to database
        Integer adminId = adminDAO.create(admin);
        
        if (adminId != null) {
            logger.info("Recruiter registered successfully: " + email);
            
            // Set success message in session
            request.getSession().setAttribute("success", "Account created successfully! Please login.");
            
            // Redirect to login page
            response.sendRedirect(request.getContextPath() + "/login?type=admin");
        } else {
            request.setAttribute("error", "Failed to create account. Please try again.");
            request.getRequestDispatcher("/WEB-INF/jsp/register.jsp").forward(request, response);
        }
    }
    
    @Override
    public void destroy() {
        logger.info("RegisterServlet destroyed");
        super.destroy();
    }
}
