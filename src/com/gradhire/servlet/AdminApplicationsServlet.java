package com.gradhire.servlet;

import com.gradhire.dao.ApplicationDAO;
import com.gradhire.model.Application;
import com.gradhire.model.Admin;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class AdminApplicationsServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(AdminApplicationsServlet.class.getName());

    private ApplicationDAO applicationDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        applicationDAO = new ApplicationDAO();
        logger.info("AdminApplicationsServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Require admin authentication
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("userType"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            Admin admin = (Admin) session.getAttribute("user");
            // Fetch applications for this admin's jobs
            List<Application> applications = applicationDAO.findByAdmin(admin.getAdminId());
            request.setAttribute("applications", applications);

            request.getRequestDispatcher("/WEB-INF/jsp/admin/applications.jsp").forward(request, response);
        } catch (Exception e) {
            logger.severe("Error loading admin applications: " + e.getMessage());
            request.setAttribute("error", "Unable to load applications. Please try again.");
            request.getRequestDispatcher("/WEB-INF/jsp/admin/applications.jsp").forward(request, response);
        }
    }
}
