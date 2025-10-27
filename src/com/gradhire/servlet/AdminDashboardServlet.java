package com.gradhire.servlet;

import com.gradhire.dao.AdminDAO;
import com.gradhire.dao.ApplicationDAO;
import com.gradhire.dao.JobDAO;
import com.gradhire.model.Admin;
import com.gradhire.model.Application;
import com.gradhire.model.Job;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * AdminDashboardServlet handles the admin/recruiter dashboard.
 * 
 * URL Pattern: /admin/dashboard
 * Methods: GET, POST (for AJAX status updates)
 */
public class AdminDashboardServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(AdminDashboardServlet.class.getName());
    
    private AdminDAO adminDAO;
    private JobDAO jobDAO;
    private ApplicationDAO applicationDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        adminDAO = new AdminDAO();
        jobDAO = new JobDAO();
        applicationDAO = new ApplicationDAO();
        logger.info("AdminDashboardServlet initialized");
    }
    
    /**
     * GET /admin/dashboard - Display admin dashboard
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
        
        try {
            Admin admin = (Admin) session.getAttribute("user");
            
            // Refresh admin data from database
            Admin refreshedAdmin = adminDAO.findById(admin.getAdminId());
            if (refreshedAdmin != null) {
                admin = refreshedAdmin;
                session.setAttribute("user", admin);
            }
            
            // Get statistics
            Map<String, Object> stats = new HashMap<>();
            
            // Total active jobs posted by this admin
            int totalJobs = jobDAO.countByAdmin(admin.getAdminId());
            stats.put("totalJobs", totalJobs);
            
            // Total applications for this admin's jobs
            int totalApplications = applicationDAO.getApplicationCountByAdmin(admin.getAdminId());
            stats.put("totalApplications", totalApplications);
            
            // Pending review count
            int pendingReview = applicationDAO.getApplicationCountByAdminAndStatus(
                admin.getAdminId(), "PENDING");
            stats.put("pendingReview", pendingReview);
            
            // Shortlisted count
            int shortlisted = applicationDAO.getApplicationCountByAdminAndStatus(
                admin.getAdminId(), "SHORTLISTED");
            stats.put("shortlisted", shortlisted);
            
            // Accepted count
            int accepted = applicationDAO.getApplicationCountByAdminAndStatus(
                admin.getAdminId(), "ACCEPTED");
            stats.put("accepted", accepted);
            
            // Calculate percentages for progress bars
            if (totalApplications > 0) {
                stats.put("pendingPercentage", (pendingReview * 100) / totalApplications);
                stats.put("shortlistedPercentage", (shortlisted * 100) / totalApplications);
                stats.put("acceptedPercentage", (accepted * 100) / totalApplications);
            } else {
                stats.put("pendingPercentage", 0);
                stats.put("shortlistedPercentage", 0);
                stats.put("acceptedPercentage", 0);
            }
            
            // Get recent applications (limit 10)
            List<Application> recentApplications = applicationDAO.findByAdmin(admin.getAdminId(), 10);
            
            // Get active jobs (limit 5)
            List<Job> activeJobs = jobDAO.findByAdmin(admin.getAdminId(), 5);
            
            // Set attributes for JSP
            request.setAttribute("admin", admin);
            request.setAttribute("stats", stats);
            request.setAttribute("recentApplications", recentApplications);
            request.setAttribute("activeJobs", activeJobs);
            request.setAttribute("notificationCount", 0); // TODO: Implement notifications
            
            // Forward to JSP
            request.getRequestDispatcher("/WEB-INF/jsp/admin/dashboard.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading admin dashboard", e);
            request.setAttribute("error", "Error loading dashboard. Please try again.");
            request.getRequestDispatcher("/WEB-INF/jsp/admin/dashboard.jsp").forward(request, response);
        }
    }
    
    /**
     * POST /admin/dashboard - Handle AJAX requests (status updates, etc.)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("userType"))) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        
        try {
            // This could handle AJAX requests like status updates
            // For now, redirect to GET
            out.print("{\"success\": false, \"message\": \"Use POST to /admin/application/update-status\"}");
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error in admin dashboard POST", e);
            out.print("{\"success\": false, \"message\": \"An error occurred\"}");
        }
    }
}
