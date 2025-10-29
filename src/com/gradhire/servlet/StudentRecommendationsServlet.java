package com.gradhire.servlet;

import com.gradhire.dao.JobDAO;
import com.gradhire.dao.SkillDAO;
import com.gradhire.dao.StudentDAO;
import com.gradhire.model.Student;
import com.gradhire.util.RecommendationEngine.JobRecommendation;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * StudentRecommendationsServlet handles the student recommendations page.
 * 
 * URL Pattern: /student/recommendations
 * Methods: GET
 */
public class StudentRecommendationsServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(StudentRecommendationsServlet.class.getName());
    
    private StudentDAO studentDAO;
    private JobDAO jobDAO;
    private SkillDAO skillDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        studentDAO = new StudentDAO();
        jobDAO = new JobDAO();
        skillDAO = new SkillDAO();
        logger.info("StudentRecommendationsServlet initialized");
    }
    
    /**
     * GET /student/recommendations - Display job recommendations page
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
            
            // Load student's skills for recommendation engine
            student.setSkills(skillDAO.getStudentSkills(student.getStudentId()));
            
            // Get smart recommended jobs based on skill matching (limit 20 for full page)
            List<JobRecommendation> recommendedJobsWithScores = jobDAO.getRecommendedJobsNotApplied(student, 20);
            
            // Set attributes for JSP
            request.setAttribute("student", student);
            request.setAttribute("recommendedJobs", recommendedJobsWithScores);
            
            // Forward to JSP
            request.getRequestDispatcher("/WEB-INF/jsp/student/recommendations.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.severe("Error in StudentRecommendationsServlet: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("error", "Unable to load recommendations. Please try again.");
            request.getRequestDispatcher("/WEB-INF/jsp/student/recommendations.jsp").forward(request, response);
        }
    }
}
