package com.gradhire.dao;

import com.gradhire.model.ActivityLog;
import com.gradhire.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ActivityLogDao {
    private static final String INSERT_ACTIVITY =
            "INSERT INTO activity_logs (user_type, user_id, activity_type, activity_description, ip_address, user_agent) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String FIND_BY_USER =
            "SELECT log_id, activity_type, activity_description, created_at FROM activity_logs WHERE user_type = ? AND user_id = ? ORDER BY created_at DESC LIMIT ?";

    public void logActivity(String userType, int userId, String activityType, String description, String ipAddress, String userAgent) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_ACTIVITY)) {
            statement.setString(1, normalizeUserType(userType));
            statement.setInt(2, userId);
            statement.setString(3, activityType);
            statement.setString(4, description);
            statement.setString(5, ipAddress);
            statement.setString(6, userAgent);
            statement.executeUpdate();
        }
    }

    public List<ActivityLog> findRecentByUser(String userType, int userId, int limit) throws SQLException {
        List<ActivityLog> logs = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_USER)) {
            statement.setString(1, normalizeUserType(userType));
            statement.setInt(2, userId);
            statement.setInt(3, limit);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ActivityLog log = new ActivityLog();
                    log.setLogId(resultSet.getInt("log_id"));
                    log.setActivityType(resultSet.getString("activity_type"));
                    log.setActivityDescription(resultSet.getString("activity_description"));
                    Timestamp createdAt = resultSet.getTimestamp("created_at");
                    if (createdAt != null) {
                        log.setCreatedAt(createdAt.toLocalDateTime());
                    }
                    logs.add(log);
                }
            }
        }
        return logs;
    }

    private String normalizeUserType(String userType) {
        return "recruiter".equalsIgnoreCase(userType) ? "admin" : userType;
    }
}
