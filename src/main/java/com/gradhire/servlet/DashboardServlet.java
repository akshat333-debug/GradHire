package com.gradhire.servlet;

import com.gradhire.dao.ApplicationDao;
import com.gradhire.dao.JobDao;
import com.gradhire.model.Application;
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
import java.util.Collections;
import java.util.List;

public class DashboardServlet extends HttpServlet {
    private final JobDao jobDao = new JobDao();
    private final ApplicationDao applicationDao = new ApplicationDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(req)) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        HttpSession session = req.getSession(false);
        String userType = (String) session.getAttribute(SessionUtil.USER_TYPE);
        Integer userId = (Integer) session.getAttribute(SessionUtil.USER_ID);
        Object applyError = session.getAttribute("applicationError");
        if (applyError != null) {
            req.setAttribute("applicationError", applyError);
            session.removeAttribute("applicationError");
        }

        req.setAttribute("jobs", loadJobs(req));
        req.setAttribute("applications", loadApplications(req, userType, userId));

        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/dashboard.jsp");
        dispatcher.forward(req, resp);
    }

    private List<Job> loadJobs(HttpServletRequest req) {
        try {
            return jobDao.findActiveJobs(10);
        } catch (SQLException exception) {
            req.setAttribute("dashboardError", "Unable to load jobs.");
            return Collections.emptyList();
        }
    }

    private List<Application> loadApplications(HttpServletRequest req, String userType, Integer userId) {
        if (!"student".equalsIgnoreCase(userType) || userId == null) {
            return Collections.emptyList();
        }

        try {
            return applicationDao.findByStudentId(userId);
        } catch (SQLException exception) {
            req.setAttribute("dashboardError", "Unable to load applications.");
            return Collections.emptyList();
        }
    }
}
