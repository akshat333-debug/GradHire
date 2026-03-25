package com.gradhire.servlet;

import com.gradhire.dao.ApplicationDao;
import com.gradhire.dao.ActivityLogDao;
import com.gradhire.dao.JobDao;
import com.gradhire.dao.RecommendationDao;
import com.gradhire.dao.SavedJobDao;
import com.gradhire.model.ActivityLog;
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
    private final RecommendationDao recommendationDao = new RecommendationDao();
    private final SavedJobDao savedJobDao = new SavedJobDao();
    private final ActivityLogDao activityLogDao = new ActivityLogDao();

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
        Object savedJobError = session.getAttribute("savedJobError");
        if (savedJobError != null) {
            req.setAttribute("savedJobError", savedJobError);
            session.removeAttribute("savedJobError");
        }
        Object savedJobSuccess = session.getAttribute("savedJobSuccess");
        if (savedJobSuccess != null) {
            req.setAttribute("savedJobSuccess", savedJobSuccess);
            session.removeAttribute("savedJobSuccess");
        }

        List<Job> jobs = loadJobs(req);
        List<Application> applications = loadApplications(req, userType, userId);
        List<Job> savedJobs = loadSavedJobs(req, userType, userId);
        req.setAttribute("jobs", jobs);
        req.setAttribute("applications", applications);
        req.setAttribute("appliedJobIds", buildAppliedJobIds(applications));
        req.setAttribute("reviewApplications", loadReviewApplications(req, userType, userId));
        req.setAttribute("managedJobs", loadManagedJobs(req, userType, userId));
        req.setAttribute("recommendedJobs", loadRecommendations(req, userType, userId));
        req.setAttribute("savedJobs", savedJobs);
        req.setAttribute("savedJobIds", buildJobIds(savedJobs));
        req.setAttribute("activityLogs", loadActivityLogs(req, userType, userId));

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
            if ("admin".equalsIgnoreCase(userType)) {
                return jobDao.findAllLimited(100);
            }
            return jobDao.findByAdminId(userId, 50);
        } catch (SQLException exception) {
            req.setAttribute("dashboardError", "Unable to load your managed jobs due to a database error. Please refresh and try again.");
            return Collections.emptyList();
        }
    }

    private List<Job> loadRecommendations(HttpServletRequest req, String userType, Integer userId) {
        if (!"student".equalsIgnoreCase(userType) || userId == null) {
            return Collections.emptyList();
        }
        try {
            return recommendationDao.findRecommendationsForStudent(userId, 10);
        } catch (SQLException exception) {
            req.setAttribute("dashboardError", "Unable to load recommendations due to a database error. Please refresh and try again.");
            return Collections.emptyList();
        }
    }

    private List<Job> loadSavedJobs(HttpServletRequest req, String userType, Integer userId) {
        if (!"student".equalsIgnoreCase(userType) || userId == null) {
            return Collections.emptyList();
        }
        try {
            return savedJobDao.findSavedJobsByStudentId(userId, 20);
        } catch (SQLException exception) {
            req.setAttribute("dashboardError", "Unable to load saved jobs due to a database error. Please refresh and try again.");
            return Collections.emptyList();
        }
    }

    private Set<Integer> buildJobIds(List<Job> jobs) {
        if (jobs == null || jobs.isEmpty()) {
            return Collections.emptySet();
        }
        Set<Integer> ids = new HashSet<>();
        for (Job job : jobs) {
            ids.add(job.getJobId());
        }
        return ids;
    }

    private List<ActivityLog> loadActivityLogs(HttpServletRequest req, String userType, Integer userId) {
        if (userType == null || userId == null) {
            return Collections.emptyList();
        }
        try {
            return activityLogDao.findRecentByUser(userType, userId, 20);
        } catch (SQLException exception) {
            req.setAttribute("dashboardError", "Unable to load activity logs due to a database error. Please refresh and try again.");
            return Collections.emptyList();
        }
    }
}
