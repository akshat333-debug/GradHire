package com.gradhire.dao;

import com.gradhire.model.Skill;
import com.gradhire.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SkillDao {
    private static final String FIND_ALL = "SELECT skill_id, skill_name, category FROM skills ORDER BY skill_name ASC";
    private static final String FIND_BY_ID = "SELECT skill_id, skill_name, category FROM skills WHERE skill_id = ?";
    private static final String FIND_BY_STUDENT_ID =
            "SELECT sk.skill_id, sk.skill_name, sk.category " +
            "FROM skills sk " +
            "JOIN student_skills ss ON sk.skill_id = ss.skill_id " +
            "WHERE ss.student_id = ? " +
            "ORDER BY sk.skill_name ASC";
    private static final String INSERT_SKILL = "INSERT INTO skills (skill_name, category) VALUES (?, ?)";
    private static final String UPDATE_SKILL = "UPDATE skills SET skill_name = ?, category = ? WHERE skill_id = ?";

    public List<Skill> findAll() throws SQLException {
        List<Skill> skills = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                skills.add(mapSkill(resultSet));
            }
        }
        return skills;
    }

    public Optional<Skill> findById(int skillId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setInt(1, skillId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapSkill(resultSet));
            }
        }
    }

    public List<Skill> findByStudentId(int studentId) throws SQLException {
        List<Skill> skills = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_STUDENT_ID)) {
            statement.setInt(1, studentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    skills.add(mapSkill(resultSet));
                }
            }
        }
        return skills;
    }

    public int createSkill(String skillName, String category) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_SKILL, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, skillName);
            statement.setString(2, category);
            int inserted = statement.executeUpdate();
            if (inserted != 1) {
                throw new SQLException("Failed to insert skill.");
            }
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
            throw new SQLException("Failed to fetch generated skill id.");
        }
    }

    public boolean updateSkill(int skillId, String skillName, String category) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_SKILL)) {
            statement.setString(1, skillName);
            statement.setString(2, category);
            statement.setInt(3, skillId);
            return statement.executeUpdate() == 1;
        }
    }

    private Skill mapSkill(ResultSet resultSet) throws SQLException {
        Skill skill = new Skill();
        skill.setSkillId(resultSet.getInt("skill_id"));
        skill.setSkillName(resultSet.getString("skill_name"));
        skill.setCategory(resultSet.getString("category"));
        return skill;
    }
}
