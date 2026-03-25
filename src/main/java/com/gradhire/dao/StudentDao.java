package com.gradhire.dao;

import com.gradhire.model.Student;
import com.gradhire.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class StudentDao {
    private static final String FIND_BY_ID = "SELECT student_id, email, full_name, college_name FROM students WHERE student_id = ?";
    private static final String FIND_BY_EMAIL = "SELECT student_id, email, full_name, college_name FROM students WHERE email = ?";

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
