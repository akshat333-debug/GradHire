package com.gradhire.servlet;

import com.gradhire.dao.ApplicationDAO;
import com.gradhire.dao.JobDAO;
import com.gradhire.dao.SkillDAO;
import com.gradhire.dao.StudentDAO;
import com.gradhire.model.Application;
import com.gradhire.model.ApplicationStats;
import com.gradhire.model.Job;
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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * StudentDashboardServlet handles the student dashboard and related views.
 * 
 * URL Pattern: /student/dashboard
 * Methods: GET
 */
public class StudentDashboardServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(StudentDashboardServlet.class.getName());
    
    private StudentDAO studentDAO;
    private ApplicationDAO applicationDAO;
    private JobDAO jobDAO;
    private SkillDAO skillDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        studentDAO = new StudentDAO();
        applicationDAO = new ApplicationDAO();
        jobDAO = new JobDAO();
        skillDAO = new SkillDAO();
        logger.info("StudentDashboardServlet initialized");
    }
    
    /**
     * GET /student/dashboard - Display student dashboard
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
            
            // Get application statistics
            ApplicationStats stats = applicationDAO.getStatusStatsByStudent(student.getStudentId());
            
            // Get recent applications (limit 5)
            List<Application> recentApplications = applicationDAO.getRecentApplications(student.getStudentId(), 5);
            
            // Get smart recommended jobs based on skill matching (limit 5)
            // This uses Jaccard similarity and keyword matching algorithms
            List<JobRecommendation> recommendedJobsWithScores = jobDAO.getRecommendedJobsNotApplied(student, 5);
            
            // Calculate profile completion percentage
            int profileCompletion = calculateProfileCompletion(student);
            
            // Set attributes for JSP
            request.setAttribute("student", student);
            request.setAttribute("applicationStats", stats);
            request.setAttribute("recentApplications", recentApplications);
            request.setAttribute("recommendedJobs", recommendedJobsWithScores); // Now includes match scores!
            request.setAttribute("profileCompletion", profileCompletion);
            request.setAttribute("notificationCount", 0); // TODO: Implement notifications
            
            // Forward to JSP
            request.getRequestDispatcher("/WEB-INF/jsp/student/dashboard.jsp").forward(request, response);
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error loading student dashboard", e);
            request.setAttribute("error", "Error loading dashboard. Please try again.");
            request.getRequestDispatcher("/WEB-INF/jsp/student/dashboard.jsp").forward(request, response);
        }
    }
    
    /**
     * Calculate profile completion percentage
     */
    private int calculateProfileCompletion(Student student) {
        int total = 0;
        int completed = 0;
        
        // Basic info (5 fields)
        total += 5;
        if (student.getFirstName() != null && !student.getFirstName().isEmpty()) completed++;
        if (student.getLastName() != null && !student.getLastName().isEmpty()) completed++;
        if (student.getEmail() != null && !student.getEmail().isEmpty()) completed++;
        if (student.getPhone() != null && !student.getPhone().isEmpty()) completed++;
        if (student.getProfilePicture() != null && !student.getProfilePicture().isEmpty()) completed++;
        
        // Education (4 fields)
        total += 4;
        if (student.getUniversity() != null && !student.getUniversity().isEmpty()) completed++;
        if (student.getMajor() != null && !student.getMajor().isEmpty()) completed++;
        if (student.getGraduationYear() != null) completed++;
        if (student.getGpa() != null) completed++;
        
        // Resume
        total += 1;
        if (student.getResume() != null && !student.getResume().isEmpty()) completed++;
        
        // Bio
        total += 1;
        if (student.getBio() != null && !student.getBio().isEmpty()) completed++;
        
        // Social links (optional but counted)
        total += 2;
        if (student.getLinkedIn() != null && !student.getLinkedIn().isEmpty()) completed++;
        if (student.getGitHub() != null && !student.getGitHub().isEmpty()) completed++;
        
        return (int) ((completed / (double) total) * 100);
    }
}
