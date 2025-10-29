package com.gradhire.servlet;

import com.gradhire.dao.StudentDAO;
import com.gradhire.model.Admin;
import com.gradhire.model.Student;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

public class AdminCandidatesServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(AdminCandidatesServlet.class.getName());

    private StudentDAO studentDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        studentDAO = new StudentDAO();
        logger.info("AdminCandidatesServlet initialized");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"admin".equals(session.getAttribute("userType"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String q = request.getParameter("q");
        List<Student> candidates;
        if (q != null && !q.trim().isEmpty()) {
            candidates = studentDAO.search(q.trim());
        } else {
            candidates = studentDAO.findAll(100, 0);
        }
        request.setAttribute("candidates", candidates);
        request.setAttribute("query", q);
        request.getRequestDispatcher("/WEB-INF/jsp/admin/candidates.jsp").forward(request, response);
    }
}
