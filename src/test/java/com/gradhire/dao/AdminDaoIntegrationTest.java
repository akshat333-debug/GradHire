package com.gradhire.dao;

import com.gradhire.model.Admin;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdminDaoIntegrationTest {
    private static final AdminDao ADMIN_DAO = new AdminDao();

    @BeforeAll
    static void setup() {
        DaoIntegrationTestSupport.assumeDatabaseAvailable();
    }

    @Test
    void createAndUpdateAdminProfile() throws SQLException {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 10);
        String email = "admin+" + suffix + "@example.com";
        int adminId = ADMIN_DAO.createAdmin(
                email,
                "$2a$10$abcdefghijklmnopqrstuv123456789012345678901234567890",
                "Test Recruiter",
                "Initial Company",
                "recruiter"
        );

        Optional<Admin> created = ADMIN_DAO.findById(adminId);
        assertTrue(created.isPresent());
        assertEquals(email, created.get().getEmail());

        boolean updated = ADMIN_DAO.updateAdminProfile(adminId, "Updated Recruiter", "Updated Company", "admin");
        assertTrue(updated);

        Optional<Admin> updatedAdmin = ADMIN_DAO.findById(adminId);
        assertTrue(updatedAdmin.isPresent());
        assertEquals("Updated Recruiter", updatedAdmin.get().getFullName());
        assertEquals("Updated Company", updatedAdmin.get().getCompanyName());
        assertEquals("admin", updatedAdmin.get().getRole());
    }
}
