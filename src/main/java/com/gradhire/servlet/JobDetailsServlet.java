package com.gradhire.servlet;

import com.gradhire.dao.ApplicationDao;
import com.gradhire.dao.JobDao;
import com.gradhire.model.Job;
import com.gradhire.util.SessionUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class JobDetailsServlet extends HttpServlet {
    private final JobDao jobDao = new JobDao();
    private final ApplicationDao applicationDao = new ApplicationDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(req)) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

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

        HttpSession session = req.getSession(false);
        String userType = session != null ? (String) session.getAttribute(SessionUtil.USER_TYPE) : null;
        Integer userId = session != null ? (Integer) session.getAttribute(SessionUtil.USER_ID) : null;

        Object applyError = session.getAttribute("applicationError");
        if (applyError != null) {
            req.setAttribute("applicationError", applyError);
            session.removeAttribute("applicationError");
        }
        Object applySuccess = session.getAttribute("applicationSuccess");
        if (applySuccess != null) {
            req.setAttribute("applicationSuccess", applySuccess);
            session.removeAttribute("applicationSuccess");
        }

        try {
            Optional<Job> job = jobDao.findById(jobId);
            if (job.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Job not found.");
                return;
            }

            req.setAttribute("job", job.get());
            if ("student".equalsIgnoreCase(userType) && userId != null) {
                req.setAttribute("alreadyApplied", applicationDao.hasApplied(jobId, userId));
            } else {
                req.setAttribute("alreadyApplied", false);
            }
        } catch (SQLException exception) {
            req.setAttribute("jobDetailsError", "Unable to load job details due to a database error. Please refresh and try again.");
        }

        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/job-details.jsp");
        dispatcher.forward(req, resp);
    }
}
