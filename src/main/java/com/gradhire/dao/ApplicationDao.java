package com.gradhire.dao;

import com.gradhire.model.Application;
import com.gradhire.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ApplicationDao {
    private static final String FIND_BY_STUDENT = "SELECT application_id, job_id, student_id, cover_letter, application_status, applied_at FROM applications WHERE student_id = ? ORDER BY applied_at DESC";
    private static final String FIND_BY_JOB_AND_STUDENT =
            "SELECT application_id, job_id, student_id, cover_letter, application_status, applied_at " +
            "FROM applications WHERE job_id = ? AND student_id = ?";
    private static final String EXISTS_BY_JOB_AND_STUDENT =
            "SELECT 1 FROM applications WHERE job_id = ? AND student_id = ?";

    public List<Application> findByStudentId(int studentId) throws SQLException {
        List<Application> applications = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_STUDENT)) {
            statement.setInt(1, studentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    applications.add(mapApplication(resultSet));
                }
            }
        }
        return applications;
    }

    public Optional<Application> findByJobAndStudent(int jobId, int studentId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_JOB_AND_STUDENT)) {
            statement.setInt(1, jobId);
            statement.setInt(2, studentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapApplication(resultSet));
            }
        }
    }

    public boolean hasApplied(int jobId, int studentId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(EXISTS_BY_JOB_AND_STUDENT)) {
            statement.setInt(1, jobId);
            statement.setInt(2, studentId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
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

    private Application mapApplication(ResultSet resultSet) throws SQLException {
        Application application = new Application();
        application.setApplicationId(resultSet.getInt("application_id"));
        application.setJobId(resultSet.getInt("job_id"));
        application.setStudentId(resultSet.getInt("student_id"));
        application.setCoverLetter(resultSet.getString("cover_letter"));
        application.setApplicationStatus(resultSet.getString("application_status"));
        Timestamp appliedAtTs = resultSet.getTimestamp("applied_at");
        LocalDateTime appliedAt = appliedAtTs != null ? appliedAtTs.toLocalDateTime() : null;
        application.setAppliedAt(appliedAt);
        return application;
    }
}
