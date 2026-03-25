package com.gradhire.servlet;

import com.gradhire.dao.ActivityLogDao;
import com.gradhire.dao.AdminDao;
import com.gradhire.dao.StudentDao;
import com.gradhire.model.Admin;
import com.gradhire.model.Student;
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
import java.util.Set;

public class ProfileServlet extends HttpServlet {
    private static final int FULL_NAME_MAX = 150;
    private static final int COLLEGE_MAX = 150;
    private static final int COMPANY_MAX = 150;
    private static final Set<String> ALLOWED_ADMIN_ROLES = Set.of("admin", "recruiter");
    private final StudentDao studentDao = new StudentDao();
    private final AdminDao adminDao = new AdminDao();
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
        if (userType == null || userId == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        try {
            if ("student".equalsIgnoreCase(userType)) {
                Optional<Student> student = studentDao.findById(userId);
                if (student.isPresent()) {
                    req.setAttribute("profileStudent", student.get());
                } else {
                    req.setAttribute("profileError", "Profile not found.");
                }
            } else if ("recruiter".equalsIgnoreCase(userType) || "admin".equalsIgnoreCase(userType)) {
                Optional<Admin> admin = adminDao.findById(userId);
                if (admin.isPresent()) {
                    req.setAttribute("profileAdmin", admin.get());
                } else {
                    req.setAttribute("profileError", "Profile not found.");
                }
            } else {
                req.setAttribute("profileError", "Unsupported user role.");
            }
        } catch (SQLException exception) {
            req.setAttribute("profileError", "Unable to load profile due to a database error.");
        }

        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/profile.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!SessionUtil.isLoggedIn(req)) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }
        HttpSession session = req.getSession(false);
        String userType = (String) session.getAttribute(SessionUtil.USER_TYPE);
        Integer userId = (Integer) session.getAttribute(SessionUtil.USER_ID);
        if (userType == null || userId == null) {
            resp.sendRedirect(req.getContextPath() + "/auth/login");
            return;
        }

        String fullName = normalize(req.getParameter("fullName"));
        if (fullName == null || fullName.length() > FULL_NAME_MAX) {
            req.setAttribute("profileError", "A valid full name is required.");
            doGet(req, resp);
            return;
        }

        try {
            boolean updated = false;
            if ("student".equalsIgnoreCase(userType)) {
                String collegeName = normalize(req.getParameter("collegeName"));
                if (collegeName != null && collegeName.length() > COLLEGE_MAX) {
                    req.setAttribute("profileError", "College name is too long.");
                    doGet(req, resp);
                    return;
                }
                updated = studentDao.updateStudentProfile(userId, fullName, collegeName);
                if (updated) {
                    activityLogDao.logActivity("student", userId, "profile_update", "Updated student profile.", req.getRemoteAddr(), req.getHeader("User-Agent"));
                }
            } else if ("recruiter".equalsIgnoreCase(userType) || "admin".equalsIgnoreCase(userType)) {
                String companyName = normalize(req.getParameter("companyName"));
                String role = normalize(req.getParameter("role"));
                if (companyName != null && companyName.length() > COMPANY_MAX) {
                    req.setAttribute("profileError", "Company name is too long.");
                    doGet(req, resp);
                    return;
                }
                if (role == null || !ALLOWED_ADMIN_ROLES.contains(role.toLowerCase())) {
                    req.setAttribute("profileError", "Invalid role selected.");
                    doGet(req, resp);
                    return;
                }
                if ("recruiter".equalsIgnoreCase(userType) && "admin".equalsIgnoreCase(role)) {
                    req.setAttribute("profileError", "Recruiters cannot escalate role to admin.");
                    doGet(req, resp);
                    return;
                }
                updated = adminDao.updateAdminProfile(userId, fullName, companyName, role);
                if (updated) {
                    activityLogDao.logActivity(userType, userId, "profile_update", "Updated admin/recruiter profile.", req.getRemoteAddr(), req.getHeader("User-Agent"));
                }
            }

            if (updated) {
                session.setAttribute(SessionUtil.USER_NAME, fullName);
                req.setAttribute("profileSuccess", "Profile updated successfully.");
            } else {
                req.setAttribute("profileError", "Profile update failed.");
            }
        } catch (SQLException exception) {
            req.setAttribute("profileError", "Unable to update profile due to a database error.");
        }

        doGet(req, resp);
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
