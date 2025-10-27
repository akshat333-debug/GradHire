package com.gradhire.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 * Database Connection Manager
 * Handles database connections using Apache Commons DBCP2 connection pooling
 * 
 * Features:
 * - Connection pooling for performance
 * - Automatic connection retry
 * - Resource cleanup
 * - Configuration from properties file
 * 
 * @author GradHire Team
 * @version 1.0
 */
public class DBConnection {
    
    private static BasicDataSource dataSource;
    private static Properties properties;
    private static final String PROPERTIES_FILE = "/database.properties";
    
    // Static block to initialize the data source
    static {
        try {
            loadProperties();
            initializeDataSource();
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Failed to initialize database connection pool: " + e.getMessage());
        }
    }
    
    /**
     * Load database properties from configuration file
     */
    private static void loadProperties() throws IOException {
        properties = new Properties();
        InputStream input = DBConnection.class.getResourceAsStream(PROPERTIES_FILE);
        
        if (input == null) {
            // Fallback to default properties if file not found
            System.err.println("Warning: database.properties not found. Using default configuration.");
            setDefaultProperties();
        } else {
            properties.load(input);
            input.close();
        }
    }
    
    /**
     * Set default database properties (for development)
     */
    private static void setDefaultProperties() {
        properties.setProperty("db.url", "jdbc:mysql://localhost:3306/gradhire_db?useSSL=false&serverTimezone=UTC");
        properties.setProperty("db.username", "root");
        properties.setProperty("db.password", "");
        properties.setProperty("db.driver", "com.mysql.cj.jdbc.Driver");
        properties.setProperty("db.pool.initialSize", "5");
        properties.setProperty("db.pool.maxTotal", "20");
        properties.setProperty("db.pool.maxIdle", "10");
        properties.setProperty("db.pool.minIdle", "5");
    }
    
    /**
     * Initialize Apache Commons DBCP2 connection pool
     */
    private static void initializeDataSource() throws ClassNotFoundException {
        // Load JDBC driver
        String driver = properties.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");
        Class.forName(driver);
        
        // Create data source
        dataSource = new BasicDataSource();
        
        // Basic connection properties
        dataSource.setUrl(properties.getProperty("db.url"));
        dataSource.setUsername(properties.getProperty("db.username"));
        dataSource.setPassword(properties.getProperty("db.password"));
        dataSource.setDriverClassName(driver);
        
        // Pool configuration
        dataSource.setInitialSize(getIntProperty("db.pool.initialSize", 5));
        dataSource.setMaxTotal(getIntProperty("db.pool.maxTotal", 20));
        dataSource.setMaxIdle(getIntProperty("db.pool.maxIdle", 10));
        dataSource.setMinIdle(getIntProperty("db.pool.minIdle", 5));
        dataSource.setMaxWaitMillis(getIntProperty("db.pool.maxWaitMillis", 10000));
        
        // Connection validation
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setTestOnBorrow(true);
        dataSource.setTestOnReturn(false);
        dataSource.setTestWhileIdle(true);
        
        // Connection leak prevention
        dataSource.setRemoveAbandonedOnBorrow(true);
        dataSource.setRemoveAbandonedOnMaintenance(true);
        dataSource.setRemoveAbandonedTimeout(300); // 5 minutes
        dataSource.setLogAbandoned(true);
        
        System.out.println("Database connection pool initialized successfully.");
    }
    
    /**
     * Get an integer property with a default value
     */
    private static int getIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            System.err.println("Invalid integer value for " + key + ": " + value);
            return defaultValue;
        }
    }
    
    /**
     * Get a database connection from the pool
     * 
     * @return Connection object
     * @throws SQLException if connection cannot be obtained
     */
    public static Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("DataSource is not initialized");
        }
        return dataSource.getConnection();
    }
    
    /**
     * Close a database connection (returns it to the pool)
     * 
     * @param conn Connection to close
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Close a database connection with auto-closeable resources
     * 
     * @param resources AutoCloseable resources to close
     */
    public static void closeResources(AutoCloseable... resources) {
        for (AutoCloseable resource : resources) {
            if (resource != null) {
                try {
                    resource.close();
                } catch (Exception e) {
                    System.err.println("Error closing resource: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Get the data source (useful for advanced operations)
     * 
     * @return DataSource object
     */
    public static DataSource getDataSource() {
        return dataSource;
    }
    
    /**
     * Get connection pool statistics (for monitoring)
     * 
     * @return String with pool statistics
     */
    public static String getPoolStats() {
        if (dataSource == null) {
            return "DataSource not initialized";
        }
        
        return String.format(
            "Connection Pool Stats - Active: %d, Idle: %d, Total: %d, Max: %d",
            dataSource.getNumActive(),
            dataSource.getNumIdle(),
            dataSource.getNumActive() + dataSource.getNumIdle(),
            dataSource.getMaxTotal()
        );
    }
    
    /**
     * Test the database connection
     * 
     * @return true if connection is successful, false otherwise
     */
    public static boolean testConnection() {
        Connection conn = null;
        try {
            conn = getConnection();
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        } finally {
            closeConnection(conn);
        }
    }
    
    /**
     * Shutdown the connection pool (call on application shutdown)
     */
    public static void shutdown() {
        if (dataSource != null) {
            try {
                dataSource.close();
                System.out.println("Database connection pool closed successfully.");
            } catch (SQLException e) {
                System.err.println("Error closing connection pool: " + e.getMessage());
            }
        }
    }
}

