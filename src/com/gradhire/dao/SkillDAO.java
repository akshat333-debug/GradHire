package com.gradhire.dao;

import com.gradhire.model.Skill;
import com.gradhire.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Skill Data Access Object
 * Handles all database operations for Skill entity and skill relationships
 * 
 * @author GradHire Team
 * @version 1.0
 */
public class SkillDAO {
    
    /**
     * Get all skills
     * 
     * @return List of all skills
     */
    public List<Skill> findAll() {
        String sql = "SELECT * FROM skills ORDER BY skill_name";
        
        List<Skill> skills = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                skills.add(extractSkillFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
        
        return skills;
    }
    
    /**
     * Find skill by ID
     * 
     * @param skillId Skill ID
     * @return Skill object or null
     */
    public Skill findById(Integer skillId) {
        String sql = "SELECT * FROM skills WHERE skill_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, skillId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractSkillFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return null;
    }
    
    /**
     * Find skill by name
     * 
     * @param skillName Skill name
     * @return Skill object or null
     */
    public Skill findByName(String skillName) {
        String sql = "SELECT * FROM skills WHERE skill_name = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, skillName);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return extractSkillFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return null;
    }
    
    /**
     * Get skills by category
     * 
     * @param category Category name
     * @return List of skills
     */
    public List<Skill> findByCategory(String category) {
        String sql = "SELECT * FROM skills WHERE category = ? ORDER BY skill_name";
        
        List<Skill> skills = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, category);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                skills.add(extractSkillFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return skills;
    }
    
    /**
     * Get all unique categories
     * 
     * @return List of category names
     */
    public List<String> getAllCategories() {
        String sql = "SELECT DISTINCT category FROM skills WHERE category IS NOT NULL ORDER BY category";
        
        List<String> categories = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(rs, stmt, conn);
        }
        
        return categories;
    }
    
    /**
     * Get skills for a student (with proficiency levels)
     * 
     * @param studentId Student ID
     * @return List of skills with proficiency
     */
    public List<Skill> getStudentSkills(Integer studentId) {
        String sql = "SELECT s.*, ss.proficiency_level FROM skills s " +
                    "INNER JOIN student_skills ss ON s.skill_id = ss.skill_id " +
                    "WHERE ss.student_id = ? ORDER BY s.skill_name";
        
        List<Skill> skills = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentId);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Skill skill = extractSkillFromResultSet(rs);
                skill.setProficiencyLevel(rs.getString("proficiency_level"));
                skills.add(skill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return skills;
    }
    
    /**
     * Add skill to student
     * 
     * @param studentId Student ID
     * @param skillId Skill ID
     * @param proficiencyLevel Proficiency level
     * @return true if successful
     */
    public boolean addStudentSkill(Integer studentId, Integer skillId, String proficiencyLevel) {
        String sql = "INSERT INTO student_skills (student_id, skill_id, proficiency_level) VALUES (?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, skillId);
            pstmt.setString(3, proficiencyLevel);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(pstmt, conn);
        }
        
        return false;
    }
    
    /**
     * Update student skill proficiency
     * 
     * @param studentId Student ID
     * @param skillId Skill ID
     * @param proficiencyLevel New proficiency level
     * @return true if successful
     */
    public boolean updateStudentSkill(Integer studentId, Integer skillId, String proficiencyLevel) {
        String sql = "UPDATE student_skills SET proficiency_level = ? WHERE student_id = ? AND skill_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, proficiencyLevel);
            pstmt.setInt(2, studentId);
            pstmt.setInt(3, skillId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(pstmt, conn);
        }
        
        return false;
    }
    
    /**
     * Remove skill from student
     * 
     * @param studentId Student ID
     * @param skillId Skill ID
     * @return true if successful
     */
    public boolean removeStudentSkill(Integer studentId, Integer skillId) {
        String sql = "DELETE FROM student_skills WHERE student_id = ? AND skill_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, studentId);
            pstmt.setInt(2, skillId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(pstmt, conn);
        }
        
        return false;
    }
    
    /**
     * Get skills for a job (with required/optional flag)
     * 
     * @param jobId Job ID
     * @return List of skills with isRequired flag
     */
    public List<Skill> getJobSkills(Integer jobId) {
        String sql = "SELECT s.*, js.is_required FROM skills s " +
                    "INNER JOIN job_skills js ON s.skill_id = js.skill_id " +
                    "WHERE js.job_id = ? ORDER BY js.is_required DESC, s.skill_name";
        
        List<Skill> skills = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, jobId);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Skill skill = extractSkillFromResultSet(rs);
                skill.setIsRequired(rs.getBoolean("is_required"));
                skills.add(skill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return skills;
    }
    
    /**
     * Get required skills for a job
     * 
     * @param jobId Job ID
     * @return List of required skills
     */
    public List<Skill> getRequiredJobSkills(Integer jobId) {
        String sql = "SELECT s.* FROM skills s " +
                    "INNER JOIN job_skills js ON s.skill_id = js.skill_id " +
                    "WHERE js.job_id = ? AND js.is_required = TRUE ORDER BY s.skill_name";
        
        List<Skill> skills = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, jobId);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                skills.add(extractSkillFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return skills;
    }
    
    /**
     * Add skill to job
     * 
     * @param jobId Job ID
     * @param skillId Skill ID
     * @param isRequired Required or optional
     * @return true if successful
     */
    public boolean addJobSkill(Integer jobId, Integer skillId, boolean isRequired) {
        String sql = "INSERT INTO job_skills (job_id, skill_id, is_required) VALUES (?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, jobId);
            pstmt.setInt(2, skillId);
            pstmt.setBoolean(3, isRequired);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(pstmt, conn);
        }
        
        return false;
    }
    
    /**
     * Remove skill from job
     * 
     * @param jobId Job ID
     * @param skillId Skill ID
     * @return true if successful
     */
    public boolean removeJobSkill(Integer jobId, Integer skillId) {
        String sql = "DELETE FROM job_skills WHERE job_id = ? AND skill_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, jobId);
            pstmt.setInt(2, skillId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(pstmt, conn);
        }
        
        return false;
    }
    
    /**
     * Clear all skills for a job
     * 
     * @param jobId Job ID
     * @return true if successful
     */
    public boolean clearJobSkills(Integer jobId) {
        String sql = "DELETE FROM job_skills WHERE job_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, jobId);
            
            return pstmt.executeUpdate() >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(pstmt, conn);
        }
        
        return false;
    }
    
    /**
     * Search skills by name
     * 
     * @param keyword Search keyword
     * @return List of matching skills
     */
    public List<Skill> searchSkills(String keyword) {
        String sql = "SELECT * FROM skills WHERE skill_name LIKE ? ORDER BY skill_name LIMIT 20";
        
        List<Skill> skills = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + keyword + "%");
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                skills.add(extractSkillFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return skills;
    }
    
    /**
     * Get most popular skills (by student count)
     * 
     * @param limit Number of skills to return
     * @return List of popular skills
     */
    public List<Skill> getMostPopularSkills(int limit) {
        String sql = "SELECT s.*, COUNT(ss.student_id) as student_count " +
                    "FROM skills s " +
                    "LEFT JOIN student_skills ss ON s.skill_id = ss.skill_id " +
                    "GROUP BY s.skill_id " +
                    "ORDER BY student_count DESC, s.skill_name " +
                    "LIMIT ?";
        
        List<Skill> skills = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, limit);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                skills.add(extractSkillFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return skills;
    }
    
    /**
     * Get skill count for a student
     * 
     * @param studentId Student ID
     * @return Number of skills
     */
    public int getStudentSkillCount(Integer studentId) {
        String sql = "SELECT COUNT(*) FROM student_skills WHERE student_id = ?";
        
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
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBConnection.closeResources(rs, pstmt, conn);
        }
        
        return 0;
    }
    
    /**
     * Extract Skill object from ResultSet
     * 
     * @param rs ResultSet positioned at a skill record
     * @return Skill object
     */
    private Skill extractSkillFromResultSet(ResultSet rs) throws SQLException {
        Skill skill = new Skill();
        
        skill.setSkillId(rs.getInt("skill_id"));
        skill.setSkillName(rs.getString("skill_name"));
        skill.setCategory(rs.getString("category"));
        skill.setCreatedAt(rs.getTimestamp("created_at"));
        
        return skill;
    }
    
    // ========================================
    // ADDITIONAL METHODS FOR SERVLET COMPATIBILITY
    // ========================================
    
    /**
     * Alias for getJobSkills
     */
    public List<Skill> findByJob(Integer jobId) {
        return getJobSkills(jobId);
    }
    
    /**
     * Alias for getStudentSkills
     */
    public List<Skill> findByStudent(Integer studentId) {
        return getStudentSkills(studentId);
    }
    
    /**
     * Update student skills (delete old, insert new)
     */
    public void updateStudentSkills(Integer studentId, String[] skillNames) {
        if (studentId == null || skillNames == null) {
            return;
        }
        
        // First, delete existing student skills
        String deleteSql = "DELETE FROM student_skills WHERE student_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteSql)) {
            pstmt.setInt(1, studentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
        
        // Then, insert new skills
        for (String skillName : skillNames) {
            if (skillName != null && !skillName.trim().isEmpty()) {
                Skill skill = findByName(skillName.trim());
                if (skill != null) {
                    addStudentSkill(studentId, skill.getSkillId(), "Intermediate");
                }
            }
        }
    }
    
    /**
     * Overload for addJobSkill that accepts skill name instead of ID
     */
    public boolean addJobSkill(Integer jobId, String skillName, boolean isRequired) {
        if (skillName == null || skillName.trim().isEmpty()) {
            return false;
        }
        
        Skill skill = findByName(skillName.trim());
        if (skill != null) {
            return addJobSkill(jobId, skill.getSkillId(), isRequired);
        }
        return false;
    }
}

