package com.gradhire.dao;

import com.gradhire.model.Student;
import com.gradhire.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Student Data Access Object
 * Handles all database operations for Student entity
 * 
 * @author GradHire Team
 * @version 1.0
 */
public class StudentDAO {
    
    /**
     * Create a new student account
     * 
     * @param student Student object to insert
     * @return Generated student ID, or null if failed
     */
    public Integer createStudent(Student student) {
        String sql = "INSERT INTO students (email, password_hash, full_name, phone, " +
                    "college_name, degree, graduation_year, bio, linkedin_url, github_url) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setString(1, student.getEmail());
            pstmt.setString(2, student.getPasswordHash());
            pstmt.setString(3, student.getFullName());
            pstmt.setString(4, student.getPhone());
            pstmt.setString(5, student.getCollegeName());
            pstmt.setString(6, student.getDegree());
            
            if (student.getGraduationYear() != null) {
                pstmt.setInt(7, student.getGraduationYear());
            } else {
                pstmt.setNull(7, Types.INTEGER);
            }
            
            pstmt.setString(8, student.getBio());
            pstmt.setString(9, student.getLinkedinUrl());
            pstmt.setString(10, student.getGithubUrl());
            
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
     * Find student by ID
     * 
     * @param studentId Student ID
     * @return Student object or null if not found
     */
    public Student findById(Integer studentId) {
        String sql = "SELECT * FROM students WHERE student_id = ? AND is_active = TRUE";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractStudentFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return null;
    }
    
    /**
     * Find student by email
     * 
     * @param email Student email
     * @return Student object or null if not found
     */
    public Student findByEmail(String email) {
        String sql = "SELECT * FROM students WHERE email = ? AND is_active = TRUE";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractStudentFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return null;
    }
    
    /**
     * Update student profile
     * 
     * @param student Student object with updated data
     * @return true if successful, false otherwise
     */
    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET full_name = ?, phone = ?, college_name = ?, " +
                    "degree = ?, graduation_year = ?, bio = ?, linkedin_url = ?, " +
                    "github_url = ?, profile_picture = ?, resume_path = ? WHERE student_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, student.getFullName());
            pstmt.setString(2, student.getPhone());
            pstmt.setString(3, student.getCollegeName());
            pstmt.setString(4, student.getDegree());
            
            if (student.getGraduationYear() != null) {
                pstmt.setInt(5, student.getGraduationYear());
            } else {
                pstmt.setNull(5, Types.INTEGER);
            }
            
            pstmt.setString(6, student.getBio());
            pstmt.setString(7, student.getLinkedinUrl());
            pstmt.setString(8, student.getGithubUrl());
            pstmt.setString(9, student.getProfilePicture());
            pstmt.setString(10, student.getResumePath());
            pstmt.setInt(11, student.getStudentId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(pstmt, conn);
        }
        
        return false;
    }
    
    /**
     * Update student resume path
     * 
     * @param studentId Student ID
     * @param resumePath Path to resume file
     * @return true if successful, false otherwise
     */
    public boolean updateResumePath(Integer studentId, String resumePath) {
        String sql = "UPDATE students SET resume_path = ? WHERE student_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, resumePath);
            pstmt.setInt(2, studentId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(pstmt, conn);
        }
        
        return false;
    }
    
    /**
     * Update student password
     * 
     * @param studentId Student ID
     * @param newPasswordHash New password hash
     * @return true if successful, false otherwise
     */
    public boolean updatePassword(Integer studentId, String newPasswordHash) {
        String sql = "UPDATE students SET password_hash = ? WHERE student_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newPasswordHash);
            pstmt.setInt(2, studentId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(pstmt, conn);
        }
        
        return false;
    }
    
    /**
     * Soft delete student account
     * 
     * @param studentId Student ID
     * @return true if successful, false otherwise
     */
    public boolean deleteStudent(Integer studentId) {
        String sql = "UPDATE students SET is_active = FALSE WHERE student_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(pstmt, conn);
        }
        
        return false;
    }
    
    /**
     * Get all students (with optional filters)
     * 
     * @param limit Maximum number of results
     * @param offset Offset for pagination
     * @return List of students
     */
    public List<Student> findAll(int limit, int offset) {
        String sql = "SELECT * FROM students WHERE is_active = TRUE " +
                    "ORDER BY created_at DESC LIMIT ? OFFSET ?";
        
        List<Student> students = new ArrayList<>();
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
                students.add(extractStudentFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return students;
    }
    
    /**
     * Search students by name, email, or college
     * 
     * @param keyword Search keyword
     * @return List of matching students
     */
    public List<Student> search(String keyword) {
        String sql = "SELECT * FROM students WHERE is_active = TRUE AND " +
                    "(full_name LIKE ? OR email LIKE ? OR college_name LIKE ?) " +
                    "ORDER BY full_name LIMIT 50";
        
        List<Student> students = new ArrayList<>();
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
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                students.add(extractStudentFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return students;
    }
    
    /**
     * Get student count
     * 
     * @return Total number of active students
     */
    public int getStudentCount() {
        String sql = "SELECT COUNT(*) FROM students WHERE is_active = TRUE";
        
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
     * Check if email exists
     * 
     * @param email Email to check
     * @return true if exists, false otherwise
     */
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM students WHERE email = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return false;
    }
    
    /**
     * Get students by graduation year
     * 
     * @param year Graduation year
     * @return List of students
     */
    public List<Student> findByGraduationYear(int year) {
        String sql = "SELECT * FROM students WHERE graduation_year = ? AND is_active = TRUE " +
                    "ORDER BY full_name";
        
        List<Student> students = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, year);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                students.add(extractStudentFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return students;
    }
    
    /**
     * Extract Student object from ResultSet
     * 
     * @param rs ResultSet positioned at a student record
     * @return Student object
     */
    private Student extractStudentFromResultSet(ResultSet rs) throws SQLException {
        Student student = new Student();
        
        student.setStudentId(rs.getInt("student_id"));
        student.setEmail(rs.getString("email"));
        student.setPasswordHash(rs.getString("password_hash"));
        student.setFullName(rs.getString("full_name"));
        student.setPhone(rs.getString("phone"));
        student.setCollegeName(rs.getString("college_name"));
        student.setDegree(rs.getString("degree"));
        
        int gradYear = rs.getInt("graduation_year");
        if (!rs.wasNull()) {
            student.setGraduationYear(gradYear);
        }
        
        student.setResumePath(rs.getString("resume_path"));
        student.setProfilePicture(rs.getString("profile_picture"));
        student.setBio(rs.getString("bio"));
        student.setLinkedinUrl(rs.getString("linkedin_url"));
        student.setGithubUrl(rs.getString("github_url"));
        student.setCreatedAt(rs.getTimestamp("created_at"));
        student.setUpdatedAt(rs.getTimestamp("updated_at"));
        student.setIsActive(rs.getBoolean("is_active"));
        
        return student;
    }
    
    // ========================================
    // ADDITIONAL METHODS FOR SERVLET COMPATIBILITY
    // ========================================
    
    /**
     * Alias for createStudent
     */
    public Integer create(Student student) {
        return createStudent(student);
    }
    
    /**
     * Alias for updateStudent
     */
    public boolean update(Student student) {
        return updateStudent(student);
    }
}

