package com.gradhire.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * AuthorizationFilter ensures users can only access resources appropriate for their role.
 * 
 * This filter checks if a user's role matches the requested resource:
 * - Students can only access /student/* URLs
 * - Admins can only access /admin/* URLs
 */
public class AuthorizationFilter implements Filter {
    
    private static final Logger logger = Logger.getLogger(AuthorizationFilter.class.getName());
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("AuthorizationFilter initialized");
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        
        // Get the path after context
        String path = requestURI.substring(contextPath.length());
        
        // Get session
        HttpSession session = httpRequest.getSession(false);
        
        // This filter should only run after AuthenticationFilter
        // So we can assume user is logged in at this point
        if (session == null || session.getAttribute("user") == null) {
            // This shouldn't happen if AuthenticationFilter ran first
            // But redirect to login as a safety measure
            httpResponse.sendRedirect(contextPath + "/login");
            return;
        }
        
        // Get user type from session
        String userType = (String) session.getAttribute("userType");
        
        if (userType == null) {
            // Invalid session state
            logger.severe("User logged in but no userType in session");
            session.invalidate();
            httpResponse.sendRedirect(contextPath + "/login");
            return;
        }
        
        // Check authorization based on path and user type
        boolean isAuthorized = false;
        
        if (path.startsWith("/student/")) {
            // Student area - only students allowed
            isAuthorized = "student".equals(userType);
        } else if (path.startsWith("/admin/")) {
            // Admin area - only admins allowed
            isAuthorized = "admin".equals(userType);
        }
        
        if (isAuthorized) {
            // User is authorized, continue the request
            logger.fine("User authorized for: " + path + " (Type: " + userType + ")");
            chain.doFilter(request, response);
        } else {
            // User is not authorized for this resource
            logger.warning("Unauthorized access attempt by " + userType + " to: " + path);
            
            // Send 403 Forbidden error
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, 
                "You don't have permission to access this resource");
        }
    }
    
    @Override
    public void destroy() {
        logger.info("AuthorizationFilter destroyed");
    }
}
