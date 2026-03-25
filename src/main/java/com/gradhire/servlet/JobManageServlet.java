package com.gradhire.servlet;

import com.gradhire.dao.JobDao;
import com.gradhire.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Set;

public class JobManageServlet extends HttpServlet {
    private static final Set<String> ALLOWED_JOB_TYPES = Set.of("Internship", "Full-time", "Part-time", "Contract");
    private static final Set<String> ALLOWED_JOB_STATUS = Set.of("Active", "Closed", "Draft");
    private static final int FIELD_MAX_LENGTH = 255;
    private final JobDao jobDao = new JobDao();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(req)) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        HttpSession session = req.getSession(false);
        String userType = (String) session.getAttribute(SessionUtil.USER_TYPE);
        Integer adminId = (Integer) session.getAttribute(SessionUtil.USER_ID);
        if (userType == null || adminId == null || (!"recruiter".equalsIgnoreCase(userType) && !"admin".equalsIgnoreCase(userType))) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Only recruiters/admins can manage jobs.");
            return;
        }

        String action = normalize(req.getParameter("action"));
        if (!"create".equalsIgnoreCase(action) && !"update".equalsIgnoreCase(action)) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
            return;
        }

        String jobTitle = normalize(req.getParameter("jobTitle"));
        String companyName = normalize(req.getParameter("companyName"));
        String jobType = normalize(req.getParameter("jobType"));
        String domain = normalize(req.getParameter("domain"));
        String description = normalize(req.getParameter("description"));
        String location = normalize(req.getParameter("location"));
        String jobStatus = normalize(req.getParameter("jobStatus"));

        if (jobTitle == null || jobStatus == null) {
            session.setAttribute("jobManageError", "Job title and status are required.");
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }
        if (!ALLOWED_JOB_STATUS.contains(jobStatus)) {
            session.setAttribute("jobManageError", "Invalid job type or status.");
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }
        if (isTooLong(jobTitle) || isTooLong(companyName) || isTooLong(jobType) || isTooLong(domain) || isTooLong(location) || isTooLong(jobStatus)) {
            session.setAttribute("jobManageError", "One or more fields exceed the allowed length.");
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }

        LocalDate deadline;
        try {
            deadline = parseDate(req.getParameter("applicationDeadline"));
        } catch (DateTimeParseException dateTimeParseException) {
            session.setAttribute("jobManageError", "Invalid application deadline format.");
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }

        try {
            if ("create".equalsIgnoreCase(action)) {
                if (companyName == null || jobType == null || description == null) {
                    session.setAttribute("jobManageError", "Company, job type, and description are required for creating a job.");
                    resp.sendRedirect(req.getContextPath() + "/dashboard");
                    return;
                }
                if (!ALLOWED_JOB_TYPES.contains(jobType)) {
                    session.setAttribute("jobManageError", "Invalid job type.");
                    resp.sendRedirect(req.getContextPath() + "/dashboard");
                    return;
                }
                int createdJobId = jobDao.createJob(
                        adminId,
                        jobTitle,
                        companyName,
                        jobType,
                        domain,
                        description,
                        location,
                        deadline,
                        jobStatus
                );
                session.setAttribute("jobManageSuccess", "Job created successfully (ID: " + createdJobId + ").");
            } else {
                int jobId;
                try {
                    jobId = Integer.parseInt(req.getParameter("jobId"));
                    if (jobId <= 0) {
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException nfe) {
                    session.setAttribute("jobManageError", "Invalid job ID.");
                    resp.sendRedirect(req.getContextPath() + "/dashboard");
                    return;
                }

                boolean updated;
                if ("admin".equalsIgnoreCase(userType)) {
                    updated = jobDao.updateJobBasic(jobId, jobTitle, domain, location, deadline, jobStatus);
                } else {
                    updated = jobDao.updateJobBasicForAdmin(jobId, jobTitle, domain, location, deadline, jobStatus, adminId);
                }
                if (updated) {
                    session.setAttribute("jobManageSuccess", "Job updated successfully.");
                } else {
                    session.setAttribute("jobManageError", "Job not found or not accessible for update.");
                }
            }
        } catch (SQLException sqlException) {
            session.setAttribute("jobManageError", "Unable to manage job due to a database error. Please try again.");
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

    private LocalDate parseDate(String value) {
        String normalized = normalize(value);
        if (normalized == null) {
            return null;
        }
        return LocalDate.parse(normalized);
    }

    private boolean isTooLong(String value) {
        return value != null && value.length() > FIELD_MAX_LENGTH;
    }
}
