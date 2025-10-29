package com.gradhire.servlet;

import com.gradhire.dao.ApplicationDAO;
import com.gradhire.dao.StudentDAO;
import com.gradhire.model.Application;
import com.gradhire.model.Student;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * StudentApplicationsServlet handles the student applications page.
 * 
 * URL Pattern: /student/applications
 * Methods: GET
 */
public class StudentApplicationsServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(StudentApplicationsServlet.class.getName());
    
    private StudentDAO studentDAO;
    private ApplicationDAO applicationDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        studentDAO = new StudentDAO();
        applicationDAO = new ApplicationDAO();
        logger.info("StudentApplicationsServlet initialized");
    }
    
    /**
     * GET /student/applications - Display student applications page
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Check authentication
        HttpSession session = request.getSession(false);
        if (session == null || !"student".equals(session.getAttribute("userType"))) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        
        try {
            Student student = (Student) session.getAttribute("user");
            
            // Refresh student data from database
            Student refreshedStudent = studentDAO.findById(student.getStudentId());
            if (refreshedStudent != null) {
                student = refreshedStudent;
                session.setAttribute("user", student);
            }
            
            // Get all applications for this student
            List<Application> applications = applicationDAO.findByStudent(student.getStudentId());
            
            // Set attributes for JSP
            request.setAttribute("student", student);
            request.setAttribute("applications", applications);
            
            // Forward to JSP
            request.getRequestDispatcher("/WEB-INF/jsp/student/applications.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.severe("Error in StudentApplicationsServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Unable to load applications. Please try again.");
            request.getRequestDispatcher("/WEB-INF/jsp/student/applications.jsp").forward(request, response);
        }
    }
}
