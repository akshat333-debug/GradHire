package com.gradhire.config;

public final class DatabaseConfig {
    private static final String DEFAULT_URL = "jdbc:mysql://localhost:3306/gradhire_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DEFAULT_USERNAME = "root";
    private static final String DEFAULT_PASSWORD = "";
    private static final String DEFAULT_DRIVER = "com.mysql.cj.jdbc.Driver";

    private DatabaseConfig() {
    }

    public static String getUrl() {
        return resolve("GRADHIRE_DB_URL", "gradhire.db.url", DEFAULT_URL);
    }

    public static String getUsername() {
        return resolve("GRADHIRE_DB_USERNAME", "gradhire.db.username", DEFAULT_USERNAME);
    }

    public static String getPassword() {
        return resolve("GRADHIRE_DB_PASSWORD", "gradhire.db.password", DEFAULT_PASSWORD);
    }

    public static String getDriverClass() {
        return resolve("GRADHIRE_DB_DRIVER", "gradhire.db.driver", DEFAULT_DRIVER);
    }

    private static String resolve(String envKey, String systemKey, String fallback) {
        String env = System.getenv(envKey);
        if (env != null && !env.isBlank()) {
            return env;
        }

        String systemProperty = System.getProperty(systemKey);
        if (systemProperty != null && !systemProperty.isBlank()) {
            return systemProperty;
        }

        return fallback;
    }
}
