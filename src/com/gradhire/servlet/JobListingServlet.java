package com.gradhire.servlet;

import com.gradhire.dao.ApplicationDAO;
import com.gradhire.dao.JobDAO;
import com.gradhire.model.Job;
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
 * JobListingServlet handles job browsing and filtering.
 * 
 * URL Pattern: /jobs
 * Methods: GET
 */
public class JobListingServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(JobListingServlet.class.getName());
    private static final int JOBS_PER_PAGE = 10;
    
    private JobDAO jobDAO;
    private ApplicationDAO applicationDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        jobDAO = new JobDAO();
        applicationDAO = new ApplicationDAO();
        logger.info("JobListingServlet initialized");
    }
    
    /**
     * GET /jobs - Display job listings with optional filters
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            // Get filter parameters
            String keyword = request.getParameter("keyword");
            String location = request.getParameter("location");
            String jobType = request.getParameter("jobType");
            String experienceLevel = request.getParameter("experienceLevel");
            String minSalaryStr = request.getParameter("minSalary");
            String sort = request.getParameter("sort");
            String pageStr = request.getParameter("page");
            
            // Parse pagination
            int currentPage = 1;
            if (pageStr != null && !pageStr.isEmpty()) {
                try {
                    currentPage = Integer.parseInt(pageStr);
                    if (currentPage < 1) currentPage = 1;
                } catch (NumberFormatException e) {
                    currentPage = 1;
                }
            }
            
            // Parse min salary
            Integer minSalary = null;
            if (minSalaryStr != null && !minSalaryStr.isEmpty()) {
                try {
                    minSalary = Integer.parseInt(minSalaryStr);
                } catch (NumberFormatException e) {
                    logger.warning("Invalid minSalary value: " + minSalaryStr);
                }
            }
            
            // Calculate offset for pagination
            int offset = (currentPage - 1) * JOBS_PER_PAGE;
            
            // Get jobs from database with filters
            List<Job> jobs = jobDAO.findAllActive(JOBS_PER_PAGE, offset);
            logger.info(String.format("/jobs params -> page=%d offset=%d; jobs.size=%d", currentPage, offset, (jobs != null ? jobs.size() : -1)));
            
            // TODO: Implement filtering in JobDAO
            // For now, we get all active jobs
            // Future: jobDAO.search(keyword, location, jobType, experienceLevel, minSalary, sort, JOBS_PER_PAGE, offset);
            
            // Get total job count for pagination
            int totalJobs = jobDAO.countActive();
            logger.info(String.format("/jobs totals -> totalJobs=%d totalPages=%d", totalJobs, (int) Math.ceil((double) totalJobs / JOBS_PER_PAGE)));
            int totalPages = (int) Math.ceil((double) totalJobs / JOBS_PER_PAGE);
            
            // Check if logged in as student
            HttpSession session = request.getSession(false);
            if (session != null && "student".equals(session.getAttribute("userType"))) {
                Student student = (Student) session.getAttribute("user");
                Integer sid = (student != null ? student.getStudentId() : null);
                logger.info("/jobs student sid=" + sid);
                if (sid != null) {
                    // Check which jobs the student has applied to; do not fail the page if one check errors
                    for (Job job : jobs) {
                        try {
                            boolean hasApplied = applicationDAO.hasApplied(sid, job.getJobId());
                            job.setHasApplied(hasApplied);
                        } catch (Exception ex) {
                            logger.log(Level.WARNING, "hasApplied check failed for student=" + sid + ", job=" + job.getJobId(), ex);
                            job.setHasApplied(false);
                        }
                    }
                }
            }
            
            // Set attributes for JSP
            request.setAttribute("jobs", jobs);
            request.setAttribute("totalJobs", totalJobs);
            request.setAttribute("currentPage", currentPage);
            request.setAttribute("totalPages", totalPages);
            
            // Build query string for pagination links (excluding page parameter)
            StringBuilder queryString = new StringBuilder();
            if (keyword != null && !keyword.isEmpty()) {
                queryString.append("keyword=").append(keyword).append("&");
            }
            if (location != null && !location.isEmpty()) {
                queryString.append("location=").append(location).append("&");
            }
            if (jobType != null && !jobType.isEmpty()) {
                queryString.append("jobType=").append(jobType).append("&");
            }
            if (experienceLevel != null && !experienceLevel.isEmpty()) {
                queryString.append("experienceLevel=").append(experienceLevel).append("&");
            }
            if (minSalaryStr != null && !minSalaryStr.isEmpty()) {
                queryString.append("minSalary=").append(minSalaryStr).append("&");
            }
            if (sort != null && !sort.isEmpty()) {
                queryString.append("sort=").append(sort).append("&");
            }
            
            // Remove trailing & if exists
            String queryStr = queryString.toString();
            if (queryStr.endsWith("&")) {
                queryStr = queryStr.substring(0, queryStr.length() - 1);
            }
            
            request.setAttribute("queryString", queryStr);
            
            // Forward to JSP
            request.getRequestDispatcher("/WEB-INF/jsp/jobs/listing.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading job listings", e);
            request.setAttribute("error", "Error loading jobs. Please try again.");
            request.getRequestDispatcher("/WEB-INF/jsp/jobs/listing.jsp").forward(request, response);
        }
    }
}
