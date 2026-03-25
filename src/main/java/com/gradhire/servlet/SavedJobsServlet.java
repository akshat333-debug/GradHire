package com.gradhire.servlet;

import com.gradhire.dao.ActivityLogDao;
import com.gradhire.dao.SavedJobDao;
import com.gradhire.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

public class SavedJobsServlet extends HttpServlet {
    private final SavedJobDao savedJobDao = new SavedJobDao();
    private final ActivityLogDao activityLogDao = new ActivityLogDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(req)) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        HttpSession session = req.getSession(false);
        String userType = (String) session.getAttribute(SessionUtil.USER_TYPE);
        Integer studentId = (Integer) session.getAttribute(SessionUtil.USER_ID);
        if (!"student".equalsIgnoreCase(userType) || studentId == null) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Only students can manage saved jobs.");
            return;
        }

        String action = normalize(req.getParameter("action"));
        int jobId;
        try {
            jobId = Integer.parseInt(req.getParameter("jobId"));
            if (jobId <= 0) {
                throw new NumberFormatException("jobId must be positive.");
            }
        } catch (NumberFormatException exception) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid job ID.");
            return;
        }

        try {
            if ("save".equalsIgnoreCase(action)) {
                if (!savedJobDao.hasSavedJob(studentId, jobId)) {
                    savedJobDao.saveJob(studentId, jobId);
                    activityLogDao.logActivity("student", studentId, "saved_job", "Saved job ID: " + jobId, req.getRemoteAddr(), req.getHeader("User-Agent"));
                }
                session.setAttribute("savedJobSuccess", "Job saved.");
            } else if ("unsave".equalsIgnoreCase(action)) {
                if (savedJobDao.removeSavedJob(studentId, jobId)) {
                    activityLogDao.logActivity("student", studentId, "unsaved_job", "Removed saved job ID: " + jobId, req.getRemoteAddr(), req.getHeader("User-Agent"));
                }
                session.setAttribute("savedJobSuccess", "Saved job removed.");
            } else {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
                return;
            }
        } catch (SQLException exception) {
            session.setAttribute("savedJobError", "Unable to update saved jobs due to a database error. Please try again.");
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
