package com.gradhire.util;

import com.gradhire.model.Admin;
import com.gradhire.model.Student;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.logging.Logger;

/**
 * SessionManager provides utility methods for managing user sessions.
 * 
 * This class centralizes session operations like checking authentication,
 * getting user information, and managing session attributes.
 */
public class SessionManager {
    
    private static final Logger logger = Logger.getLogger(SessionManager.class.getName());
    
    // Session attribute keys
    public static final String USER_ATTRIBUTE = "user";
    public static final String USER_TYPE_ATTRIBUTE = "userType";
    public static final String REDIRECT_AFTER_LOGIN = "redirectAfterLogin";
    
    // User types
    public static final String USER_TYPE_STUDENT = "student";
    public static final String USER_TYPE_ADMIN = "admin";
    
    // Default session timeout (30 minutes)
    private static final int DEFAULT_SESSION_TIMEOUT = 30 * 60;
    
    /**
     * Check if a user is currently logged in
     */
    public static boolean isLoggedIn(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session != null && session.getAttribute(USER_ATTRIBUTE) != null;
    }
    
    /**
     * Check if the logged-in user is a student
     */
    public static boolean isStudent(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        String userType = (String) session.getAttribute(USER_TYPE_ATTRIBUTE);
        return USER_TYPE_STUDENT.equals(userType);
    }
    
    /**
     * Check if the logged-in user is an admin
     */
    public static boolean isAdmin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        String userType = (String) session.getAttribute(USER_TYPE_ATTRIBUTE);
        return USER_TYPE_ADMIN.equals(userType);
    }
    
    /**
     * Get the current user object from session
     */
    public static Object getUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return session.getAttribute(USER_ATTRIBUTE);
    }
    
    /**
     * Get the current student from session
     * Returns null if not logged in or not a student
     */
    public static Student getStudent(HttpServletRequest request) {
        if (!isStudent(request)) {
            return null;
        }
        HttpSession session = request.getSession(false);
        return (Student) session.getAttribute(USER_ATTRIBUTE);
    }
    
    /**
     * Get the current admin from session
     * Returns null if not logged in or not an admin
     */
    public static Admin getAdmin(HttpServletRequest request) {
        if (!isAdmin(request)) {
            return null;
        }
        HttpSession session = request.getSession(false);
        return (Admin) session.getAttribute(USER_ATTRIBUTE);
    }
    
    /**
     * Get the user type (student or admin)
     */
    public static String getUserType(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        return (String) session.getAttribute(USER_TYPE_ATTRIBUTE);
    }
    
    /**
     * Create a new session for a student
     */
    public static void createStudentSession(HttpServletRequest request, Student student) {
        createStudentSession(request, student, false);
    }
    
    /**
     * Create a new session for a student with optional remember me
     */
    public static void createStudentSession(HttpServletRequest request, Student student, boolean rememberMe) {
        HttpSession session = request.getSession(true);
        session.setAttribute(USER_ATTRIBUTE, student);
        session.setAttribute(USER_TYPE_ATTRIBUTE, USER_TYPE_STUDENT);
        
        // Set session timeout
        if (rememberMe) {
            // Remember me: 7 days
            session.setMaxInactiveInterval(7 * 24 * 60 * 60);
        } else {
            // Default: 30 minutes
            session.setMaxInactiveInterval(DEFAULT_SESSION_TIMEOUT);
        }
        
        logger.info("Student session created: ID=" + student.getStudentId() + 
                   ", RememberMe=" + rememberMe);
    }
    
    /**
     * Create a new session for an admin
     */
    public static void createAdminSession(HttpServletRequest request, Admin admin) {
        createAdminSession(request, admin, false);
    }
    
    /**
     * Create a new session for an admin with optional remember me
     */
    public static void createAdminSession(HttpServletRequest request, Admin admin, boolean rememberMe) {
        HttpSession session = request.getSession(true);
        session.setAttribute(USER_ATTRIBUTE, admin);
        session.setAttribute(USER_TYPE_ATTRIBUTE, USER_TYPE_ADMIN);
        
        // Set session timeout
        if (rememberMe) {
            // Remember me: 7 days
            session.setMaxInactiveInterval(7 * 24 * 60 * 60);
        } else {
            // Default: 30 minutes
            session.setMaxInactiveInterval(DEFAULT_SESSION_TIMEOUT);
        }
        
        logger.info("Admin session created: ID=" + admin.getAdminId() + 
                   ", RememberMe=" + rememberMe);
    }
    
    /**
     * Update the user object in session (after profile update, etc.)
     */
    public static void updateUser(HttpServletRequest request, Object user) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.setAttribute(USER_ATTRIBUTE, user);
            logger.fine("User object updated in session");
        }
    }
    
    /**
     * Invalidate the current session (logout)
     */
    public static void invalidateSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String userType = (String) session.getAttribute(USER_TYPE_ATTRIBUTE);
            session.invalidate();
            logger.info("Session invalidated: UserType=" + userType);
        }
    }
    
    /**
     * Store the URL to redirect to after login
     */
    public static void setRedirectAfterLogin(HttpServletRequest request, String url) {
        HttpSession session = request.getSession(true);
        session.setAttribute(REDIRECT_AFTER_LOGIN, url);
    }
    
    /**
     * Get and remove the redirect URL after login
     */
    public static String getAndClearRedirectAfterLogin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        String redirect = (String) session.getAttribute(REDIRECT_AFTER_LOGIN);
        if (redirect != null) {
            session.removeAttribute(REDIRECT_AFTER_LOGIN);
        }
        return redirect;
    }
    
    /**
     * Get the appropriate dashboard URL for the current user
     */
    public static String getDashboardUrl(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        if (isStudent(request)) {
            return contextPath + "/student/dashboard";
        } else if (isAdmin(request)) {
            return contextPath + "/admin/dashboard";
        }
        return contextPath + "/";
    }
    
    /**
     * Set a success message in session
     */
    public static void setSuccessMessage(HttpServletRequest request, String message) {
        HttpSession session = request.getSession(true);
        session.setAttribute("success", message);
    }
    
    /**
     * Set an error message in session
     */
    public static void setErrorMessage(HttpServletRequest request, String message) {
        HttpSession session = request.getSession(true);
        session.setAttribute("error", message);
    }
    
    /**
     * Get and clear the success message
     */
    public static String getAndClearSuccessMessage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        String message = (String) session.getAttribute("success");
        if (message != null) {
            session.removeAttribute("success");
        }
        return message;
    }
    
    /**
     * Get and clear the error message
     */
    public static String getAndClearErrorMessage(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        String message = (String) session.getAttribute("error");
        if (message != null) {
            session.removeAttribute("error");
        }
        return message;
    }
}
