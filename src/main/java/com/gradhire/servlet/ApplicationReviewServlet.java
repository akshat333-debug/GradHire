package com.gradhire.servlet;

import com.gradhire.dao.ApplicationDao;
import com.gradhire.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

public class ApplicationReviewServlet extends HttpServlet {
    private static final Set<String> ALLOWED_STATUSES = Set.of("Pending", "Reviewed", "Shortlisted", "Rejected", "Accepted");
    private final ApplicationDao applicationDao = new ApplicationDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(req)) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        HttpSession session = req.getSession(false);
        String userType = session != null ? (String) session.getAttribute(SessionUtil.USER_TYPE) : null;
        Integer userId = session != null ? (Integer) session.getAttribute(SessionUtil.USER_ID) : null;
        if (userType == null || userId == null || !"recruiter".equalsIgnoreCase(userType) && !"admin".equalsIgnoreCase(userType)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Only recruiters/admins can review applications.");
            return;
        }

        int applicationId;
        try {
            applicationId = Integer.parseInt(req.getParameter("applicationId"));
            if (applicationId <= 0) {
                throw new NumberFormatException("applicationId must be positive.");
            }
        } catch (NumberFormatException exception) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid application ID.");
            return;
        }

        String status = normalize(req.getParameter("status"));
        String reviewerNotes = normalize(req.getParameter("reviewerNotes"));
        if (status == null || !ALLOWED_STATUSES.contains(status)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid application status.");
            return;
        }

        try {
            boolean updated;
            if ("admin".equalsIgnoreCase(userType)) {
                updated = applicationDao.updateApplicationStatusForSuperAdmin(applicationId, status, reviewerNotes);
            } else {
                updated = applicationDao.updateApplicationStatusForAdmin(applicationId, status, reviewerNotes, userId);
            }
            if (updated) {
                session.setAttribute("applicationSuccess", "Application status updated successfully.");
            } else {
                session.setAttribute("applicationError", "Application not found or not accessible.");
            }
        } catch (SQLException exception) {
            session.setAttribute("applicationError", "Unable to update application due to a database error. Please try again.");
        }

        resp.sendRedirect(req.getContextPath() + "/dashboard");
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
