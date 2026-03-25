package com.gradhire.servlet;

import com.gradhire.dao.AuthDao;
import com.gradhire.model.AuthResult;
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

public class LoginServlet extends HttpServlet {
    private final AuthDao authDao = new AuthDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/login.jsp");
        dispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = normalize(req.getParameter("email"));
        String password = req.getParameter("password");
        String role = normalize(req.getParameter("role"));

        if (email == null || password == null || role == null) {
            req.setAttribute("error", "Email, password and role are required.");
            doGet(req, resp);
            return;
        }

        try {
            Optional<AuthResult> authResult = authenticate(role, email, password);
            if (authResult.isEmpty()) {
                req.setAttribute("error", "Invalid credentials.");
                doGet(req, resp);
                return;
            }

            AuthResult user = authResult.get();
            HttpSession session = req.getSession(true);
            session.setAttribute(SessionUtil.USER_ID, user.getUserId());
            session.setAttribute(SessionUtil.USER_TYPE, user.getUserType());
            session.setAttribute(SessionUtil.USER_NAME, user.getFullName());

            resp.sendRedirect(req.getContextPath() + "/dashboard");
        } catch (SQLException exception) {
            req.setAttribute("error", "Login failed due to a server error.");
            doGet(req, resp);
        }
    }

    private Optional<AuthResult> authenticate(String role, String email, String password) throws SQLException {
        if ("student".equalsIgnoreCase(role)) {
            return authDao.authenticateStudent(email, password);
        }

        if ("recruiter".equalsIgnoreCase(role) || "admin".equalsIgnoreCase(role)) {
            return authDao.authenticateAdminOrRecruiter(email, password);
        }

        return Optional.empty();
    }

    private String normalize(String value) {
        if (value == null) {
            return null;
        }

        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
