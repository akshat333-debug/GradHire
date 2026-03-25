package com.gradhire.servlet;

import com.gradhire.dao.ApplicationDao;
import com.gradhire.util.SessionUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class ApplyServlet extends HttpServlet {
    private final ApplicationDao applicationDao = new ApplicationDao();

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
        String coverLetter = req.getParameter("coverLetter");
        int jobId;
        try {
            jobId = Integer.parseInt(jobIdRaw);
        } catch (NumberFormatException exception) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid job ID.");
            return;
        }

        try {
            applicationDao.applyToJob(jobId, studentId, coverLetter);
            resp.sendRedirect(req.getContextPath() + "/dashboard");
        } catch (SQLException exception) {
            req.getSession().setAttribute("applicationError", "Could not submit application.");
            resp.sendRedirect(req.getContextPath() + "/dashboard");
        }
    }
}
