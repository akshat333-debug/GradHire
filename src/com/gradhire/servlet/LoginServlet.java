package com.gradhire.servlet;

import com.gradhire.dao.AdminDAO;
import com.gradhire.dao.StudentDAO;
import com.gradhire.model.Admin;
import com.gradhire.model.Student;
import com.gradhire.util.PasswordHasher;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * LoginServlet handles authentication for both students and admins.
 * 
 * URL Pattern: /login
 * Methods: GET, POST
 */
public class LoginServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());
    private StudentDAO studentDAO;
    private AdminDAO adminDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        studentDAO = new StudentDAO();
        adminDAO = new AdminDAO();
        logger.info("LoginServlet initialized");
    }
    
    /**
     * GET /login - Display login page
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check if user is already logged in
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("user") != null) {
            String userType = (String) session.getAttribute("userType");
            
            // Redirect to appropriate dashboard
            if ("student".equals(userType)) {
                response.sendRedirect(request.getContextPath() + "/student/dashboard");
            } else if ("admin".equals(userType)) {
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            }
            return;
        }
        
        // Forward to login page
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }
    
    /**
     * POST /login - Process login credentials
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get form parameters
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String userType = request.getParameter("userType"); // "student" or "admin"
        String rememberMe = request.getParameter("rememberMe");
        
        // Validate input
        if (email == null || email.trim().isEmpty() || 
            password == null || password.trim().isEmpty() ||
            userType == null || userType.trim().isEmpty()) {
            
            request.setAttribute("error", "Email, password, and user type are required");
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
            return;
        }
        
        try {
            boolean authenticated = false;
            Object user = null;
            
            // Authenticate based on user type
            if ("student".equals(userType)) {
                user = authenticateStudent(email.trim(), password);
                authenticated = (user != null);
            } else if ("admin".equals(userType)) {
                user = authenticateAdmin(email.trim(), password);
                authenticated = (user != null);
            } else {
                request.setAttribute("error", "Invalid user type");
                request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
                return;
            }
            
            if (authenticated) {
                // Create session
                HttpSession session = request.getSession(true);
                session.setAttribute("user", user);
                session.setAttribute("userType", userType);
                
                // Set session timeout (30 minutes)
                session.setMaxInactiveInterval(30 * 60);
                
                // Handle remember me (extend cookie age)
                if ("on".equals(rememberMe)) {
                    session.setMaxInactiveInterval(7 * 24 * 60 * 60); // 7 days
                }
                
                logger.info("User logged in successfully: " + email + " (Type: " + userType + ")");
                
                // Redirect to appropriate dashboard
                if ("student".equals(userType)) {
                    response.sendRedirect(request.getContextPath() + "/student/dashboard");
                } else {
                    response.sendRedirect(request.getContextPath() + "/admin/dashboard");
                }
                
            } else {
                // Authentication failed
                logger.warning("Failed login attempt for: " + email + " (Type: " + userType + ")");
                request.setAttribute("error", "Invalid email or password");
                request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
            }
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error during login", e);
            request.setAttribute("error", "An error occurred during login. Please try again.");
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        }
    }
    
    /**
     * Authenticate a student user
     */
    private Student authenticateStudent(String email, String password) {
        try {
            Student student = studentDAO.findByEmail(email);
            
            if (student != null) {
                // Verify password
                if (PasswordHasher.checkPassword(password, student.getPasswordHash())) {
                    return student;
                }
            }
            
            return null;
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error authenticating student", e);
            return null;
        }
    }
    
    /**
     * Authenticate an admin/recruiter user
     */
    private Admin authenticateAdmin(String email, String password) {
        try {
            Admin admin = adminDAO.findByEmail(email);
            
            if (admin != null) {
                // Verify password
                if (PasswordHasher.checkPassword(password, admin.getPasswordHash())) {
                    return admin;
                }
            }
            
            return null;
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error authenticating admin", e);
            return null;
        }
    }
    
    @Override
    public void destroy() {
        logger.info("LoginServlet destroyed");
        super.destroy();
    }
}
