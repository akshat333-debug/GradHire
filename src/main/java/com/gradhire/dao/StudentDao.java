package com.gradhire.dao;

import com.gradhire.model.Student;
import com.gradhire.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class StudentDao {
    private static final String FIND_BY_ID = "SELECT student_id, email, full_name, college_name FROM students WHERE student_id = ?";
    private static final String FIND_BY_EMAIL = "SELECT student_id, email, full_name, college_name FROM students WHERE email = ?";
    private static final String INSERT_STUDENT =
            "INSERT INTO students (email, password_hash, full_name, college_name) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_STUDENT_PROFILE =
            "UPDATE students SET full_name = ?, college_name = ? WHERE student_id = ?";

    public Optional<Student> findById(int studentId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setInt(1, studentId);
            return mapSingle(statement);
        }
    }

    public Optional<Student> findByEmail(String email) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_EMAIL)) {
            statement.setString(1, email);
            return mapSingle(statement);
        }
    }

    public int createStudent(String email, String passwordHash, String fullName, String collegeName) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_STUDENT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, email);
            statement.setString(2, passwordHash);
            statement.setString(3, fullName);
            statement.setString(4, collegeName);
            int inserted = statement.executeUpdate();
            if (inserted != 1) {
                throw new SQLException("Failed to insert student.");
            }
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
            throw new SQLException("Failed to fetch generated student id.");
        }
    }

    public boolean updateStudentProfile(int studentId, String fullName, String collegeName) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_STUDENT_PROFILE)) {
            statement.setString(1, fullName);
            statement.setString(2, collegeName);
            statement.setInt(3, studentId);
            return statement.executeUpdate() == 1;
        }
    }

    private Optional<Student> mapSingle(PreparedStatement statement) throws SQLException {
        try (ResultSet resultSet = statement.executeQuery()) {
            if (!resultSet.next()) {
                return Optional.empty();
            }

            Student student = new Student();
            student.setStudentId(resultSet.getInt("student_id"));
            student.setEmail(resultSet.getString("email"));
            student.setFullName(resultSet.getString("full_name"));
            student.setCollegeName(resultSet.getString("college_name"));
            return Optional.of(student);
        }
    }
}
