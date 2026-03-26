package com.gradhire.dao;

import com.gradhire.model.ActivityLog;
import com.gradhire.model.Job;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecommendationsSavedJobsActivityDaoIntegrationTest {
    private static final AdminDao ADMIN_DAO = new AdminDao();
    private static final StudentDao STUDENT_DAO = new StudentDao();
    private static final JobDao JOB_DAO = new JobDao();
    private static final RecommendationDao RECOMMENDATION_DAO = new RecommendationDao();
    private static final SavedJobDao SAVED_JOB_DAO = new SavedJobDao();
    private static final ActivityLogDao ACTIVITY_LOG_DAO = new ActivityLogDao();

    @BeforeAll
    static void setup() {
        DaoIntegrationTestSupport.assumeDatabaseAvailable();
    }

    @Test
    void savedJobsAndActivityLogsCanBeCreatedAndFetched() throws SQLException {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        int adminId = ADMIN_DAO.createAdmin(
                "savedadmin+" + suffix + "@example.com",
                "$2a$10$abcdefghijklmnopqrstuv123456789012345678901234567890",
                "Saved Admin",
                "Saved Co " + suffix,
                "recruiter"
        );
        int studentId = STUDENT_DAO.createStudent(
                "savedstudent+" + suffix + "@example.com",
                "$2a$10$abcdefghijklmnopqrstuv123456789012345678901234567890",
                "Saved Student",
                "Saved College"
        );

        int jobId = JOB_DAO.createJob(
                adminId,
                "Saved Job " + suffix,
                "Saved Co " + suffix,
                "Internship",
                "Backend",
                "Saved job description",
                "Remote",
                LocalDate.now().plusDays(20),
                "Active"
        );

        assertTrue(SAVED_JOB_DAO.saveJob(studentId, jobId));
        assertTrue(SAVED_JOB_DAO.hasSavedJob(studentId, jobId));
        List<Job> savedJobs = SAVED_JOB_DAO.findSavedJobsByStudentId(studentId, 10);
        assertTrue(savedJobs.stream().map(Job::getJobId).anyMatch(id -> id == jobId));

        ACTIVITY_LOG_DAO.logActivity("student", studentId, "test_event", "integration test activity", "127.0.0.1", "junit");
        List<ActivityLog> logs = ACTIVITY_LOG_DAO.findRecentByUser("student", studentId, 10);
        assertTrue(logs.stream().anyMatch(log -> "test_event".equals(log.getActivityType())));

        assertTrue(SAVED_JOB_DAO.removeSavedJob(studentId, jobId));
        assertFalse(SAVED_JOB_DAO.hasSavedJob(studentId, jobId));
    }

    @Test
    void recommendationQueryReturnsMatchesForSkillAlignedStudent() throws SQLException {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        int adminId = ADMIN_DAO.createAdmin(
                "recadmin+" + suffix + "@example.com",
                "$2a$10$abcdefghijklmnopqrstuv123456789012345678901234567890",
                "Rec Admin",
                "Rec Co " + suffix,
                "recruiter"
        );
        int studentId = STUDENT_DAO.createStudent(
                "recstudent+" + suffix + "@example.com",
                "$2a$10$abcdefghijklmnopqrstuv123456789012345678901234567890",
                "Rec Student",
                "Rec College"
        );
        int jobId = JOB_DAO.createJob(
                adminId,
                "Recommendation Job " + suffix,
                "Rec Co " + suffix,
                "Internship",
                "Engineering",
                "Recommendation job description",
                "Hybrid",
                LocalDate.now().plusDays(15),
                "Active"
        );

        int skillId = findAnySkillId();
        linkStudentSkill(studentId, skillId);
        linkJobSkill(jobId, skillId);

        List<Job> recommendations = RECOMMENDATION_DAO.findRecommendationsForStudent(studentId, 10);
        assertTrue(recommendations.stream().map(Job::getJobId).anyMatch(id -> id == jobId));
    }

    private int findAnySkillId() throws SQLException {
        try (Connection connection = com.gradhire.util.DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT skill_id FROM skills LIMIT 1");
             ResultSet resultSet = statement.executeQuery()) {
            if (!resultSet.next()) {
                throw new SQLException("No skills available in database.");
            }
            return resultSet.getInt(1);
        }
    }

    private void linkStudentSkill(int studentId, int skillId) throws SQLException {
        try (Connection connection = com.gradhire.util.DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO student_skills (student_id, skill_id, proficiency_level) VALUES (?, ?, 'Intermediate')")) {
            statement.setInt(1, studentId);
            statement.setInt(2, skillId);
            statement.executeUpdate();
        }
    }

    private void linkJobSkill(int jobId, int skillId) throws SQLException {
        try (Connection connection = com.gradhire.util.DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO job_skills (job_id, skill_id, is_required) VALUES (?, ?, TRUE)")) {
            statement.setInt(1, jobId);
            statement.setInt(2, skillId);
            statement.executeUpdate();
        }
    }
}
