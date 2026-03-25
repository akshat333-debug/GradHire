package com.gradhire.dao;

import com.gradhire.model.Admin;
import com.gradhire.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AdminDao {
    private static final String FIND_BY_ID =
            "SELECT admin_id, email, full_name, company_name, role FROM admins WHERE admin_id = ?";

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
}
