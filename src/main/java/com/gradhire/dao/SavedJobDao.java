package com.gradhire.dao;

import com.gradhire.model.Job;
import com.gradhire.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SavedJobDao {
    private static final String FIND_SAVED_JOBS =
            "SELECT j.job_id, j.job_title, j.company_name, j.job_type, j.domain, j.location, j.application_deadline " +
                    "FROM saved_jobs sj " +
                    "JOIN jobs j ON j.job_id = sj.job_id " +
                    "WHERE sj.student_id = ? " +
                    "ORDER BY sj.saved_at DESC LIMIT ?";
    private static final String SAVE_JOB =
            "INSERT INTO saved_jobs (student_id, job_id) VALUES (?, ?)";
    private static final String REMOVE_SAVED_JOB =
            "DELETE FROM saved_jobs WHERE student_id = ? AND job_id = ?";
    private static final String HAS_SAVED_JOB =
            "SELECT 1 FROM saved_jobs WHERE student_id = ? AND job_id = ?";

    public List<Job> findSavedJobsByStudentId(int studentId, int limit) throws SQLException {
        List<Job> jobs = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_SAVED_JOBS)) {
            statement.setInt(1, studentId);
            statement.setInt(2, limit);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Job job = new Job();
                    job.setJobId(resultSet.getInt("job_id"));
                    job.setJobTitle(resultSet.getString("job_title"));
                    job.setCompanyName(resultSet.getString("company_name"));
                    job.setJobType(resultSet.getString("job_type"));
                    job.setDomain(resultSet.getString("domain"));
                    job.setLocation(resultSet.getString("location"));
                    Date deadline = resultSet.getDate("application_deadline");
                    if (deadline != null) {
                        job.setApplicationDeadline(deadline.toLocalDate());
                    }
                    jobs.add(job);
                }
            }
        }
        return jobs;
    }

    public boolean saveJob(int studentId, int jobId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_JOB)) {
            statement.setInt(1, studentId);
            statement.setInt(2, jobId);
            return statement.executeUpdate() == 1;
        }
    }

    public boolean removeSavedJob(int studentId, int jobId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(REMOVE_SAVED_JOB)) {
            statement.setInt(1, studentId);
            statement.setInt(2, jobId);
            return statement.executeUpdate() == 1;
        }
    }

    public boolean hasSavedJob(int studentId, int jobId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(HAS_SAVED_JOB)) {
            statement.setInt(1, studentId);
            statement.setInt(2, jobId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
}
