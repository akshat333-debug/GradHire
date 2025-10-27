package com.gradhire.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

/**
 * AuthenticationFilter ensures users are logged in before accessing protected resources.
 * 
 * This filter checks if a user session exists before allowing access to
 * protected URLs (student/* and admin/*).
 */
public class AuthenticationFilter implements Filter {
    
    private static final Logger logger = Logger.getLogger(AuthenticationFilter.class.getName());
    
    // URLs that don't require authentication (if any within protected paths)
    private static final List<String> EXCLUDED_URLS = Arrays.asList(
        // Add any excluded URLs here if needed
    );
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("AuthenticationFilter initialized");
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
        
        // Check if URL is excluded from authentication
        if (isExcludedUrl(path)) {
            chain.doFilter(request, response);
            return;
        }
        
        // Get session (don't create new one)
        HttpSession session = httpRequest.getSession(false);
        
        // Check if user is logged in
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);
        
        if (isLoggedIn) {
            // User is authenticated, continue the request
            logger.fine("User authenticated for: " + path);
            chain.doFilter(request, response);
        } else {
            // User is not authenticated, redirect to login
            logger.warning("Unauthenticated access attempt to: " + path);
            
            // Store the original URL to redirect after login
            session = httpRequest.getSession(true);
            session.setAttribute("redirectAfterLogin", path);
            
            // Redirect to login page
            httpResponse.sendRedirect(contextPath + "/login");
        }
    }
    
    @Override
    public void destroy() {
        logger.info("AuthenticationFilter destroyed");
    }
    
    /**
     * Check if the URL is excluded from authentication
     */
    private boolean isExcludedUrl(String path) {
        for (String excludedUrl : EXCLUDED_URLS) {
            if (path.startsWith(excludedUrl)) {
                return true;
            }
        }
        return false;
    }
}
