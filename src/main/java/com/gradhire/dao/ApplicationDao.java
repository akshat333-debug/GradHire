package com.gradhire.dao;

import com.gradhire.model.Application;
import com.gradhire.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDao {
    private static final String FIND_BY_STUDENT = "SELECT application_id, job_id, student_id, application_status, applied_at FROM applications WHERE student_id = ? ORDER BY applied_at DESC";

    public List<Application> findByStudentId(int studentId) throws SQLException {
        List<Application> applications = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_STUDENT)) {
            statement.setInt(1, studentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Application application = new Application();
                    application.setApplicationId(resultSet.getInt("application_id"));
                    application.setJobId(resultSet.getInt("job_id"));
                    application.setStudentId(resultSet.getInt("student_id"));
                    application.setApplicationStatus(resultSet.getString("application_status"));
                    application.setAppliedAt(resultSet.getTimestamp("applied_at").toLocalDateTime());
                    applications.add(application);
                }
            }
        }
        return applications;
    }

    public boolean applyToJob(int jobId, int studentId, String coverLetter) throws SQLException {
        String sql = "INSERT INTO applications (job_id, student_id, cover_letter, application_status) VALUES (?, ?, ?, 'Pending')";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, jobId);
            statement.setInt(2, studentId);
            statement.setString(3, coverLetter);
            return statement.executeUpdate() == 1;
        }
    }
}
