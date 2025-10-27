package com.gradhire.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * LogoutServlet handles user logout and session termination.
 * 
 * URL Pattern: /logout
 * Methods: GET
 */
public class LogoutServlet extends HttpServlet {
    
    private static final Logger logger = Logger.getLogger(LogoutServlet.class.getName());
    
    /**
     * GET /logout - Terminate user session and redirect to home
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get current session
        HttpSession session = request.getSession(false);
        
        if (session != null) {
            // Log the logout
            Object user = session.getAttribute("user");
            String userType = (String) session.getAttribute("userType");
            
            if (user != null) {
                logger.info("User logged out: Type=" + userType);
            }
            
            // Invalidate session
            session.invalidate();
        }
        
        // Redirect to home page with success message
        HttpSession newSession = request.getSession(true);
        newSession.setAttribute("success", "You have been logged out successfully");
        
        response.sendRedirect(request.getContextPath() + "/");
    }
}
