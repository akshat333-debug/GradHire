package com.gradhire.dao;

import com.gradhire.model.Application;
import com.gradhire.model.ApplicationReviewItem;
import com.gradhire.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
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
    private static final String UPDATE_APPLICATION_STATUS =
            "UPDATE applications SET application_status = ?, reviewer_notes = ?, reviewed_at = ? WHERE application_id = ?";
    private static final String FIND_REVIEW_ITEMS_FOR_ADMIN =
            "SELECT a.application_id, a.job_id, j.job_title, a.student_id, s.full_name AS student_name, a.application_status, a.applied_at " +
                    "FROM applications a " +
                    "JOIN jobs j ON j.job_id = a.job_id " +
                    "JOIN students s ON s.student_id = a.student_id " +
                    "WHERE j.admin_id = ? " +
                    "ORDER BY a.applied_at DESC";
    private static final String FIND_REVIEW_ITEMS_FOR_SUPER_ADMIN =
            "SELECT a.application_id, a.job_id, j.job_title, a.student_id, s.full_name AS student_name, a.application_status, a.applied_at " +
                    "FROM applications a " +
                    "JOIN jobs j ON j.job_id = a.job_id " +
                    "JOIN students s ON s.student_id = a.student_id " +
                    "ORDER BY a.applied_at DESC";
    private static final String UPDATE_APPLICATION_STATUS_FOR_ADMIN =
            "UPDATE applications a " +
                    "JOIN jobs j ON j.job_id = a.job_id " +
                    "SET a.application_status = ?, a.reviewer_notes = ?, a.reviewed_at = ? " +
                    "WHERE a.application_id = ? AND j.admin_id = ?";
    private static final String UPDATE_APPLICATION_STATUS_FOR_SUPER_ADMIN =
            "UPDATE applications SET application_status = ?, reviewer_notes = ?, reviewed_at = ? WHERE application_id = ?";

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

    public boolean updateApplicationStatus(int applicationId, String status, String reviewerNotes) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
              PreparedStatement statement = connection.prepareStatement(UPDATE_APPLICATION_STATUS)) {
            statement.setString(1, status);
            statement.setString(2, reviewerNotes);
            if (status == null || "Pending".equalsIgnoreCase(status)) {
                statement.setNull(3, Types.TIMESTAMP);
            } else {
                statement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            }
            statement.setInt(4, applicationId);
            return statement.executeUpdate() == 1;
        }
    }

    public List<ApplicationReviewItem> findReviewItemsForAdmin(int adminId) throws SQLException {
        List<ApplicationReviewItem> items = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_REVIEW_ITEMS_FOR_ADMIN)) {
            statement.setInt(1, adminId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    items.add(mapReviewItem(resultSet));
                }
            }
        }
        return items;
    }

    public List<ApplicationReviewItem> findReviewItemsForSuperAdmin() throws SQLException {
        List<ApplicationReviewItem> items = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_REVIEW_ITEMS_FOR_SUPER_ADMIN);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                items.add(mapReviewItem(resultSet));
            }
        }
        return items;
    }

    public boolean updateApplicationStatusForAdmin(int applicationId, String status, String reviewerNotes, int adminId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_APPLICATION_STATUS_FOR_ADMIN)) {
            applyStatusParams(statement, status, reviewerNotes, applicationId);
            statement.setInt(5, adminId);
            return statement.executeUpdate() == 1;
        }
    }

    public boolean updateApplicationStatusForSuperAdmin(int applicationId, String status, String reviewerNotes) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_APPLICATION_STATUS_FOR_SUPER_ADMIN)) {
            applyStatusParams(statement, status, reviewerNotes, applicationId);
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

    private ApplicationReviewItem mapReviewItem(ResultSet resultSet) throws SQLException {
        ApplicationReviewItem item = new ApplicationReviewItem();
        item.setApplicationId(resultSet.getInt("application_id"));
        item.setJobId(resultSet.getInt("job_id"));
        item.setJobTitle(resultSet.getString("job_title"));
        item.setStudentId(resultSet.getInt("student_id"));
        item.setStudentName(resultSet.getString("student_name"));
        item.setApplicationStatus(resultSet.getString("application_status"));
        Timestamp appliedAtTs = resultSet.getTimestamp("applied_at");
        item.setAppliedAt(appliedAtTs != null ? appliedAtTs.toLocalDateTime() : null);
        return item;
    }

    private void applyStatusParams(PreparedStatement statement, String status, String reviewerNotes, int applicationId) throws SQLException {
        statement.setString(1, status);
        statement.setString(2, reviewerNotes);
        if (status == null || "Pending".equalsIgnoreCase(status)) {
            statement.setNull(3, Types.TIMESTAMP);
        } else {
            statement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
        }
        statement.setInt(4, applicationId);
    }
}
