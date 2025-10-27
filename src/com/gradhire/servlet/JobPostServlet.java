package com.gradhire.servlet;

import com.gradhire.dao.JobDAO;
import com.gradhire.dao.SkillDAO;
import com.gradhire.model.Admin;
import com.gradhire.model.Job;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * JobPostServlet handles job posting by recruiters.
 * 
 * URL Pattern: /admin/jobs/post
 * Methods: GET, POST
 */
public class JobPostServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(JobPostServlet.class.getName());
    
    private JobDAO jobDAO;
    private SkillDAO skillDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        jobDAO = new JobDAO();
        skillDAO = new SkillDAO();
        logger.info("JobPostServlet initialized");
    }
    
    /**
     * GET /admin/jobs/post - Display job posting form
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("userType"))) {
            response.sendRedirect(request.getContextPath() + "/login?type=admin");
            return;
        }
        
        Admin admin = (Admin) session.getAttribute("user");
        request.setAttribute("admin", admin);
        
        // Forward to job posting form
        request.getRequestDispatcher("/WEB-INF/jsp/admin/post-job.jsp").forward(request, response);
    }
    
    /**
     * POST /admin/jobs/post - Submit new job posting
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("userType"))) {
            response.sendRedirect(request.getContextPath() + "/login?type=admin");
            return;
        }
        
        try {
            Admin admin = (Admin) session.getAttribute("user");
            
            // Get form parameters
            String title = request.getParameter("title");
            String jobType = request.getParameter("jobType");
            String company = request.getParameter("company");
            String location = request.getParameter("location");
            String description = request.getParameter("description");
            String requirements = request.getParameter("requirements");
            String salaryMinStr = request.getParameter("salaryMin");
            String salaryMaxStr = request.getParameter("salaryMax");
            String benefits = request.getParameter("benefits");
            String skillsStr = request.getParameter("skills");
            String experienceLevel = request.getParameter("experienceLevel");
            String educationLevel = request.getParameter("educationLevel");
            String applicationDeadlineStr = request.getParameter("applicationDeadline");
            String positionsStr = request.getParameter("positions");
            String contactEmail = request.getParameter("contactEmail");
            String contactPhone = request.getParameter("contactPhone");
            String isActiveStr = request.getParameter("isActive");
            
            // Validate required fields
            if (title == null || title.isEmpty() ||
                jobType == null || jobType.isEmpty() ||
                company == null || company.isEmpty() ||
                location == null || location.isEmpty() ||
                description == null || description.isEmpty()) {
                
                session.setAttribute("error", "Title, job type, company, location, and description are required");
                response.sendRedirect(request.getContextPath() + "/admin/jobs/post");
                return;
            }
            
            // Create job object
            Job job = new Job();
            job.setAdminId(admin.getAdminId());
            job.setTitle(title);
            job.setJobType(jobType);
            job.setCompany(company);
            job.setLocation(location);
            job.setDescription(description);
            job.setRequirements(requirements);
            job.setBenefits(benefits);
            job.setExperienceLevel(experienceLevel);
            job.setEducationLevel(educationLevel);
            job.setContactEmail(contactEmail);
            job.setContactPhone(contactPhone);
            
            // Parse salary with validation
            if (salaryMinStr != null && !salaryMinStr.isEmpty()) {
                try {
                    java.math.BigDecimal salaryMin = new java.math.BigDecimal(salaryMinStr);
                    if (salaryMin.compareTo(java.math.BigDecimal.ZERO) < 0) {
                        session.setAttribute("error", "Salary cannot be negative");
                        response.sendRedirect(request.getContextPath() + "/admin/jobs/post");
                        return;
                    }
                    job.setSalaryMin(salaryMin);
                } catch (NumberFormatException e) {
                    logger.warning("Invalid salary min: " + salaryMinStr);
                    session.setAttribute("error", "Invalid minimum salary format");
                    response.sendRedirect(request.getContextPath() + "/admin/jobs/post");
                    return;
                }
            }
            
            if (salaryMaxStr != null && !salaryMaxStr.isEmpty()) {
                try {
                    java.math.BigDecimal salaryMax = new java.math.BigDecimal(salaryMaxStr);
                    if (salaryMax.compareTo(java.math.BigDecimal.ZERO) < 0) {
                        session.setAttribute("error", "Salary cannot be negative");
                        response.sendRedirect(request.getContextPath() + "/admin/jobs/post");
                        return;
                    }
                    job.setSalaryMax(salaryMax);
                } catch (NumberFormatException e) {
                    logger.warning("Invalid salary max: " + salaryMaxStr);
                    session.setAttribute("error", "Invalid maximum salary format");
                    response.sendRedirect(request.getContextPath() + "/admin/jobs/post");
                    return;
                }
            }
            
            // Validate salary range
            if (job.getSalaryMin() != null && job.getSalaryMax() != null &&
                job.getSalaryMin().compareTo(job.getSalaryMax()) > 0) {
                session.setAttribute("error", "Minimum salary cannot be greater than maximum salary");
                response.sendRedirect(request.getContextPath() + "/admin/jobs/post");
                return;
            }
            
            // Parse deadline
            if (applicationDeadlineStr != null && !applicationDeadlineStr.isEmpty()) {
                try {
                    job.setApplicationDeadline(Date.valueOf(applicationDeadlineStr));
                } catch (IllegalArgumentException e) {
                    logger.warning("Invalid deadline: " + applicationDeadlineStr);
                }
            }
            
            // Parse positions
            if (positionsStr != null && !positionsStr.isEmpty()) {
                try {
                    job.setPositionsAvailable(Integer.parseInt(positionsStr));
                } catch (NumberFormatException e) {
                    logger.warning("Invalid positions: " + positionsStr);
                    job.setPositionsAvailable(1); // Default to 1
                }
            } else {
                job.setPositionsAvailable(1); // Default to 1
            }
            
            // Set job status
            boolean isActive = "on".equals(isActiveStr);
            job.setIsActive(isActive);
            
            // Save job to database
            Integer jobId = jobDAO.create(job);
            
            if (jobId != null) {
                // Add skills if provided
                if (skillsStr != null && !skillsStr.isEmpty()) {
                    String[] skillNames = skillsStr.split(",");
                    for (String skillName : skillNames) {
                        skillName = skillName.trim();
                        if (!skillName.isEmpty()) {
                            // Add skill to job
                            skillDAO.addJobSkill(jobId, skillName, true); // Mark as required
                        }
                    }
                }
                
                logger.info("Job posted successfully: ID=" + jobId + ", Admin=" + admin.getAdminId());
                session.setAttribute("success", "Job posted successfully!");
                response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            } else {
                session.setAttribute("error", "Failed to post job. Please try again.");
                response.sendRedirect(request.getContextPath() + "/admin/jobs/post");
            }
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error posting job", e);
            session.setAttribute("error", "An error occurred while posting the job");
            response.sendRedirect(request.getContextPath() + "/admin/jobs/post");
        }
    }
}
