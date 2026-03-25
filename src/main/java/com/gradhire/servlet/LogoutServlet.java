package com.gradhire.servlet;

import com.gradhire.util.SessionUtil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        SessionUtil.clearSession(req);
        resp.sendRedirect(req.getContextPath() + "/auth/login");
    }
}
