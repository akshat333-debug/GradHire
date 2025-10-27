package com.gradhire.exception;

/**
 * Custom exception for data access layer errors
 * Wraps underlying SQLException and provides meaningful context
 * 
 * @author GradHire Team
 * @version 1.0
 */
public class DataAccessException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructs a new DataAccessException with the specified detail message
     * 
     * @param message the detail message
     */
    public DataAccessException(String message) {
        super(message);
    }
    
    /**
     * Constructs a new DataAccessException with the specified detail message and cause
     * 
     * @param message the detail message
     * @param cause the cause
     */
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Constructs a new DataAccessException with the specified cause
     * 
     * @param cause the cause
     */
    public DataAccessException(Throwable cause) {
        super(cause);
    }
}



