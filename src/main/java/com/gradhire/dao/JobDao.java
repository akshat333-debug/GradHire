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
import java.util.Optional;

public class JobDao {
    private static final String FIND_ACTIVE = "SELECT job_id, job_title, company_name, job_type, domain, location, application_deadline FROM jobs WHERE job_status = 'Active' AND application_deadline >= CURDATE() ORDER BY created_at DESC LIMIT ?";
    private static final String FIND_BY_ID = "SELECT job_id, job_title, company_name, job_type, domain, location, application_deadline FROM jobs WHERE job_id = ?";
    private static final String EXISTS_ACTIVE = "SELECT 1 FROM jobs WHERE job_id = ? AND job_status = 'Active' AND application_deadline >= CURDATE()";

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
