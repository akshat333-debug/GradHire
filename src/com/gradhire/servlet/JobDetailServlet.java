package com.gradhire.servlet;

import com.gradhire.dao.ApplicationDAO;
import com.gradhire.dao.JobDAO;
import com.gradhire.dao.SkillDAO;
import com.gradhire.model.Job;
import com.gradhire.model.Skill;
import com.gradhire.model.Student;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JobDetailServlet displays detailed information about a specific job.
 * 
 * URL Pattern: /job/*
 * Methods: GET
 */
public class JobDetailServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(JobDetailServlet.class.getName());
    
    private JobDAO jobDAO;
    private ApplicationDAO applicationDAO;
    private SkillDAO skillDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        jobDAO = new JobDAO();
        applicationDAO = new ApplicationDAO();
        skillDAO = new SkillDAO();
        logger.info("JobDetailServlet initialized");
    }
    
    /**
     * GET /job/{jobId} - Display job details
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // Extract job ID from URL path: /job/123
            String pathInfo = request.getPathInfo();
            
            if (pathInfo == null || pathInfo.length() <= 1) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Job ID is required");
                return;
            }
            
            // Parse job ID
            Integer jobId = null;
            try {
                jobId = Integer.parseInt(pathInfo.substring(1)); // Remove leading "/"
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid job ID");
                return;
            }
            
            // Get job details
            Job job = jobDAO.findById(jobId);
            
            if (job == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Job not found");
                return;
            }
            
            // Get required skills for this job
            List<Skill> requiredSkills = skillDAO.findByJob(jobId);
            job.setRequiredSkills(requiredSkills);
            
            // Get application count
            int applicationCount = applicationDAO.getApplicationCountByJob(jobId);
            job.setApplicationCount(applicationCount);
            
            // Check if student has already applied
            boolean hasApplied = false;
            HttpSession session = request.getSession(false);
            
            if (session != null && "student".equals(session.getAttribute("userType"))) {
                Student student = (Student) session.getAttribute("user");
                hasApplied = applicationDAO.hasApplied(student.getStudentId(), jobId);
                
                // Set student attribute for application form
                request.setAttribute("student", student);
            }
            
            // Get similar jobs (same company or same job type) - limit to 3
            List<Job> similarJobs = jobDAO.findSimilar(job.getCompany(), job.getJobType(), 3, jobId);
            
            // Set attributes for JSP
            request.setAttribute("job", job);
            request.setAttribute("hasApplied", hasApplied);
            request.setAttribute("similarJobs", similarJobs);
            
            // Forward to JSP
            request.getRequestDispatcher("/WEB-INF/jsp/jobs/detail.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading job details", e);
            request.setAttribute("error", "Error loading job details. Please try again.");
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
