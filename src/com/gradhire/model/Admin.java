package com.gradhire.model;

import java.sql.Timestamp;

/**
 * Admin/Recruiter Entity Class
 * Represents an admin or recruiter user in the GradHire system
 * 
 * @author GradHire Team
 * @version 1.0
 */
public class Admin {
    
    private Integer adminId;
    private String email;
    private String passwordHash;
    private String fullName;
    private String companyName;
    private String companyWebsite;
    private String phone;
    private String role; // 'admin' or 'recruiter'
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Boolean isActive;
    
    // Additional fields (not in database)
    private Integer jobPostCount;
    private Integer applicationReceivedCount;
    
    /**
     * Default constructor
     */
    public Admin() {
        this.role = "recruiter";
        this.isActive = true;
    }
    
    /**
     * Constructor with essential fields
     */
    public Admin(String email, String passwordHash, String fullName, String companyName) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.companyName = companyName;
        this.role = "recruiter";
        this.isActive = true;
    }
    
    // Getters and Setters
    
    public Integer getAdminId() {
        return adminId;
    }
    
    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPasswordHash() {
        return passwordHash;
    }
    
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public String getCompanyWebsite() {
        return companyWebsite;
    }
    
    public void setCompanyWebsite(String companyWebsite) {
        this.companyWebsite = companyWebsite;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public Integer getJobPostCount() {
        return jobPostCount;
    }
    
    public void setJobPostCount(Integer jobPostCount) {
        this.jobPostCount = jobPostCount;
    }
    
    public Integer getApplicationReceivedCount() {
        return applicationReceivedCount;
    }
    
    public void setApplicationReceivedCount(Integer applicationReceivedCount) {
        this.applicationReceivedCount = applicationReceivedCount;
    }
    
    /**
     * Check if user is a super admin
     */
    public boolean isSuperAdmin() {
        return "admin".equalsIgnoreCase(role);
    }
    
    /**
     * Check if user is a recruiter
     */
    public boolean isRecruiter() {
        return "recruiter".equalsIgnoreCase(role);
    }
    
    // ========================================
    // ALIAS METHODS FOR SERVLET COMPATIBILITY
    // ========================================
    
    // Alias for fullName
    public String getName() { return fullName; }
    public void setName(String name) { this.fullName = name; }
    
    // Alias for companyName
    public String getCompany() { return companyName; }
    public void setCompany(String company) { this.companyName = company; }
    
    /**
     * Get first name from full name
     */
    public String getFirstName() {
        if (fullName == null || fullName.isEmpty()) {
            return "";
        }
        String[] parts = fullName.split("\\s+");
        return parts[0];
    }
    
    @Override
    public String toString() {
        return "Admin{" +
                "adminId=" + adminId +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", companyName='" + companyName + '\'' +
                ", role='" + role + '\'' +
                ", isActive=" + isActive +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Admin admin = (Admin) o;
        return adminId != null && adminId.equals(admin.adminId);
    }
    
    @Override
    public int hashCode() {
        return adminId != null ? adminId.hashCode() : 0;
    }
}

