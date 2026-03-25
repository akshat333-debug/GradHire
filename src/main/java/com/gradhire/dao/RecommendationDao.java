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

public class RecommendationDao {
    private static final String FIND_RECOMMENDATIONS =
            "SELECT j.job_id, j.job_title, j.company_name, j.job_type, j.domain, j.location, j.application_deadline " +
                    "FROM jobs j " +
                    "JOIN job_skills js ON j.job_id = js.job_id " +
                    "JOIN student_skills ss ON js.skill_id = ss.skill_id " +
                    "WHERE ss.student_id = ? " +
                    "AND j.job_status = 'Active' " +
                    "AND j.application_deadline >= CURDATE() " +
                    "AND NOT EXISTS (SELECT 1 FROM applications a WHERE a.job_id = j.job_id AND a.student_id = ?) " +
                    "GROUP BY j.job_id, j.job_title, j.company_name, j.job_type, j.domain, j.location, j.application_deadline " +
                    "ORDER BY COUNT(DISTINCT js.skill_id) DESC, j.created_at DESC " +
                    "LIMIT ?";

    public List<Job> findRecommendationsForStudent(int studentId, int limit) throws SQLException {
        List<Job> jobs = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_RECOMMENDATIONS)) {
            statement.setInt(1, studentId);
            statement.setInt(2, studentId);
            statement.setInt(3, limit);
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
}
