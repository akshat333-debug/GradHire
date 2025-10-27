package com.gradhire.servlet;

import com.gradhire.dao.ApplicationDAO;
import com.gradhire.dao.JobDAO;
import com.gradhire.model.Application;
import com.gradhire.model.Job;
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
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ApplyServlet handles job application submissions.
 * 
 * URL Pattern: /apply
 * Methods: POST
 */
@MultipartConfig(
    maxFileSize = 5 * 1024 * 1024,       // 5 MB
    maxRequestSize = 10 * 1024 * 1024    // 10 MB
)
public class ApplyServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(ApplyServlet.class.getName());
    private static final String RESUME_UPLOAD_DIR = "uploads/resumes";
    
    private ApplicationDAO applicationDAO;
    private JobDAO jobDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        applicationDAO = new ApplicationDAO();
        jobDAO = new JobDAO();
        logger.info("ApplyServlet initialized");
    }
    
    /**
     * POST /apply - Submit job application
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
        
        Student student = (Student) session.getAttribute("user");
        
        try {
            // Get form parameters
            String jobIdStr = request.getParameter("jobId");
            String coverLetter = request.getParameter("coverLetter");
            String resumeOption = request.getParameter("resumeOption"); // "existing" or "new"
            
            // Validate parameters
            if (jobIdStr == null || jobIdStr.isEmpty()) {
                session.setAttribute("error", "Job ID is required");
                response.sendRedirect(request.getContextPath() + "/jobs");
                return;
            }
            
            Integer jobId = null;
            try {
                jobId = Integer.parseInt(jobIdStr);
            } catch (NumberFormatException e) {
                session.setAttribute("error", "Invalid job ID");
                response.sendRedirect(request.getContextPath() + "/jobs");
                return;
            }
            
            // Check if job exists
            Job job = jobDAO.findById(jobId);
            if (job == null) {
                session.setAttribute("error", "Job not found");
                response.sendRedirect(request.getContextPath() + "/jobs");
                return;
            }
            
            // Check if student has already applied
            if (applicationDAO.hasApplied(student.getStudentId(), jobId)) {
                session.setAttribute("error", "You have already applied to this job");
                response.sendRedirect(request.getContextPath() + "/job/" + jobId);
                return;
            }
            
            // Handle resume upload if needed
            String resumePath = student.getResume();
            
            if ("new".equals(resumeOption)) {
                Part resumePart = request.getPart("resume");
                if (resumePart != null && resumePart.getSize() > 0) {
                    resumePath = uploadResume(resumePart, student.getStudentId());
                    
                    if (resumePath == null) {
                        session.setAttribute("error", "Failed to upload resume");
                        response.sendRedirect(request.getContextPath() + "/job/" + jobId);
                        return;
                    }
                }
            }
            
            // Create application
            Application application = new Application();
            application.setJobId(jobId);
            application.setStudentId(student.getStudentId());
            application.setCoverLetter(coverLetter);
            
            // Save application
            Integer applicationId = applicationDAO.createApplication(application);
            
            if (applicationId != null) {
                logger.info("Application submitted: Student=" + student.getStudentId() + 
                           ", Job=" + jobId + ", Application=" + applicationId);
                
                session.setAttribute("success", "Application submitted successfully!");
                response.sendRedirect(request.getContextPath() + "/student/dashboard");
            } else {
                session.setAttribute("error", "Failed to submit application. Please try again.");
                response.sendRedirect(request.getContextPath() + "/job/" + jobId);
            }
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing application", e);
            session.setAttribute("error", "An error occurred while submitting your application");
            response.sendRedirect(request.getContextPath() + "/jobs");
        }
    }
    
    /**
     * Upload resume file and return the file path
     */
    private String uploadResume(Part filePart, Integer studentId) {
        try {
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            
            // Get upload directory path
            String uploadPath = getServletContext().getRealPath("") + File.separator + RESUME_UPLOAD_DIR;
            
            // Create directory if not exists
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // Generate unique filename
            String fileExtension = "";
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex > 0) {
                fileExtension = fileName.substring(dotIndex);
            }
            
            String uniqueFileName = "resume_" + studentId + "_" + System.currentTimeMillis() + fileExtension;
            String filePath = uploadPath + File.separator + uniqueFileName;
            
            // Save file
            filePart.write(filePath);
            
            logger.info("Resume uploaded: " + uniqueFileName);
            
            return uniqueFileName;
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error uploading resume", e);
            return null;
        }
    }
}
