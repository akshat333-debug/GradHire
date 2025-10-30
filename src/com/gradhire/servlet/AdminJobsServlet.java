package com.gradhire.servlet;

import com.gradhire.dao.JobDAO;
import com.gradhire.model.Admin;
import com.gradhire.model.Job;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Lists all jobs posted by the logged-in recruiter/admin.
 * URL: /admin/jobs
 */
public class AdminJobsServlet extends HttpServlet {

    private static final Logger logger = Logger.getLogger(AdminJobsServlet.class.getName());
    private JobDAO jobDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        jobDAO = new JobDAO();
        logger.info("AdminJobsServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("userType"))) {
            response.sendRedirect(request.getContextPath() + "/login?type=admin");
            return;
        }

        try {
            Admin admin = (Admin) session.getAttribute("user");
            List<Job> jobs = jobDAO.findByAdmin(admin.getAdminId());
            request.setAttribute("jobs", jobs);
            request.getRequestDispatcher("/WEB-INF/jsp/admin/jobs.jsp").forward(request, response);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading admin jobs", e);
            request.setAttribute("error", "Error loading jobs. Please try again.");
            request.getRequestDispatcher("/WEB-INF/jsp/admin/dashboard.jsp").forward(request, response);
        }
    }
}


