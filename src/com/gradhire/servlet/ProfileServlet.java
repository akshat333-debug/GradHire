package com.gradhire.servlet;

import com.gradhire.dao.SkillDAO;
import com.gradhire.dao.StudentDAO;
import com.gradhire.model.Skill;
import com.gradhire.model.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ProfileServlet handles student profile viewing and editing.
 * 
 * URL Pattern: /student/profile
 * Methods: GET, POST
 */
@MultipartConfig(
    maxFileSize = 5 * 1024 * 1024,       // 5 MB
    maxRequestSize = 10 * 1024 * 1024    // 10 MB
)
public class ProfileServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(ProfileServlet.class.getName());
    private static final String PROFILE_UPLOAD_DIR = "uploads/profiles";
    private static final String RESUME_UPLOAD_DIR = "uploads/resumes";
    
    private StudentDAO studentDAO;
    private SkillDAO skillDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        studentDAO = new StudentDAO();
        skillDAO = new SkillDAO();
        logger.info("ProfileServlet initialized");
    }
    
    /**
     * GET /student/profile - Display profile page
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || !"student".equals(session.getAttribute("userType"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        try {
            Student student = (Student) session.getAttribute("user");
            
            // Refresh student data from database
            Student refreshedStudent = studentDAO.findById(student.getStudentId());
            if (refreshedStudent != null) {
                student = refreshedStudent;
                session.setAttribute("user", student);
            }
            
            // Get student skills
            List<Skill> skills = skillDAO.findByStudent(student.getStudentId());
            student.setSkills(skills);
            
            // Calculate profile completion
            int profileCompletion = calculateProfileCompletion(student);
            
            // Set attributes for JSP
            request.setAttribute("student", student);
            request.setAttribute("profileCompletion", profileCompletion);
            
            // Forward to JSP
            request.getRequestDispatcher("/WEB-INF/jsp/student/profile.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading profile", e);
            request.setAttribute("error", "Error loading profile. Please try again.");
            request.getRequestDispatcher("/WEB-INF/jsp/student/profile.jsp").forward(request, response);
        }
    }
    
    /**
     * POST /student/profile - Update profile
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || !"student".equals(session.getAttribute("userType"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        try {
            Student student = (Student) session.getAttribute("user");
            
            // Get form parameters
            String firstName = request.getParameter("firstName");
            String lastName = request.getParameter("lastName");
            String phone = request.getParameter("phone");
            String university = request.getParameter("university");
            String major = request.getParameter("major");
            String minor = request.getParameter("minor");
            String gpaStr = request.getParameter("gpa");
            String graduationYearStr = request.getParameter("graduationYear");
            String degreeLevel = request.getParameter("degreeLevel");
            String bio = request.getParameter("bio");
            String linkedIn = request.getParameter("linkedIn");
            String github = request.getParameter("github");
            String portfolio = request.getParameter("portfolio");
            String skillsStr = request.getParameter("skills");
            
            // Update student object
            student.setFirstName(firstName);
            student.setLastName(lastName);
            student.setPhone(phone);
            student.setUniversity(university);
            student.setMajor(major);
            student.setMinor(minor);
            student.setDegreeLevel(degreeLevel);
            student.setBio(bio);
            student.setLinkedIn(linkedIn);
            student.setGitHub(github);
            student.setPortfolio(portfolio);
            
            // Parse GPA
            if (gpaStr != null && !gpaStr.isEmpty()) {
                try {
                    student.setGpa(new BigDecimal(gpaStr));
                } catch (NumberFormatException e) {
                    logger.warning("Invalid GPA: " + gpaStr);
                }
            }
            
            // Parse graduation year
            if (graduationYearStr != null && !graduationYearStr.isEmpty()) {
                try {
                    student.setGraduationYear(Integer.parseInt(graduationYearStr));
                } catch (NumberFormatException e) {
                    logger.warning("Invalid graduation year: " + graduationYearStr);
                }
            }
            
            // Handle profile picture upload
            Part profilePicturePart = request.getPart("profilePicture");
            if (profilePicturePart != null && profilePicturePart.getSize() > 0) {
                String picturePath = uploadFile(profilePicturePart, PROFILE_UPLOAD_DIR, 
                                               "profile_" + student.getStudentId());
                if (picturePath != null) {
                    student.setProfilePicture(picturePath);
                }
            }
            
            // Handle resume upload
            Part resumePart = request.getPart("resume");
            if (resumePart != null && resumePart.getSize() > 0) {
                String resumePath = uploadFile(resumePart, RESUME_UPLOAD_DIR, 
                                              "resume_" + student.getStudentId());
                if (resumePath != null) {
                    student.setResume(resumePath);
                }
            }
            
            // Update student in database
            boolean updated = studentDAO.update(student);
            
            // Update skills if provided
            if (skillsStr != null && !skillsStr.isEmpty()) {
                String[] skillNames = skillsStr.split(",");
                skillDAO.updateStudentSkills(student.getStudentId(), skillNames);
            }
            
            if (updated) {
                // Refresh student in session
                session.setAttribute("user", student);
                
                logger.info("Profile updated for student: " + student.getStudentId());
                session.setAttribute("success", "Profile updated successfully!");
            } else {
                session.setAttribute("error", "Failed to update profile");
            }
            
            response.sendRedirect(request.getContextPath() + "/student/profile");
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error updating profile", e);
            session.setAttribute("error", "An error occurred while updating your profile");
            response.sendRedirect(request.getContextPath() + "/student/profile");
        }
    }
    
    /**
     * Upload a file and return the filename
     */
    private String uploadFile(Part filePart, String uploadDir, String filePrefix) {
        try {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            
            // Get upload directory path
            String uploadPath = getServletContext().getRealPath("") + File.separator + uploadDir;
            
            // Create directory if not exists
            File uploadDirectory = new File(uploadPath);
            if (!uploadDirectory.exists()) {
                uploadDirectory.mkdirs();
            }
            
            // Get file extension
            String fileExtension = "";
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex > 0) {
                fileExtension = fileName.substring(dotIndex);
            }
            
            // Generate unique filename
            String uniqueFileName = filePrefix + "_" + System.currentTimeMillis() + fileExtension;
            String filePath = uploadPath + File.separator + uniqueFileName;
            
            // Save file
            filePart.write(filePath);
            
            logger.info("File uploaded: " + uniqueFileName);
            
            return uniqueFileName;
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error uploading file", e);
            return null;
        }
    }
    
    /**
     * Calculate profile completion percentage
     */
    private int calculateProfileCompletion(Student student) {
        int total = 13;
        int completed = 0;
        
        if (student.getFirstName() != null && !student.getFirstName().isEmpty()) completed++;
        if (student.getLastName() != null && !student.getLastName().isEmpty()) completed++;
        if (student.getEmail() != null && !student.getEmail().isEmpty()) completed++;
        if (student.getPhone() != null && !student.getPhone().isEmpty()) completed++;
        if (student.getProfilePicture() != null && !student.getProfilePicture().isEmpty()) completed++;
        if (student.getUniversity() != null && !student.getUniversity().isEmpty()) completed++;
        if (student.getMajor() != null && !student.getMajor().isEmpty()) completed++;
        if (student.getGraduationYear() != null) completed++;
        if (student.getGpa() != null) completed++;
        if (student.getResume() != null && !student.getResume().isEmpty()) completed++;
        if (student.getBio() != null && !student.getBio().isEmpty()) completed++;
        if (student.getLinkedIn() != null && !student.getLinkedIn().isEmpty()) completed++;
        if (student.getGitHub() != null && !student.getGitHub().isEmpty()) completed++;
        
        return (int) ((completed / (double) total) * 100);
    }
}
