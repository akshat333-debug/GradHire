package com.gradhire.dao;

import com.gradhire.model.Job;
import com.gradhire.model.Student;
import com.gradhire.util.DBConnection;
import com.gradhire.util.RecommendationEngine;
import com.gradhire.util.RecommendationEngine.JobRecommendation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Job Data Access Object
 * Handles all database operations for Job entity
 * 
 * @author GradHire Team
 * @version 1.0
 */
public class JobDAO {
    
    /**
     * Create a new job listing
     * 
     * @param job Job object to insert
     * @return Generated job ID, or null if failed
     */
    public Integer createJob(Job job) {
        String sql = "INSERT INTO jobs (admin_id, job_title, company_name, job_type, domain, " +
                    "description, requirements, responsibilities, location, is_remote, " +
                    "salary_min, salary_max, application_deadline, job_status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setInt(1, job.getAdminId());
            pstmt.setString(2, job.getJobTitle());
            pstmt.setString(3, job.getCompanyName());
            pstmt.setString(4, job.getJobType());
            pstmt.setString(5, job.getDomain());
            pstmt.setString(6, job.getDescription());
            pstmt.setString(7, job.getRequirements());
            pstmt.setString(8, job.getResponsibilities());
            pstmt.setString(9, job.getLocation());
            pstmt.setBoolean(10, job.getIsRemote() != null && job.getIsRemote());
            pstmt.setBigDecimal(11, job.getSalaryMin());
            pstmt.setBigDecimal(12, job.getSalaryMax());
            pstmt.setDate(13, job.getApplicationDeadline());
            pstmt.setString(14, job.getJobStatus());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return null;
    }
    
    /**
     * Find job by ID
     * 
     * @param jobId Job ID
     * @return Job object or null if not found
     */
    public Job findById(Integer jobId) {
        String sql = "SELECT * FROM jobs WHERE job_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, jobId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractJobFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return null;
    }
    
    /**
     * Update job listing
     * 
     * @param job Job object with updated data
     * @return true if successful, false otherwise
     */
    public boolean updateJob(Job job) {
        String sql = "UPDATE jobs SET job_title = ?, job_type = ?, domain = ?, " +
                    "description = ?, requirements = ?, responsibilities = ?, " +
                    "location = ?, is_remote = ?, salary_min = ?, salary_max = ?, " +
                    "application_deadline = ?, job_status = ? WHERE job_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, job.getJobTitle());
            pstmt.setString(2, job.getJobType());
            pstmt.setString(3, job.getDomain());
            pstmt.setString(4, job.getDescription());
            pstmt.setString(5, job.getRequirements());
            pstmt.setString(6, job.getResponsibilities());
            pstmt.setString(7, job.getLocation());
            pstmt.setBoolean(8, job.getIsRemote() != null && job.getIsRemote());
            pstmt.setBigDecimal(9, job.getSalaryMin());
            pstmt.setBigDecimal(10, job.getSalaryMax());
            pstmt.setDate(11, job.getApplicationDeadline());
            pstmt.setString(12, job.getJobStatus());
            pstmt.setInt(13, job.getJobId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(pstmt, conn);
        }
        
        return false;
    }
    
    /**
     * Update job status
     * 
     * @param jobId Job ID
     * @param status New status
     * @return true if successful
     */
    public boolean updateJobStatus(Integer jobId, String status) {
        String sql = "UPDATE jobs SET job_status = ? WHERE job_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, status);
            pstmt.setInt(2, jobId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(pstmt, conn);
        }
        
        return false;
    }
    
    /**
     * Increment job views count
     * 
     * @param jobId Job ID
     * @return true if successful
     */
    public boolean incrementViews(Integer jobId) {
        String sql = "UPDATE jobs SET views_count = views_count + 1 WHERE job_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, jobId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(pstmt, conn);
        }
        
        return false;
    }
    
    /**
     * Delete job listing
     * 
     * @param jobId Job ID
     * @return true if successful
     */
    public boolean deleteJob(Integer jobId) {
        String sql = "DELETE FROM jobs WHERE job_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, jobId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(pstmt, conn);
        }
        
        return false;
    }
    
    /**
     * Get all active jobs with pagination
     * 
     * @param limit Maximum results
     * @param offset Offset for pagination
     * @return List of jobs
     */
    public List<Job> findActiveJobs(int limit, int offset) {
        String sql = "SELECT * FROM jobs WHERE job_status = 'Active' " +
                    "AND application_deadline >= CURDATE() " +
                    "ORDER BY created_at DESC LIMIT ? OFFSET ?";
        
        List<Job> jobs = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, limit);
            pstmt.setInt(2, offset);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                jobs.add(extractJobFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return jobs;
    }
    
    /**
     * Get jobs by admin/recruiter
     * 
     * @param adminId Admin ID
     * @return List of jobs
     */
    public List<Job> findByAdmin(Integer adminId) {
        String sql = "SELECT * FROM jobs WHERE admin_id = ? ORDER BY created_at DESC";
        
        List<Job> jobs = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, adminId);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                jobs.add(extractJobFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return jobs;
    }
    
    /**
     * Find jobs by admin ID with limit
     * 
     * @param adminId Admin ID
     * @param limit Maximum number of jobs to return
     * @return List of jobs
     */
    public List<Job> findByAdmin(Integer adminId, int limit) {
        String sql = "SELECT * FROM jobs WHERE admin_id = ? ORDER BY created_at DESC LIMIT ?";
        List<Job> jobs = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, adminId);
            pstmt.setInt(2, limit);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                jobs.add(extractJobFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jobs;
    }
    
    /**
     * Search jobs by keyword (title, company, domain, description)
     * 
     * @param keyword Search keyword
     * @return List of matching jobs
     */
    public List<Job> searchJobs(String keyword) {
        String sql = "SELECT * FROM jobs WHERE job_status = 'Active' " +
                    "AND (job_title LIKE ? OR company_name LIKE ? OR domain LIKE ? OR description LIKE ?) " +
                    "ORDER BY created_at DESC LIMIT 50";
        
        List<Job> jobs = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                jobs.add(extractJobFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return jobs;
    }
    
    /**
     * Filter jobs by multiple criteria
     * 
     * @param jobType Job type filter (null for all)
     * @param domain Domain filter (null for all)
     * @param location Location filter (null for all)
     * @param isRemote Remote only (null for all)
     * @param limit Maximum results
     * @param offset Offset for pagination
     * @return List of jobs
     */
    public List<Job> filterJobs(String jobType, String domain, String location, Boolean isRemote, int limit, int offset) {
        StringBuilder sql = new StringBuilder("SELECT * FROM jobs WHERE job_status = 'Active'");
        
        if (jobType != null && !jobType.isEmpty()) {
            sql.append(" AND job_type = '").append(jobType).append("'");
        }
        if (domain != null && !domain.isEmpty()) {
            sql.append(" AND domain = '").append(domain).append("'");
        }
        if (location != null && !location.isEmpty()) {
            sql.append(" AND location LIKE '%").append(location).append("%'");
        }
        if (isRemote != null) {
            sql.append(" AND is_remote = ").append(isRemote ? "TRUE" : "FALSE");
        }
        
        sql.append(" ORDER BY created_at DESC LIMIT ").append(limit).append(" OFFSET ").append(offset);
        
        List<Job> jobs = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql.toString());
            
            while (rs.next()) {
                jobs.add(extractJobFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
        
        return jobs;
    }
    
    /**
     * Get job recommendations for a student (using stored procedure)
     * 
     * @param studentId Student ID
     * @return List of recommended jobs
     */
    public List<Job> getRecommendations(Integer studentId) {
        String sql = "CALL sp_get_job_recommendations(?)";
        
        List<Job> jobs = new ArrayList<>();
        Connection conn = null;
        CallableStatement cstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            cstmt = conn.prepareCall(sql);
            cstmt.setInt(1, studentId);
            
            rs = cstmt.executeQuery();
            
            while (rs.next()) {
                Job job = extractJobFromResultSet(rs);
                // Set match percentage from stored procedure result
                try {
                    job.setMatchPercentage(rs.getInt("match_percentage"));
                } catch (SQLException e) {
                    // match_percentage might not be in result
                }
                jobs.add(job);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(rs, cstmt, conn);
        }
        
        return jobs;
    }
    
    /**
     * Get total job count (all statuses)
     * 
     * @return Total job count
     */
    public int getJobCount() {
        String sql = "SELECT COUNT(*) FROM jobs";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
        
        return 0;
    }
    
    /**
     * Get active job count
     * 
     * @return Active job count
     */
    public int getActiveJobCount() {
        String sql = "SELECT COUNT(*) FROM jobs WHERE job_status = 'Active'";
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
        
        return 0;
    }
    
    /**
     * Get jobs by type
     * 
     * @param jobType Job type ('Internship', 'Full-time', etc.)
     * @return List of jobs
     */
    public List<Job> findByType(String jobType) {
        String sql = "SELECT * FROM jobs WHERE job_type = ? AND job_status = 'Active' " +
                    "ORDER BY created_at DESC";
        
        List<Job> jobs = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, jobType);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                jobs.add(extractJobFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return jobs;
    }
    
    /**
     * Get jobs by domain
     * 
     * @param domain Domain/category
     * @return List of jobs
     */
    public List<Job> findByDomain(String domain) {
        String sql = "SELECT * FROM jobs WHERE domain = ? AND job_status = 'Active' " +
                    "ORDER BY created_at DESC";
        
        List<Job> jobs = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, domain);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                jobs.add(extractJobFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return jobs;
    }
    
    /**
     * Get personalized job recommendations for a student based on skill matching.
     * 
     * This method uses the RecommendationEngine to rank jobs by how well they match
     * the student's skills using Jaccard similarity and keyword matching algorithms.
     * 
     * @param student The student to get recommendations for
     * @param limit Maximum number of recommendations to return
     * @return List of JobRecommendation objects sorted by score (best matches first)
     */
    public List<JobRecommendation> getRecommendedJobs(Student student, int limit) {
        if (student == null) {
            return new ArrayList<>();
        }
        
        // Get all active jobs (set high limit to get all)
        List<Job> activeJobs = findActiveJobs(10000, 0);
        
        if (activeJobs.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Load skills for each job
        SkillDAO skillDAO = new SkillDAO();
        for (Job job : activeJobs) {
            job.setRequiredSkills(skillDAO.getJobSkills(job.getJobId()));
        }
        
        // Use recommendation engine to rank jobs
        return RecommendationEngine.getTopRecommendations(student, activeJobs, limit);
    }
    
    /**
     * Get recommended jobs excluding those the student has already applied to.
     * 
     * @param student The student to get recommendations for
     * @param limit Maximum number of recommendations to return
     * @return List of JobRecommendation objects for jobs not yet applied to
     */
    public List<JobRecommendation> getRecommendedJobsNotApplied(Student student, int limit) {
        if (student == null) {
            return new ArrayList<>();
        }
        
        // Get all active jobs (set high limit to get all)
        List<Job> activeJobs = findActiveJobs(10000, 0);
        
        if (activeJobs.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Filter out jobs already applied to
        ApplicationDAO applicationDAO = new ApplicationDAO();
        List<Job> notAppliedJobs = new ArrayList<>();
        
        SkillDAO skillDAO = new SkillDAO();
        for (Job job : activeJobs) {
            // Check if student has already applied
            if (!applicationDAO.hasApplied(student.getStudentId(), job.getJobId())) {
                // Load required skills for this job
                job.setRequiredSkills(skillDAO.getJobSkills(job.getJobId()));
                notAppliedJobs.add(job);
            }
        }
        
        // Use recommendation engine to rank jobs
        return RecommendationEngine.getTopRecommendations(student, notAppliedJobs, limit);
    }
    
    
    /**
     * Extract Job object from ResultSet
     * 
     * @param rs ResultSet positioned at a job record
     * @return Job object
     */
    private Job extractJobFromResultSet(ResultSet rs) throws SQLException {
        Job job = new Job();
        
        job.setJobId(rs.getInt("job_id"));
        job.setAdminId(rs.getInt("admin_id"));
        job.setJobTitle(rs.getString("job_title"));
        job.setCompanyName(rs.getString("company_name"));
        job.setJobType(rs.getString("job_type"));
        job.setDomain(rs.getString("domain"));
        job.setDescription(rs.getString("description"));
        job.setRequirements(rs.getString("requirements"));
        job.setResponsibilities(rs.getString("responsibilities"));
        job.setLocation(rs.getString("location"));
        job.setIsRemote(rs.getBoolean("is_remote"));
        job.setSalaryMin(rs.getBigDecimal("salary_min"));
        job.setSalaryMax(rs.getBigDecimal("salary_max"));
        job.setApplicationDeadline(rs.getDate("application_deadline"));
        job.setJobStatus(rs.getString("job_status"));
        job.setViewsCount(rs.getInt("views_count"));
        job.setCreatedAt(rs.getTimestamp("created_at"));
        job.setUpdatedAt(rs.getTimestamp("updated_at"));
        
        return job;
    }
    
    // ========================================
    // ADDITIONAL METHODS FOR SERVLET COMPATIBILITY
    // ========================================
    
    /**
     * Alias for createJob
     */
    public Integer create(Job job) {
        return createJob(job);
    }
    
    /**
     * Count jobs by admin ID
     */
    public Integer countByAdmin(Integer adminId) {
        String sql = "SELECT COUNT(*) FROM jobs WHERE admin_id = ?";
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
     * Alias for findActiveJobs
     */
    public List<Job> findAllActive(int limit, int offset) {
        return findActiveJobs(limit, offset);
    }
    
    /**
     * Count active jobs
     */
    public Integer countActive() {
        String sql = "SELECT COUNT(*) FROM jobs WHERE job_status = 'Active'";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
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
     * Find similar jobs
     */
    public List<Job> findSimilar(String company, String jobType, int limit, Integer excludeId) {
        String sql = "SELECT * FROM jobs WHERE job_status = 'Active' " +
                    "AND (company_name = ? OR job_type = ?) " +
                    "AND job_id != ? LIMIT ?";
        List<Job> jobs = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, company);
            pstmt.setString(2, jobType);
            pstmt.setInt(3, excludeId);
            pstmt.setInt(4, limit);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                jobs.add(extractJobFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jobs;
    }
}

