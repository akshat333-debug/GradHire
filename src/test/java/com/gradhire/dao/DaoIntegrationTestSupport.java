package com.gradhire.dao;

import com.gradhire.util.DBConnection;
import org.junit.jupiter.api.Assumptions;

import java.sql.Connection;
import java.sql.SQLException;

final class DaoIntegrationTestSupport {
    private DaoIntegrationTestSupport() {
    }

    static void assumeDatabaseAvailable() {
        try (Connection ignored = DBConnection.getConnection()) {
            // DB available.
        } catch (SQLException | RuntimeException exception) {
            Assumptions.assumeTrue(false, "Skipping DAO integration tests because database is unavailable.");
        }
    }
}
