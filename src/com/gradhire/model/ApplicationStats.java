package com.gradhire.model;

/**
 * Application Statistics Data Class
 * Holds count statistics for different application statuses
 * Provides better type safety than using primitive arrays
 * 
 * @author GradHire Team
 * @version 1.0
 */
public class ApplicationStats {
    
    private int pending;
    private int reviewed;
    private int shortlisted;
    private int accepted;
    private int rejected;
    
    /**
     * Default constructor
     */
    public ApplicationStats() {
        this.pending = 0;
        this.reviewed = 0;
        this.shortlisted = 0;
        this.accepted = 0;
        this.rejected = 0;
    }
    
    /**
     * Constructor with all fields
     */
    public ApplicationStats(int pending, int reviewed, int shortlisted, int accepted, int rejected) {
        this.pending = pending;
        this.reviewed = reviewed;
        this.shortlisted = shortlisted;
        this.accepted = accepted;
        this.rejected = rejected;
    }
    
    // Getters and Setters
    
    public int getPending() {
        return pending;
    }
    
    public void setPending(int pending) {
        this.pending = pending;
    }
    
    public int getReviewed() {
        return reviewed;
    }
    
    public void setReviewed(int reviewed) {
        this.reviewed = reviewed;
    }
    
    public int getShortlisted() {
        return shortlisted;
    }
    
    public void setShortlisted(int shortlisted) {
        this.shortlisted = shortlisted;
    }
    
    public int getAccepted() {
        return accepted;
    }
    
    public void setAccepted(int accepted) {
        this.accepted = accepted;
    }
    
    public int getRejected() {
        return rejected;
    }
    
    public void setRejected(int rejected) {
        this.rejected = rejected;
    }
    
    /**
     * Get total count of all applications
     * @return Sum of all status counts
     */
    public int getTotal() {
        return pending + reviewed + shortlisted + accepted + rejected;
    }
    
    /**
     * Get count for a specific status
     * @param status ApplicationStatus enum
     * @return Count for that status
     */
    public int getCountByStatus(ApplicationStatus status) {
        if (status == null) {
            return 0;
        }
        switch (status) {
            case PENDING:
                return pending;
            case REVIEWED:
                return reviewed;
            case SHORTLISTED:
                return shortlisted;
            case ACCEPTED:
                return accepted;
            case REJECTED:
                return rejected;
            default:
                return 0;
        }
    }
    
    @Override
    public String toString() {
        return "ApplicationStats{" +
                "pending=" + pending +
                ", reviewed=" + reviewed +
                ", shortlisted=" + shortlisted +
                ", accepted=" + accepted +
                ", rejected=" + rejected +
                ", total=" + getTotal() +
                '}';
    }
}




