package com.gradhire.dao;

import com.gradhire.exception.DataAccessException;
import com.gradhire.model.Admin;
import com.gradhire.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Admin Data Access Object
 * Handles all database operations for Admin/Recruiter entity
 * 
 * @author GradHire Team
 * @version 1.0
 */
public class AdminDAO {
    
    private static final Logger logger = Logger.getLogger(AdminDAO.class.getName());
    
    /**
     * Create a new admin/recruiter account
     * 
     * @param admin Admin object to insert
     * @return Generated admin ID, or null if failed
     * @throws IllegalArgumentException if admin is null or required fields are null
     * @throws DataAccessException if database operation fails
     */
    public Integer createAdmin(Admin admin) {
        // Validate input before acquiring resources
        if (admin == null || admin.getEmail() == null || admin.getPasswordHash() == null || admin.getFullName() == null) {
            throw new IllegalArgumentException("Admin and required fields (email, passwordHash, fullName) cannot be null");
        }
        
        String sql = "INSERT INTO admins (email, password_hash, full_name, company_name, " +
                    "company_website, phone, role) VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setString(1, admin.getEmail());
            pstmt.setString(2, admin.getPasswordHash());
            pstmt.setString(3, admin.getFullName());
            pstmt.setString(4, admin.getCompanyName());
            pstmt.setString(5, admin.getCompanyWebsite());
            pstmt.setString(6, admin.getPhone());
            pstmt.setString(7, admin.getRole());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    Integer generatedId = rs.getInt(1);
                    logger.log(Level.INFO, "Successfully created admin with ID: {0}", generatedId);
                    return generatedId;
                }
            }
            logger.log(Level.WARNING, "Admin creation returned 0 affected rows");
            return null;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to create admin", e);
            throw new DataAccessException("Failed to create admin", e);
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
    }
    
    /**
     * Find admin by ID
     * 
     * @param adminId Admin ID
     * @return Admin object or null if not found
     * @throws IllegalArgumentException if adminId is null
     * @throws DataAccessException if database operation fails
     */
    public Admin findById(Integer adminId) {
        // Validate input before acquiring resources
        if (adminId == null) {
            throw new IllegalArgumentException("Admin ID cannot be null");
        }
        
        String sql = "SELECT * FROM admins WHERE admin_id = ? AND is_active = TRUE";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, adminId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractAdminFromResultSet(rs);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to find admin by ID: " + adminId, e);
            throw new DataAccessException("Failed to find admin by ID", e);
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return null;
    }
    
    /**
     * Find admin by email
     * 
     * @param email Admin email
     * @return Admin object or null if not found
     * @throws IllegalArgumentException if email is null or empty
     * @throws DataAccessException if database operation fails
     */
    public Admin findByEmail(String email) {
        // Validate input before acquiring resources
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        
        String sql = "SELECT * FROM admins WHERE email = ? AND is_active = TRUE";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractAdminFromResultSet(rs);
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to find admin by email: " + email, e);
            throw new DataAccessException("Failed to find admin by email", e);
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return null;
    }
    
    /**
     * Update admin profile
     * 
     * @param admin Admin object with updated data
     * @return true if successful, false otherwise
     * @throws IllegalArgumentException if admin is null or adminId is null
     * @throws DataAccessException if database operation fails
     */
    public boolean updateAdmin(Admin admin) {
        // Validate input before acquiring resources
        if (admin == null || admin.getAdminId() == null) {
            throw new IllegalArgumentException("Admin and Admin ID cannot be null");
        }
        
        String sql = "UPDATE admins SET full_name = ?, company_name = ?, " +
                    "company_website = ?, phone = ? WHERE admin_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, admin.getFullName());
            pstmt.setString(2, admin.getCompanyName());
            pstmt.setString(3, admin.getCompanyWebsite());
            pstmt.setString(4, admin.getPhone());
            pstmt.setInt(5, admin.getAdminId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to update admin with ID: " + admin.getAdminId(), e);
            throw new DataAccessException("Failed to update admin", e);
        } finally {
            DBConnection.closeResources(pstmt, conn);
        }
    }
    
    /**
     * Update admin password
     * 
     * @param adminId Admin ID
     * @param newPasswordHash New password hash
     * @return true if successful, false otherwise
     * @throws IllegalArgumentException if adminId or newPasswordHash is null
     * @throws DataAccessException if database operation fails
     */
    public boolean updatePassword(Integer adminId, String newPasswordHash) {
        // Validate input before acquiring resources
        if (adminId == null || newPasswordHash == null) {
            throw new IllegalArgumentException("Admin ID and new password hash cannot be null");
        }
        
        String sql = "UPDATE admins SET password_hash = ? WHERE admin_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newPasswordHash);
            pstmt.setInt(2, adminId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to update password for admin ID: " + adminId, e);
            throw new DataAccessException("Failed to update password", e);
        } finally {
            DBConnection.closeResources(pstmt, conn);
        }
    }
    
    /**
     * Soft delete admin account (deactivate)
     * 
     * @param adminId Admin ID
     * @return true if successful, false otherwise
     * @throws IllegalArgumentException if adminId is null
     * @throws DataAccessException if database operation fails
     */
    public boolean deleteAdmin(Integer adminId) {
        // Validate input before acquiring resources
        if (adminId == null) {
            throw new IllegalArgumentException("Admin ID cannot be null");
        }
        
        String sql = "UPDATE admins SET is_active = FALSE WHERE admin_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, adminId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to deactivate admin with ID: " + adminId, e);
            throw new DataAccessException("Failed to deactivate admin", e);
        } finally {
            DBConnection.closeResources(pstmt, conn);
        }
    }
    
    /**
     * Get all admins/recruiters
     * 
     * @return List of admins
     * @throws DataAccessException if database operation fails
     */
    public List<Admin> findAll() {
        String sql = "SELECT * FROM admins WHERE is_active = TRUE ORDER BY created_at DESC";
        
        List<Admin> admins = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                admins.add(extractAdminFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to retrieve all admins", e);
            throw new DataAccessException("Failed to retrieve all admins", e);
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
        
        return admins;
    }
    
    /**
     * Get admins by role
     * 
     * @param role Role ('admin' or 'recruiter')
     * @return List of admins
     * @throws IllegalArgumentException if role is null or empty
     * @throws DataAccessException if database operation fails
     */
    public List<Admin> findByRole(String role) {
        // Validate input before acquiring resources
        if (role == null || role.trim().isEmpty()) {
            throw new IllegalArgumentException("Role cannot be null or empty");
        }
        
        String sql = "SELECT * FROM admins WHERE role = ? AND is_active = TRUE " +
                    "ORDER BY full_name";
        
        List<Admin> admins = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, role);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                admins.add(extractAdminFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to find admins by role: " + role, e);
            throw new DataAccessException("Failed to find admins by role", e);
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return admins;
    }
    
    /**
     * Get admins by company
     * 
     * @param companyName Company name
     * @return List of admins
     * @throws IllegalArgumentException if companyName is null or empty
     * @throws DataAccessException if database operation fails
     */
    public List<Admin> findByCompany(String companyName) {
        // Validate input before acquiring resources
        if (companyName == null || companyName.trim().isEmpty()) {
            throw new IllegalArgumentException("Company name cannot be null or empty");
        }
        
        String sql = "SELECT * FROM admins WHERE company_name = ? AND is_active = TRUE " +
                    "ORDER BY full_name";
        
        List<Admin> admins = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, companyName);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                admins.add(extractAdminFromResultSet(rs));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to find admins by company: " + companyName, e);
            throw new DataAccessException("Failed to find admins by company", e);
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return admins;
    }
    
    /**
     * Check if email exists (optimized with EXISTS)
     * 
     * @param email Email to check
     * @return true if exists, false otherwise
     * @throws IllegalArgumentException if email is null or empty
     * @throws DataAccessException if database operation fails
     */
    public boolean emailExists(String email) {
        // Validate input before acquiring resources
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        
        String sql = "SELECT EXISTS(SELECT 1 FROM admins WHERE email = ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getBoolean(1);
            }
            return false;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to check if email exists: " + email, e);
            throw new DataAccessException("Failed to check if email exists", e);
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
    }
    
    /**
     * Get total admin count
     * 
     * @return Number of active admins
     */
    public int getAdminCount() {
        String sql = "SELECT COUNT(*) FROM admins WHERE is_active = TRUE";
        
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
            return 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to get admin count", e);
            // Return 0 for backward compatibility instead of throwing exception
            return 0;
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
    }
    
    /**
     * Extract Admin object from ResultSet
     * 
     * @param rs ResultSet positioned at an admin record
     * @return Admin object
     */
    private Admin extractAdminFromResultSet(ResultSet rs) throws SQLException {
        Admin admin = new Admin();
        
        admin.setAdminId(rs.getInt("admin_id"));
        admin.setEmail(rs.getString("email"));
        admin.setPasswordHash(rs.getString("password_hash"));
        admin.setFullName(rs.getString("full_name"));
        admin.setCompanyName(rs.getString("company_name"));
        admin.setCompanyWebsite(rs.getString("company_website"));
        admin.setPhone(rs.getString("phone"));
        admin.setRole(rs.getString("role"));
        admin.setCreatedAt(rs.getTimestamp("created_at"));
        admin.setUpdatedAt(rs.getTimestamp("updated_at"));
        admin.setIsActive(rs.getBoolean("is_active"));
        
        return admin;
    }
    
    // ========================================
    // ADDITIONAL METHODS FOR SERVLET COMPATIBILITY
    // ========================================
    
    /**
     * Alias for createAdmin
     */
    public Integer create(Admin admin) {
        return createAdmin(admin);
    }
}

