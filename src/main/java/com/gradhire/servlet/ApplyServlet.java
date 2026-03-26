package com.gradhire.servlet;

import com.gradhire.dao.ApplicationDao;
import com.gradhire.dao.ActivityLogDao;
import com.gradhire.dao.JobDao;
import com.gradhire.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLException;

public class ApplyServlet extends HttpServlet {
    private static final int COVER_LETTER_MAX_LENGTH = 5000;
    private final ApplicationDao applicationDao = new ApplicationDao();
    private final JobDao jobDao = new JobDao();
    private final ActivityLogDao activityLogDao = new ActivityLogDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(req)) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        String userType = (String) req.getSession(false).getAttribute(SessionUtil.USER_TYPE);
        Integer studentId = (Integer) req.getSession(false).getAttribute(SessionUtil.USER_ID);
        if (!"student".equalsIgnoreCase(userType) || studentId == null) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Only students can apply.");
            return;
        }

        String jobIdRaw = req.getParameter("jobId");
        String coverLetter = normalize(req.getParameter("coverLetter"));
        if (coverLetter != null && coverLetter.length() > COVER_LETTER_MAX_LENGTH) {
            req.getSession().setAttribute("applicationError", "Cover letter exceeds the allowed length.");
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }
        int jobId;
        try {
            jobId = Integer.parseInt(jobIdRaw);
            if (jobId <= 0) {
                throw new NumberFormatException("jobId must be positive.");
            }
        } catch (NumberFormatException exception) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid job ID.");
            return;
        }

        try {
            if (!jobDao.isActiveAndOpen(jobId)) {
                req.getSession().setAttribute("applicationError", "This job is not available for applications.");
                resp.sendRedirect(req.getContextPath() + "/dashboard");
                return;
            }

            if (applicationDao.hasApplied(jobId, studentId)) {
                req.getSession().setAttribute("applicationError", "You have already applied for this job.");
                resp.sendRedirect(req.getContextPath() + "/dashboard");
                return;
            }

            applicationDao.applyToJob(jobId, studentId, coverLetter);
            try {
                activityLogDao.logActivity("student", studentId, "application", "Applied to job ID: " + jobId, req.getRemoteAddr(), req.getHeader("User-Agent"));
            } catch (SQLException ignored) {
                // Non-blocking audit log.
            }
            req.getSession().setAttribute("applicationSuccess", "Application submitted successfully.");
            resp.sendRedirect(req.getContextPath() + "/dashboard");
        } catch (SQLIntegrityConstraintViolationException exception) {
            req.getSession().setAttribute("applicationError", "You have already applied for this job.");
            resp.sendRedirect(req.getContextPath() + "/dashboard");
        } catch (SQLException exception) {
            req.getSession().setAttribute("applicationError", "Unable to submit application due to a database error. Please try again.");
            resp.sendRedirect(req.getContextPath() + "/dashboard");
        }
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
