package com.gradhire.util;

import com.gradhire.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class DBConnection {
    private static volatile boolean driverLoaded = false;

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        ensureDriverLoaded();
        Connection connection = DriverManager.getConnection(
                DatabaseConfig.getUrl(),
                DatabaseConfig.getUsername(),
                DatabaseConfig.getPassword()
        );
        connection.setAutoCommit(true);
        return connection;
    }

    private static void ensureDriverLoaded() {
        if (driverLoaded) {
            return;
        }

        synchronized (DBConnection.class) {
            if (driverLoaded) {
                return;
            }

            try {
                Class.forName(DatabaseConfig.getDriverClass());
                driverLoaded = true;
            } catch (ClassNotFoundException exception) {
                throw new IllegalStateException("Unable to load database driver", exception);
            }
        }
    }
}
