package com.gradhire.model;

/**
 * Enumeration for Application Status
 * Ensures consistency of status values across the application
 * 
 * @author GradHire Team
 * @version 1.0
 */
public enum ApplicationStatus {
    PENDING("Pending"),
    REVIEWED("Reviewed"),
    SHORTLISTED("Shortlisted"),
    ACCEPTED("Accepted"),
    REJECTED("Rejected");
    
    private final String value;
    
    /**
     * Constructor
     * @param value String representation of the status
     */
    ApplicationStatus(String value) {
        this.value = value;
    }
    
    /**
     * Get the string value of the status
     * @return String representation
     */
    public String getValue() {
        return value;
    }
    
    /**
     * Get ApplicationStatus from string value
     * @param value String value
     * @return ApplicationStatus enum or null if not found
     */
    public static ApplicationStatus fromValue(String value) {
        if (value == null) {
            return null;
        }
        for (ApplicationStatus status : ApplicationStatus.values()) {
            if (status.value.equals(value)) {
                return status;
            }
        }
        return null;
    }
    
    /**
     * Check if a string is a valid status value
     * @param value String to check
     * @return true if valid status
     */
    public static boolean isValidStatus(String value) {
        return fromValue(value) != null;
    }
    
    @Override
    public String toString() {
        return value;
    }
}




