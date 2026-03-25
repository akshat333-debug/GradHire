package com.gradhire.dao;

import com.gradhire.model.Admin;
import com.gradhire.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class AdminDao {
    private static final String FIND_BY_ID =
            "SELECT admin_id, email, full_name, company_name, role FROM admins WHERE admin_id = ?";
    private static final String INSERT_ADMIN =
            "INSERT INTO admins (email, password_hash, full_name, company_name, role) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_ADMIN_PROFILE =
            "UPDATE admins SET full_name = ?, company_name = ?, role = ? WHERE admin_id = ?";

    public Optional<Admin> findById(int adminId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setInt(1, adminId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                Admin admin = new Admin();
                admin.setAdminId(resultSet.getInt("admin_id"));
                admin.setEmail(resultSet.getString("email"));
                admin.setFullName(resultSet.getString("full_name"));
                admin.setCompanyName(resultSet.getString("company_name"));
                admin.setRole(resultSet.getString("role"));
                return Optional.of(admin);
            }
        }
    }

    public int createAdmin(String email, String passwordHash, String fullName, String companyName, String role) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_ADMIN, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, email);
            statement.setString(2, passwordHash);
            statement.setString(3, fullName);
            statement.setString(4, companyName);
            statement.setString(5, role);
            int inserted = statement.executeUpdate();
            if (inserted != 1) {
                throw new SQLException("Failed to insert admin.");
            }
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
            throw new SQLException("Failed to fetch generated admin id.");
        }
    }

    public boolean updateAdminProfile(int adminId, String fullName, String companyName, String role) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_ADMIN_PROFILE)) {
            statement.setString(1, fullName);
            statement.setString(2, companyName);
            statement.setString(3, role);
            statement.setInt(4, adminId);
            return statement.executeUpdate() == 1;
        }
    }
}
