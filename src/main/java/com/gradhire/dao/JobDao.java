package com.gradhire.dao;

import com.gradhire.model.Job;
import com.gradhire.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JobDao {
    private static final String FIND_ACTIVE = "SELECT job_id, job_title, company_name, job_type, domain, location, application_deadline FROM jobs WHERE job_status = 'Active' AND application_deadline >= CURDATE() ORDER BY created_at DESC LIMIT ?";
    private static final String FIND_BY_ID = "SELECT job_id, job_title, company_name, job_type, domain, location, application_deadline FROM jobs WHERE job_id = ?";
    private static final String EXISTS_ACTIVE = "SELECT 1 FROM jobs WHERE job_id = ? AND job_status = 'Active' AND application_deadline >= CURDATE()";
    private static final String INSERT_JOB =
            "INSERT INTO jobs (admin_id, job_title, company_name, job_type, domain, description, location, application_deadline, job_status) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_JOB_BASIC =
            "UPDATE jobs SET job_title = ?, domain = ?, location = ?, application_deadline = ?, job_status = ? WHERE job_id = ?";

    public List<Job> findActiveJobs(int limit) throws SQLException {
        List<Job> jobs = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ACTIVE)) {
            statement.setInt(1, limit);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    jobs.add(mapJob(resultSet));
                }
            }
        }
        return jobs;
    }

    public Optional<Job> findById(int jobId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID)) {
            statement.setInt(1, jobId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    return Optional.empty();
                }
                return Optional.of(mapJob(resultSet));
            }
        }
    }

    public boolean isActiveAndOpen(int jobId) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(EXISTS_ACTIVE)) {
            statement.setInt(1, jobId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    public int createJob(
            int adminId,
            String jobTitle,
            String companyName,
            String jobType,
            String domain,
            String description,
            String location,
            LocalDate applicationDeadline,
            String jobStatus
    ) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(INSERT_JOB, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, adminId);
            statement.setString(2, jobTitle);
            statement.setString(3, companyName);
            statement.setString(4, jobType);
            statement.setString(5, domain);
            statement.setString(6, description);
            statement.setString(7, location);
            if (applicationDeadline != null) {
                statement.setDate(8, Date.valueOf(applicationDeadline));
            } else {
                statement.setDate(8, null);
            }
            statement.setString(9, jobStatus);
            int inserted = statement.executeUpdate();
            if (inserted != 1) {
                throw new SQLException("Failed to insert job.");
            }
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
            throw new SQLException("Failed to fetch generated job id.");
        }
    }

    public boolean updateJobBasic(int jobId, String jobTitle, String domain, String location, LocalDate applicationDeadline, String jobStatus) throws SQLException {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_JOB_BASIC)) {
            statement.setString(1, jobTitle);
            statement.setString(2, domain);
            statement.setString(3, location);
            if (applicationDeadline != null) {
                statement.setDate(4, Date.valueOf(applicationDeadline));
            } else {
                statement.setDate(4, null);
            }
            statement.setString(5, jobStatus);
            statement.setInt(6, jobId);
            return statement.executeUpdate() == 1;
        }
    }

    private Job mapJob(ResultSet resultSet) throws SQLException {
        Job job = new Job();
        job.setJobId(resultSet.getInt("job_id"));
        job.setJobTitle(resultSet.getString("job_title"));
        job.setCompanyName(resultSet.getString("company_name"));
        job.setJobType(resultSet.getString("job_type"));
        job.setDomain(resultSet.getString("domain"));
        job.setLocation(resultSet.getString("location"));
        Date applicationDeadline = resultSet.getDate("application_deadline");
        if (applicationDeadline != null) {
            job.setApplicationDeadline(applicationDeadline.toLocalDate());
        }
        return job;
    }
}
