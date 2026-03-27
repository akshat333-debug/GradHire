package com.gradhire.servlet;

import com.gradhire.dao.ActivityLogDao;
import com.gradhire.dao.StudentDao;
import com.gradhire.model.AuthResult;
import com.gradhire.util.PasswordUtil;
import com.gradhire.util.SessionUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

public class RegisterServlet extends HttpServlet {
    private static final int EMAIL_MAX = 100;
    private static final int PASSWORD_MIN = 8;
    private static final int PASSWORD_MAX = 72;
    private static final int FULL_NAME_MAX = 100;
    private static final int COLLEGE_MAX = 150;

    private final StudentDao studentDao = new StudentDao();
    private final ActivityLogDao activityLogDao = new ActivityLogDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (SessionUtil.isLoggedIn(req)) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/register.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (SessionUtil.isLoggedIn(req)) {
            resp.sendRedirect(req.getContextPath() + "/dashboard");
            return;
        }

        String email = normalize(req.getParameter("email"));
        String password = req.getParameter("password");
        String fullName = normalize(req.getParameter("fullName"));
        String collegeName = normalize(req.getParameter("collegeName"));

        if (email == null || password == null || fullName == null) {
            req.setAttribute("error", "Email, password, and full name are required.");
            doGet(req, resp);
            return;
        }
        if (email.length() > EMAIL_MAX || fullName.length() > FULL_NAME_MAX || (collegeName != null && collegeName.length() > COLLEGE_MAX)) {
            req.setAttribute("error", "One or more fields exceed allowed length.");
            doGet(req, resp);
            return;
        }
        if (password.length() < PASSWORD_MIN || password.length() > PASSWORD_MAX) {
            req.setAttribute("error", "Password must be between 8 and 72 characters.");
            doGet(req, resp);
            return;
        }

        try {
            if (studentDao.findByEmail(email).isPresent()) {
                req.setAttribute("error", "An account with this email already exists.");
                doGet(req, resp);
                return;
            }

            int studentId = studentDao.createStudent(
                    email,
                    PasswordUtil.hash(password),
                    fullName,
                    collegeName
            );
            try {
                activityLogDao.logActivity("student", studentId, "register", "Student account created.", req.getRemoteAddr(), req.getHeader("User-Agent"));
            } catch (SQLException ignored) {
                // Non-blocking audit log.
            }

            AuthResult user = new AuthResult(studentId, "student", fullName);
            HttpSession session = req.getSession(true);
            req.changeSessionId();
            session.setAttribute(SessionUtil.USER_ID, user.getUserId());
            session.setAttribute(SessionUtil.USER_TYPE, user.getUserType());
            session.setAttribute(SessionUtil.USER_NAME, user.getFullName());
            session.setMaxInactiveInterval(30 * 60);
            resp.sendRedirect(req.getContextPath() + "/dashboard");
        } catch (SQLException exception) {
            req.setAttribute("error", "Unable to register right now due to a system error. Please try again.");
            doGet(req, resp);
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
