package com.gradhire.dao;

import com.gradhire.model.AuthResult;
import com.gradhire.util.DBConnection;
import com.gradhire.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AuthDao {
    private static final String STUDENT_QUERY = "SELECT student_id, full_name, password_hash FROM students WHERE email = ? AND is_active = TRUE";
    private static final String ADMIN_QUERY = "SELECT admin_id, full_name, password_hash, role FROM admins WHERE email = ? AND is_active = TRUE";

    public Optional<AuthResult> authenticateStudent(String email, String plainPassword) throws SQLException {
        return authenticate(email, plainPassword, STUDENT_QUERY, "student");
    }

    public Optional<AuthResult> authenticateAdminOrRecruiter(String email, String plainPassword) throws SQLException {
        return authenticateAdmin(email, plainPassword);
    }

    private Optional<AuthResult> authenticate(String email, String plainPassword, String query, String userType) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }

                String storedHash = resultSet.getString("password_hash");
                if (!PasswordUtil.verify(plainPassword, storedHash)) {
                    return Optional.empty();
                }

                int userId = resultSet.getInt("student_id");
                String fullName = resultSet.getString("full_name");
                return Optional.of(new AuthResult(userId, userType, fullName));
            }
        }
    }

    private Optional<AuthResult> authenticateAdmin(String email, String plainPassword) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(ADMIN_QUERY)) {
            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }

                String storedHash = resultSet.getString("password_hash");
                if (!PasswordUtil.verify(plainPassword, storedHash)) {
                    return Optional.empty();
                }

                int userId = resultSet.getInt("admin_id");
                String fullName = resultSet.getString("full_name");
                String role = resultSet.getString("role");
                return Optional.of(new AuthResult(userId, role, fullName));
            }
        }
    }
}
