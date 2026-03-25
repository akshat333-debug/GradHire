package com.gradhire.dao;

import com.gradhire.model.Job;
import com.gradhire.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JobDao {
    private static final String FIND_ACTIVE = "SELECT job_id, job_title, company_name, job_type, domain, location FROM jobs WHERE job_status = 'Active' AND application_deadline >= CURDATE() ORDER BY created_at DESC LIMIT ?";

    public List<Job> findActiveJobs(int limit) throws SQLException {
        List<Job> jobs = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ACTIVE)) {
            statement.setInt(1, limit);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Job job = new Job();
                    job.setJobId(resultSet.getInt("job_id"));
                    job.setJobTitle(resultSet.getString("job_title"));
                    job.setCompanyName(resultSet.getString("company_name"));
                    job.setJobType(resultSet.getString("job_type"));
                    job.setDomain(resultSet.getString("domain"));
                    job.setLocation(resultSet.getString("location"));
                    jobs.add(job);
                }
            }
        }
        return jobs;
    }
}
