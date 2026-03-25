package com.gradhire.dao;

import com.gradhire.model.Student;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StudentDaoIntegrationTest {
    private static final StudentDao STUDENT_DAO = new StudentDao();

    @BeforeAll
    static void setup() {
        DaoIntegrationTestSupport.assumeDatabaseAvailable();
    }

    @Test
    void createAndUpdateStudentProfile() throws SQLException {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        String email = "student+" + suffix + "@example.com";
        int id = STUDENT_DAO.createStudent(email, "$2a$10$abcdefghijklmnopqrstuv123456789012345678901234567890", "Test Student", "Initial College");

        Optional<Student> created = STUDENT_DAO.findById(id);
        assertTrue(created.isPresent());
        assertEquals(email, created.get().getEmail());

        boolean updated = STUDENT_DAO.updateStudentProfile(id, "Updated Student", "Updated College");
        assertTrue(updated);

        Optional<Student> updatedStudent = STUDENT_DAO.findById(id);
        assertTrue(updatedStudent.isPresent());
        assertEquals("Updated Student", updatedStudent.get().getFullName());
        assertEquals("Updated College", updatedStudent.get().getCollegeName());
    }
}
