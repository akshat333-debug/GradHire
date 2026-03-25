package com.gradhire.servlet;

import com.gradhire.dao.ApplicationDao;
import com.gradhire.dao.JobDao;
import com.gradhire.model.Application;
import com.gradhire.model.ApplicationReviewItem;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        Object applySuccess = session.getAttribute("applicationSuccess");
        if (applySuccess != null) {
            req.setAttribute("applicationSuccess", applySuccess);
            session.removeAttribute("applicationSuccess");
        }
        Object jobManageError = session.getAttribute("jobManageError");
        if (jobManageError != null) {
            req.setAttribute("jobManageError", jobManageError);
            session.removeAttribute("jobManageError");
        }
        Object jobManageSuccess = session.getAttribute("jobManageSuccess");
        if (jobManageSuccess != null) {
            req.setAttribute("jobManageSuccess", jobManageSuccess);
            session.removeAttribute("jobManageSuccess");
        }

        List<Job> jobs = loadJobs(req);
        List<Application> applications = loadApplications(req, userType, userId);
        req.setAttribute("jobs", jobs);
        req.setAttribute("applications", applications);
        req.setAttribute("appliedJobIds", buildAppliedJobIds(applications));
        req.setAttribute("reviewApplications", loadReviewApplications(req, userType, userId));
        req.setAttribute("managedJobs", loadManagedJobs(req, userType, userId));

        RequestDispatcher dispatcher = req.getRequestDispatcher(resolveDashboardView(userType));
        dispatcher.forward(req, resp);
    }

    private String resolveDashboardView(String userType) {
        if ("student".equalsIgnoreCase(userType)) {
            return "/WEB-INF/views/dashboard-student.jsp";
        }
        if ("recruiter".equalsIgnoreCase(userType)) {
            return "/WEB-INF/views/dashboard-recruiter.jsp";
        }
        if ("admin".equalsIgnoreCase(userType)) {
            return "/WEB-INF/views/dashboard-admin.jsp";
        }
        return "/WEB-INF/views/dashboard.jsp";
    }

    private List<Job> loadJobs(HttpServletRequest req) {
        try {
            return jobDao.findActiveJobs(10);
        } catch (SQLException exception) {
            req.setAttribute("dashboardError", "Unable to load jobs due to a database error. Please refresh and try again.");
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
            req.setAttribute("dashboardError", "Unable to load your applications due to a database error. Please refresh and try again.");
            return Collections.emptyList();
        }
    }

    private Set<Integer> buildAppliedJobIds(List<Application> applications) {
        if (applications == null || applications.isEmpty()) {
            return Collections.emptySet();
        }
        Set<Integer> appliedJobIds = new HashSet<>();
        for (Application application : applications) {
            appliedJobIds.add(application.getJobId());
        }
        return appliedJobIds;
    }

    private List<ApplicationReviewItem> loadReviewApplications(HttpServletRequest req, String userType, Integer userId) {
        if (userType == null || userId == null) {
            return Collections.emptyList();
        }

        try {
            if ("admin".equalsIgnoreCase(userType)) {
                return applicationDao.findReviewItemsForSuperAdmin();
            }
            if ("recruiter".equalsIgnoreCase(userType)) {
                return applicationDao.findReviewItemsForAdmin(userId);
            }
            return Collections.emptyList();
        } catch (SQLException exception) {
            req.setAttribute("dashboardError", "Unable to load applications for review due to a database error. Please refresh and try again.");
            return Collections.emptyList();
        }
    }

    private List<Job> loadManagedJobs(HttpServletRequest req, String userType, Integer userId) {
        if (userType == null || userId == null || (!"admin".equalsIgnoreCase(userType) && !"recruiter".equalsIgnoreCase(userType))) {
            return Collections.emptyList();
        }
        try {
            return jobDao.findByAdminId(userId, 50);
        } catch (SQLException exception) {
            req.setAttribute("dashboardError", "Unable to load your managed jobs due to a database error. Please refresh and try again.");
            return Collections.emptyList();
        }
    }
}
