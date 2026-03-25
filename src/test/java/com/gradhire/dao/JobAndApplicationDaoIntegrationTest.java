package com.gradhire.dao;

import com.gradhire.model.Application;
import com.gradhire.model.ApplicationReviewItem;
import com.gradhire.model.Job;
import com.gradhire.model.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JobAndApplicationDaoIntegrationTest {
    private static final JobDao JOB_DAO = new JobDao();
    private static final AdminDao ADMIN_DAO = new AdminDao();
    private static final StudentDao STUDENT_DAO = new StudentDao();
    private static final ApplicationDao APPLICATION_DAO = new ApplicationDao();

    @BeforeAll
    static void setup() {
        DaoIntegrationTestSupport.assumeDatabaseAvailable();
    }

    @Test
    void createAndUpdateJobAndApplicationStatus() throws SQLException {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        int adminId = ADMIN_DAO.createAdmin(
                "jobadmin+" + suffix + "@example.com",
                "$2a$10$abcdefghijklmnopqrstuv123456789012345678901234567890",
                "Job Admin",
                "Company " + suffix,
                "recruiter"
        );
        int studentId = STUDENT_DAO.createStudent(
                "jobstudent+" + suffix + "@example.com",
                "$2a$10$abcdefghijklmnopqrstuv123456789012345678901234567890",
                "Job Student",
                "Test College"
        );

        LocalDate deadline = LocalDate.now().plusDays(15);
        int jobId = JOB_DAO.createJob(
                adminId,
                "Integration Job " + suffix,
                "Company " + suffix,
                "Internship",
                "Testing",
                "Integration test description",
                "Remote",
                deadline,
                "Active"
        );

        Optional<Job> createdJob = JOB_DAO.findById(jobId);
        assertTrue(createdJob.isPresent());
        assertEquals("Integration Job " + suffix, createdJob.get().getJobTitle());
        assertTrue(JOB_DAO.isActiveAndOpen(jobId));

        boolean jobUpdated = JOB_DAO.updateJobBasic(
                jobId,
                "Updated Integration Job " + suffix,
                "Automation",
                "Hybrid",
                LocalDate.now().plusDays(20),
                "Active"
        );
        assertTrue(jobUpdated);

        Optional<Job> updatedJob = JOB_DAO.findById(jobId);
        assertTrue(updatedJob.isPresent());
        assertEquals("Updated Integration Job " + suffix, updatedJob.get().getJobTitle());
        assertEquals("Automation", updatedJob.get().getDomain());

        boolean applied = APPLICATION_DAO.applyToJob(jobId, studentId, "Cover letter " + suffix);
        assertTrue(applied);
        assertTrue(APPLICATION_DAO.hasApplied(jobId, studentId));

        Optional<Application> application = APPLICATION_DAO.findByJobAndStudent(jobId, studentId);
        assertTrue(application.isPresent());
        assertEquals("Pending", application.get().getApplicationStatus());

        boolean statusUpdated = APPLICATION_DAO.updateApplicationStatus(application.get().getApplicationId(), "Reviewed", "Looks good");
        assertTrue(statusUpdated);

        Optional<Application> updatedApplication = APPLICATION_DAO.findByJobAndStudent(jobId, studentId);
        assertTrue(updatedApplication.isPresent());
        assertEquals("Reviewed", updatedApplication.get().getApplicationStatus());
    }

    @Test
    void recruiterCanReviewOwnApplicationsButNotOthers() throws SQLException {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        int recruiterA = ADMIN_DAO.createAdmin(
                "recruitera+" + suffix + "@example.com",
                "$2a$10$abcdefghijklmnopqrstuv123456789012345678901234567890",
                "Recruiter A",
                "Company A " + suffix,
                "recruiter"
        );
        int recruiterB = ADMIN_DAO.createAdmin(
                "recruiterb+" + suffix + "@example.com",
                "$2a$10$abcdefghijklmnopqrstuv123456789012345678901234567890",
                "Recruiter B",
                "Company B " + suffix,
                "recruiter"
        );
        int studentId = STUDENT_DAO.createStudent(
                "reviewstudent+" + suffix + "@example.com",
                "$2a$10$abcdefghijklmnopqrstuv123456789012345678901234567890",
                "Review Student",
                "Review College"
        );

        int jobId = JOB_DAO.createJob(
                recruiterA,
                "Review Job " + suffix,
                "Company A " + suffix,
                "Internship",
                "QA",
                "Review flow job description",
                "Remote",
                LocalDate.now().plusDays(10),
                "Active"
        );

        assertTrue(APPLICATION_DAO.applyToJob(jobId, studentId, "Review letter " + suffix));
        Optional<Application> application = APPLICATION_DAO.findByJobAndStudent(jobId, studentId);
        assertTrue(application.isPresent());

        boolean reviewerBCanUpdate = APPLICATION_DAO.updateApplicationStatusForAdmin(
                application.get().getApplicationId(),
                "Shortlisted",
                "Should fail for non-owner recruiter",
                recruiterB
        );
        assertFalse(reviewerBCanUpdate);

        boolean reviewerACanUpdate = APPLICATION_DAO.updateApplicationStatusForAdmin(
                application.get().getApplicationId(),
                "Shortlisted",
                "Looks promising",
                recruiterA
        );
        assertTrue(reviewerACanUpdate);

        Optional<Application> updated = APPLICATION_DAO.findByJobAndStudent(jobId, studentId);
        assertTrue(updated.isPresent());
        assertEquals("Shortlisted", updated.get().getApplicationStatus());

        var reviewItems = APPLICATION_DAO.findReviewItemsForAdmin(recruiterA);
        assertTrue(reviewItems.stream().map(ApplicationReviewItem::getApplicationId).anyMatch(id -> id == application.get().getApplicationId()));
    }
}
