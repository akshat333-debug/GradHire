package com.gradhire.model;

import java.sql.Timestamp;

/**
 * Application Entity Class
 * Represents a student's job application in the GradHire system
 * 
 * @author GradHire Team
 * @version 1.0
 */
public class Application {
    
    private Integer applicationId;
    private Integer jobId;
    private Integer studentId;
    private String coverLetter;
    private String applicationStatus; // 'Pending', 'Reviewed', 'Shortlisted', 'Rejected', 'Accepted'
    private Timestamp appliedAt;
    private Timestamp reviewedAt;
    private String reviewerNotes;
    private Timestamp deletedAt;  // For soft delete functionality
    
    // Additional fields (not in database, populated by joins)
    private Job job;
    private Student student;
    
    /**
     * Default constructor
     */
    public Application() {
        this.applicationStatus = ApplicationStatus.PENDING.getValue();
    }
    
    /**
     * Constructor with essential fields
     */
    public Application(Integer jobId, Integer studentId, String coverLetter) {
        this.jobId = jobId;
        this.studentId = studentId;
        this.coverLetter = coverLetter;
        this.applicationStatus = ApplicationStatus.PENDING.getValue();
    }
    
    // Getters and Setters
    
    public Integer getApplicationId() {
        return applicationId;
    }
    
    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }
    
    public Integer getJobId() {
        return jobId;
    }
    
    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }
    
    public Integer getStudentId() {
        return studentId;
    }
    
    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }
    
    public String getCoverLetter() {
        return coverLetter;
    }
    
    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }
    
    public String getApplicationStatus() {
        return applicationStatus;
    }
    
    public void setApplicationStatus(String applicationStatus) {
        this.applicationStatus = applicationStatus;
    }
    
    public Timestamp getAppliedAt() {
        return appliedAt;
    }
    
    public void setAppliedAt(Timestamp appliedAt) {
        this.appliedAt = appliedAt;
    }
    
    public Timestamp getReviewedAt() {
        return reviewedAt;
    }
    
    public void setReviewedAt(Timestamp reviewedAt) {
        this.reviewedAt = reviewedAt;
    }
    
    public String getReviewerNotes() {
        return reviewerNotes;
    }
    
    public void setReviewerNotes(String reviewerNotes) {
        this.reviewerNotes = reviewerNotes;
    }
    
    public Timestamp getDeletedAt() {
        return deletedAt;
    }
    
    public void setDeletedAt(Timestamp deletedAt) {
        this.deletedAt = deletedAt;
    }
    
    public Job getJob() {
        return job;
    }
    
    public void setJob(Job job) {
        this.job = job;
    }
    
    public Student getStudent() {
        return student;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }
    
    /**
     * Check if application is pending
     */
    public boolean isPending() {
        return ApplicationStatus.PENDING.getValue().equals(applicationStatus);
    }
    
    /**
     * Check if application has been reviewed
     */
    public boolean isReviewed() {
        return ApplicationStatus.REVIEWED.getValue().equals(applicationStatus);
    }
    
    /**
     * Check if application is shortlisted
     */
    public boolean isShortlisted() {
        return ApplicationStatus.SHORTLISTED.getValue().equals(applicationStatus);
    }
    
    /**
     * Check if application is rejected
     */
    public boolean isRejected() {
        return ApplicationStatus.REJECTED.getValue().equals(applicationStatus);
    }
    
    /**
     * Check if application is accepted
     */
    public boolean isAccepted() {
        return ApplicationStatus.ACCEPTED.getValue().equals(applicationStatus);
    }
    
    /**
     * Get status badge CSS class for UI
     */
    public String getStatusBadgeClass() {
        if (applicationStatus == null) {
            return "badge-secondary";
        }
        
        switch (applicationStatus) {
            case "Pending":  // ApplicationStatus.PENDING.getValue()
                return "badge-warning";
            case "Reviewed":  // ApplicationStatus.REVIEWED.getValue()
                return "badge-info";
            case "Shortlisted":  // ApplicationStatus.SHORTLISTED.getValue()
                return "badge-primary";
            case "Accepted":  // ApplicationStatus.ACCEPTED.getValue()
                return "badge-success";
            case "Rejected":  // ApplicationStatus.REJECTED.getValue()
                return "badge-danger";
            default:
                return "badge-secondary";
        }
    }
    
    /**
     * Get days since application
     */
    public long getDaysSinceApplied() {
        if (appliedAt == null) {
            return 0;
        }
        long diff = System.currentTimeMillis() - appliedAt.getTime();
        return diff / (1000 * 60 * 60 * 24);
    }
    
    /**
     * Get days since review
     */
    public long getDaysSinceReviewed() {
        if (reviewedAt == null) {
            return 0;
        }
        long diff = System.currentTimeMillis() - reviewedAt.getTime();
        return diff / (1000 * 60 * 60 * 24);
    }
    
    /**
     * Check if application is soft-deleted
     */
    public boolean isDeleted() {
        return deletedAt != null;
    }
    
    /**
     * Check if application can be withdrawn (pending or reviewed only, and not deleted)
     */
    public boolean canWithdraw() {
        return !isDeleted() && (isPending() || isReviewed());
    }
    
    /**
     * Format applied date for display
     */
    public String getAppliedDateFormatted() {
        if (appliedAt == null) {
            return "N/A";
        }
        return new java.text.SimpleDateFormat("MMM dd, yyyy").format(appliedAt);
    }
    
    @Override
    public String toString() {
        return "Application{" +
                "applicationId=" + applicationId +
                ", jobId=" + jobId +
                ", studentId=" + studentId +
                ", applicationStatus='" + applicationStatus + '\'' +
                ", appliedAt=" + appliedAt +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Application that = (Application) o;
        return applicationId != null && applicationId.equals(that.applicationId);
    }
    
    @Override
    public int hashCode() {
        return applicationId != null ? applicationId.hashCode() : 0;
    }
}

