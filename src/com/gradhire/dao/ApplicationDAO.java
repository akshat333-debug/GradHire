package com.gradhire.dao;

import com.gradhire.exception.DataAccessException;
import com.gradhire.model.Application;
import com.gradhire.model.ApplicationStats;
import com.gradhire.model.ApplicationStatus;
import com.gradhire.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Application Data Access Object
 * Handles all database operations for Application entity
 * 
 * @author GradHire Team
 * @version 1.0
 */
public class ApplicationDAO {
    
    private static final Logger logger = Logger.getLogger(ApplicationDAO.class.getName());
    
    /**
     * Create a new application
     * 
     * @param application Application object to insert
     * @return Generated application ID, or null if failed
     * @throws IllegalArgumentException if application is null or required fields are null
     * @throws DataAccessException if database operation fails
     */
    public Integer createApplication(Application application) {
        // Validate input before acquiring resources
        if (application == null || application.getJobId() == null || application.getStudentId() == null) {
            throw new IllegalArgumentException("Application and required IDs cannot be null");
        }
        
        String sql = "INSERT INTO applications (job_id, student_id, cover_letter) VALUES (?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setInt(1, application.getJobId());
            pstmt.setInt(2, application.getStudentId());
            pstmt.setString(3, application.getCoverLetter());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    Integer generatedId = rs.getInt(1);
                    logger.log(Level.INFO, "Successfully created application with ID: {0}", generatedId);
                    return generatedId;
                }
            }
            logger.log(Level.WARNING, "Application creation returned 0 affected rows");
            return null;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to create application", e);
            throw new DataAccessException("Failed to create application", e);
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
    }
    
    /**
     * Find application by ID (excludes soft-deleted applications)
     * 
     * @param applicationId Application ID
     * @return Application object or null if not found
     */
    public Application findById(Integer applicationId) {
        if (applicationId == null) {
            throw new IllegalArgumentException("applicationId must not be null");
        }
        
        String sql = "SELECT * FROM applications WHERE application_id = ? AND deleted_at IS NULL";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, applicationId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractApplicationFromResultSet(rs);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to find application by ID: " + applicationId, e);
            throw new DataAccessException("Failed to find application by ID", e);
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return null;
    }
    
    /**
     * Find application by ID including soft-deleted (for admin purposes)
     * 
     * @param applicationId Application ID
     * @return Application object or null if not found
     */
    public Application findByIdIncludingDeleted(Integer applicationId) {
        if (applicationId == null) {
            throw new IllegalArgumentException("applicationId must not be null");
        }
        
        String sql = "SELECT * FROM applications WHERE application_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, applicationId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractApplicationFromResultSet(rs);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to find application by ID (including deleted): " + applicationId, e);
            throw new DataAccessException("Failed to find application by ID (including deleted)", e);
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return null;
    }
    
    /**
     * Update application status
     * 
     * @param applicationId Application ID
     * @param status New status
     * @return true if successful
     * @throws IllegalArgumentException if applicationId or status is null, or status is invalid
     */
    public boolean updateStatus(Integer applicationId, String status) {
        // Validate parameters before acquiring resources
        if (applicationId == null) {
            throw new IllegalArgumentException("Application ID cannot be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        if (!ApplicationStatus.isValidStatus(status)) {
            throw new IllegalArgumentException("Invalid status: " + status + 
                ". Valid statuses are: Pending, Reviewed, Shortlisted, Accepted, Rejected");
        }
        
        String sql = "UPDATE applications SET application_status = ?, reviewed_at = ? WHERE application_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(3, applicationId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to update application status for ID: " + applicationId, e);
            throw new DataAccessException("Failed to update application status", e);
        } finally {
            DBConnection.closeResources(pstmt, conn);
        }
    }
    
    /**
     * Update application with reviewer notes
     * 
     * @param applicationId Application ID
     * @param status New status
     * @param notes Reviewer notes
     * @return true if successful
     * @throws IllegalArgumentException if applicationId or status is null, status is invalid,
     *         or notes is null/empty when status requires reviewer feedback
     */
    public boolean updateWithNotes(Integer applicationId, String status, String notes) {
        // Validate parameters before acquiring resources
        if (applicationId == null) {
            throw new IllegalArgumentException("Application ID cannot be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        if (!ApplicationStatus.isValidStatus(status)) {
            throw new IllegalArgumentException("Invalid status: " + status + 
                ". Valid statuses are: Pending, Reviewed, Shortlisted, Accepted, Rejected");
        }
        
        // Validate notes for statuses that require reviewer feedback
        // Statuses other than Pending indicate reviewer action and should have notes
        if (!ApplicationStatus.PENDING.getValue().equals(status)) {
            if (notes == null) {
                throw new IllegalArgumentException("Reviewer notes cannot be null for status: " + status);
            }
            if (notes.trim().isEmpty()) {
                throw new IllegalArgumentException("Reviewer notes cannot be empty for status: " + status);
            }
        }
        
        String sql = "UPDATE applications SET application_status = ?, reviewer_notes = ?, reviewed_at = ? " +
                    "WHERE application_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setString(2, notes);
            pstmt.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(4, applicationId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to update application with notes for ID: " + applicationId, e);
            throw new DataAccessException("Failed to update application with notes", e);
        } finally {
            DBConnection.closeResources(pstmt, conn);
        }
    }
    
    /**
     * Soft delete/withdraw application (recommended for auditability)
     * Marks the application as deleted without removing it from the database
     * 
     * @param applicationId Application ID
     * @return true if successful
     * @throws IllegalArgumentException if applicationId is null
     */
    public boolean softDeleteApplication(Integer applicationId) {
        // Validate parameter before acquiring resources
        if (applicationId == null) {
            throw new IllegalArgumentException("Application ID cannot be null");
        }
        
        String sql = "UPDATE applications SET deleted_at = ? WHERE application_id = ? AND deleted_at IS NULL";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(2, applicationId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to soft delete application for ID: " + applicationId, e);
            throw new DataAccessException("Failed to soft delete application", e);
        } finally {
            DBConnection.closeResources(pstmt, conn);
        }
    }
    
    /**
     * Hard delete application (use sparingly, for admin/cleanup purposes only)
     * Permanently removes the application from the database
     * 
     * @param applicationId Application ID
     * @return true if successful
     * @throws IllegalArgumentException if applicationId is null
     */
    public boolean deleteApplication(Integer applicationId) {
        // Validate parameter before acquiring resources
        if (applicationId == null) {
            throw new IllegalArgumentException("Application ID cannot be null");
        }
        
        String sql = "DELETE FROM applications WHERE application_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, applicationId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to hard delete application for ID: " + applicationId, e);
            throw new DataAccessException("Failed to hard delete application", e);
        } finally {
            DBConnection.closeResources(pstmt, conn);
        }
    }
    
    /**
     * Restore a soft-deleted application
     * 
     * @param applicationId Application ID
     * @return true if successful
     * @throws IllegalArgumentException if applicationId is null
     */
    public boolean restoreApplication(Integer applicationId) {
        // Validate parameter before acquiring resources
        if (applicationId == null) {
            throw new IllegalArgumentException("Application ID cannot be null");
        }
        
        String sql = "UPDATE applications SET deleted_at = NULL WHERE application_id = ? AND deleted_at IS NOT NULL";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, applicationId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to restore application for ID: " + applicationId, e);
            throw new DataAccessException("Failed to restore application", e);
        } finally {
            DBConnection.closeResources(pstmt, conn);
        }
    }
    
    /**
     * Get all applications for a student (excludes soft-deleted)
     * 
     * @param studentId Student ID
     * @return List of applications
     * @throws IllegalArgumentException if studentId is null
     */
    public List<Application> findByStudent(Integer studentId) {
        // Validate parameter before acquiring resources
        if (studentId == null) {
            throw new IllegalArgumentException("Student ID cannot be null");
        }
        
        String sql = "SELECT * FROM applications WHERE student_id = ? AND deleted_at IS NULL ORDER BY applied_at DESC";
        
        List<Application> applications = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentId);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                applications.add(extractApplicationFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to find applications for student ID: " + studentId, e);
            throw new DataAccessException("Failed to find applications for student", e);
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return applications;
    }
    
    /**
     * Get all applications for a job (excludes soft-deleted)
     * 
     * @param jobId Job ID
     * @return List of applications
     * @throws IllegalArgumentException if jobId is null
     */
    public List<Application> findByJob(Integer jobId) {
        // Validate parameter before acquiring resources
        if (jobId == null) {
            throw new IllegalArgumentException("Job ID cannot be null");
        }
        
        String sql = "SELECT * FROM applications WHERE job_id = ? AND deleted_at IS NULL ORDER BY applied_at DESC";
        
        List<Application> applications = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, jobId);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                applications.add(extractApplicationFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to find applications for job ID: " + jobId, e);
            throw new DataAccessException("Failed to find applications for job", e);
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return applications;
    }
    
    /**
     * Get applications by status for a job (excludes soft-deleted)
     * 
     * @param jobId Job ID
     * @param status Application status
     * @return List of applications
     * @throws IllegalArgumentException if jobId or status is null, or status is invalid
     */
    public List<Application> findByJobAndStatus(Integer jobId, String status) {
        // Validate parameters before acquiring resources
        if (jobId == null) {
            throw new IllegalArgumentException("Job ID cannot be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        if (!ApplicationStatus.isValidStatus(status)) {
            throw new IllegalArgumentException("Invalid status: " + status + 
                ". Valid statuses are: Pending, Reviewed, Shortlisted, Accepted, Rejected");
        }
        
        String sql = "SELECT * FROM applications WHERE job_id = ? AND application_status = ? AND deleted_at IS NULL " +
                    "ORDER BY applied_at DESC";
        
        List<Application> applications = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, jobId);
            pstmt.setString(2, status);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                applications.add(extractApplicationFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to find applications for job ID: " + jobId + " with status: " + status, e);
            throw new DataAccessException("Failed to find applications by job and status", e);
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return applications;
    }
    
    /**
     * Check if student has already applied to a job (excludes soft-deleted)
     * 
     * @param studentId Student ID
     * @param jobId Job ID
     * @return true if already applied
     * @throws IllegalArgumentException if studentId or jobId is null
     */
    public boolean hasApplied(Integer studentId, Integer jobId) {
        // Validate parameters before acquiring resources
        if (studentId == null || jobId == null) {
            throw new IllegalArgumentException("Student ID and Job ID cannot be null");
        }
        
        String sql = "SELECT EXISTS(SELECT 1 FROM applications WHERE student_id = ? AND job_id = ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, jobId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getBoolean(1);
            }
            return false;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to check if student " + studentId + " has applied to job " + jobId, e);
            throw new DataAccessException("Failed to check if student has applied to job", e);
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
    }
    
    /**
     * Get application count for a student (excludes soft-deleted)
     * 
     * @param studentId Student ID
     * @return Application count
     */
    public int getApplicationCountByStudent(Integer studentId) {
        // Validate input parameter before acquiring resources
        if (studentId == null) {
            return 0;
        }
        
        String sql = "SELECT COUNT(*) FROM applications WHERE student_id = ? AND deleted_at IS NULL";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to get application count for student ID: " + studentId, e);
            // Return 0 for backward compatibility instead of throwing exception
            return 0;
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
    }
    
    /**
     * Get application count for a job (excludes soft-deleted)
     * 
     * @param jobId Job ID
     * @return Application count
     */
    public int getApplicationCountByJob(Integer jobId) {
        // Validate input parameter before acquiring resources
        if (jobId == null) {
            return 0;
        }
        
        String sql = "SELECT COUNT(*) FROM applications WHERE job_id = ? AND deleted_at IS NULL";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, jobId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to get application count for job ID: " + jobId, e);
            // Return 0 for backward compatibility instead of throwing exception
            return 0;
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
    }
    
    /**
     * Get all applications for an admin's jobs (excludes soft-deleted)
     * 
     * @param adminId Admin ID
     * @return List of applications
     * @throws IllegalArgumentException if adminId is null
     */
    public List<Application> findByAdmin(Integer adminId) {
        // Validate parameter before acquiring resources
        if (adminId == null) {
            throw new IllegalArgumentException("adminId must not be null");
        }
        
        String sql = "SELECT a.* FROM applications a " +
                    "INNER JOIN jobs j ON a.job_id = j.job_id " +
                    "WHERE j.admin_id = ? AND a.deleted_at IS NULL ORDER BY a.applied_at DESC";
        
        List<Application> applications = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, adminId);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                applications.add(extractApplicationFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to find applications for admin ID: " + adminId, e);
            throw new DataAccessException("Failed to find applications for admin", e);
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return applications;
    }
    
    /**
     * Get recent applications (last N days)
     * 
     * @param days Number of days
     * @param limit Maximum results
     * @return List of applications
     * @throws IllegalArgumentException if days is negative or limit is non-positive or too large
     */
    public List<Application> getRecentApplications(int days, int limit) {
        // Input validation
        if (days < 0) {
            throw new IllegalArgumentException("Days cannot be negative: " + days);
        }
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be positive: " + limit);
        }
        // Cap limit to prevent excessive database load
        final int MAX_LIMIT = 10000;
        if (limit > MAX_LIMIT) {
            throw new IllegalArgumentException("Limit exceeds maximum allowed (" + MAX_LIMIT + "): " + limit);
        }
        
        // Compute cutoff timestamp in Java with overflow protection
        long currentTimeMillis = System.currentTimeMillis();
        long daysInMillis;
        try {
            // Guard against overflow: days * 24L * 60L * 60L * 1000L
            daysInMillis = Math.multiplyExact(days, 24L);
            daysInMillis = Math.multiplyExact(daysInMillis, 60L);
            daysInMillis = Math.multiplyExact(daysInMillis, 60L);
            daysInMillis = Math.multiplyExact(daysInMillis, 1000L);
        } catch (ArithmeticException e) {
            throw new IllegalArgumentException("Days value too large, would cause overflow: " + days, e);
        }
        
        // Check if subtraction would cause underflow
        if (currentTimeMillis < daysInMillis) {
            // Cutoff would be before epoch, just use epoch (Jan 1, 1970)
            currentTimeMillis = 0;
        } else {
            currentTimeMillis = currentTimeMillis - daysInMillis;
        }
        
        Timestamp cutoffTimestamp = new Timestamp(currentTimeMillis);
        
        // Use database-agnostic SQL with parameterized timestamp (exclude soft-deleted)
        String sql = "SELECT * FROM applications WHERE applied_at >= ? AND deleted_at IS NULL ORDER BY applied_at DESC LIMIT ?";
        
        List<Application> applications = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setTimestamp(1, cutoffTimestamp);
            pstmt.setInt(2, limit);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                applications.add(extractApplicationFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to get recent applications for last " + days + " days", e);
            throw new DataAccessException("Failed to get recent applications", e);
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return applications;
    }
    
    /**
     * Get application statistics by status for a student (excludes soft-deleted)
     * 
     * @param studentId Student ID
     * @return ApplicationStats object with counts by status
     * @throws IllegalArgumentException if studentId is null
     */
    public ApplicationStats getStatusStatsByStudent(Integer studentId) {
        // Validate input parameter before acquiring resources
        if (studentId == null) {
            throw new IllegalArgumentException("Student ID cannot be null");
        }
        
        String sql = "SELECT " +
                    "SUM(CASE WHEN application_status = ? THEN 1 ELSE 0 END) as pending, " +
                    "SUM(CASE WHEN application_status = ? THEN 1 ELSE 0 END) as reviewed, " +
                    "SUM(CASE WHEN application_status = ? THEN 1 ELSE 0 END) as shortlisted, " +
                    "SUM(CASE WHEN application_status = ? THEN 1 ELSE 0 END) as accepted, " +
                    "SUM(CASE WHEN application_status = ? THEN 1 ELSE 0 END) as rejected " +
                    "FROM applications WHERE student_id = ?";
        
        ApplicationStats stats = new ApplicationStats();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            // Set status parameters using enum constants
            pstmt.setString(1, ApplicationStatus.PENDING.getValue());
            pstmt.setString(2, ApplicationStatus.REVIEWED.getValue());
            pstmt.setString(3, ApplicationStatus.SHORTLISTED.getValue());
            pstmt.setString(4, ApplicationStatus.ACCEPTED.getValue());
            pstmt.setString(5, ApplicationStatus.REJECTED.getValue());
            pstmt.setInt(6, studentId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                stats.setPending(rs.getInt("pending"));
                stats.setReviewed(rs.getInt("reviewed"));
                stats.setShortlisted(rs.getInt("shortlisted"));
                stats.setAccepted(rs.getInt("accepted"));
                stats.setRejected(rs.getInt("rejected"));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to get application statistics for student ID: " + studentId, e);
            throw new DataAccessException("Failed to get application statistics for student", e);
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return stats;
    }
    
    /**
     * Extract Application object from ResultSet
     * 
     * @param rs ResultSet positioned at an application record
     * @return Application object
     */
    private Application extractApplicationFromResultSet(ResultSet rs) throws SQLException {
        Application application = new Application();
        
        application.setApplicationId(rs.getInt("application_id"));
        application.setJobId(rs.getInt("job_id"));
        application.setStudentId(rs.getInt("student_id"));
        application.setCoverLetter(rs.getString("cover_letter"));
        application.setApplicationStatus(rs.getString("application_status"));
        application.setAppliedAt(rs.getTimestamp("applied_at"));
        application.setReviewedAt(rs.getTimestamp("reviewed_at"));
        application.setReviewerNotes(rs.getString("reviewer_notes"));
        application.setDeletedAt(rs.getTimestamp("deleted_at"));
        
        return application;
    }
    
    // ========================================
    // ADDITIONAL METHODS FOR SERVLET COMPATIBILITY
    // ========================================
    
    /**
     * Find applications by admin ID with limit
     */
    public List<Application> findByAdmin(Integer adminId, int limit) {
        String sql = "SELECT a.* FROM applications a " +
                    "INNER JOIN jobs j ON a.job_id = j.job_id " +
                    "WHERE j.admin_id = ? AND a.deleted_at IS NULL " +
                    "ORDER BY a.applied_at DESC LIMIT ?";
        List<Application> applications = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, adminId);
            pstmt.setInt(2, limit);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                applications.add(extractApplicationFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return applications;
    }
    
    /**
     * Get application count by admin ID
     */
    public Integer getApplicationCountByAdmin(Integer adminId) {
        String sql = "SELECT COUNT(*) FROM applications a " +
                    "INNER JOIN jobs j ON a.job_id = j.job_id " +
                    "WHERE j.admin_id = ? AND a.deleted_at IS NULL";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, adminId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    /**
     * Get application count by admin ID and status
     */
    public Integer getApplicationCountByAdminAndStatus(Integer adminId, String status) {
        String sql = "SELECT COUNT(*) FROM applications a " +
                    "INNER JOIN jobs j ON a.job_id = j.job_id " +
                    "WHERE j.admin_id = ? AND a.application_status = ? AND a.deleted_at IS NULL";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, adminId);
            pstmt.setString(2, status);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

