package com.gradhire.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * Job Entity Class
 * Represents a job or internship listing in the GradHire system
 * 
 * @author GradHire Team
 * @version 1.0
 */
public class Job {
    
    private Integer jobId;
    private Integer adminId;
    private String jobTitle;
    private String companyName;
    private String jobType; // 'Internship', 'Full-time', 'Part-time', 'Contract'
    private String domain;
    private String description;
    private String requirements;
    private String responsibilities;
    private String location;
    private Boolean isRemote;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private Date applicationDeadline;
    private String jobStatus; // 'Active', 'Closed', 'Draft'
    private Integer viewsCount;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Additional fields (not in database, populated by joins)
    private List<Skill> requiredSkills;
    private List<Skill> optionalSkills;
    private Integer applicationCount;
    private Admin recruiter;
    private Integer matchPercentage; // For job recommendations
    
    /**
     * Default constructor
     */
    public Job() {
        this.isRemote = false;
        this.jobStatus = "Draft";
        this.viewsCount = 0;
    }
    
    /**
     * Constructor with essential fields
     */
    public Job(Integer adminId, String jobTitle, String companyName, String jobType) {
        this.adminId = adminId;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.jobType = jobType;
        this.isRemote = false;
        this.jobStatus = "Draft";
        this.viewsCount = 0;
    }
    
    // Getters and Setters
    
    public Integer getJobId() {
        return jobId;
    }
    
    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }
    
    public Integer getAdminId() {
        return adminId;
    }
    
    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }
    
    public String getJobTitle() {
        return jobTitle;
    }
    
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }
    
    public String getCompanyName() {
        return companyName;
    }
    
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    
    public String getJobType() {
        return jobType;
    }
    
    public void setJobType(String jobType) {
        this.jobType = jobType;
    }
    
    public String getDomain() {
        return domain;
    }
    
    public void setDomain(String domain) {
        this.domain = domain;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getRequirements() {
        return requirements;
    }
    
    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }
    
    public String getResponsibilities() {
        return responsibilities;
    }
    
    public void setResponsibilities(String responsibilities) {
        this.responsibilities = responsibilities;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public Boolean getIsRemote() {
        return isRemote;
    }
    
    public void setIsRemote(Boolean isRemote) {
        this.isRemote = isRemote;
    }
    
    public BigDecimal getSalaryMin() {
        return salaryMin;
    }
    
    public void setSalaryMin(BigDecimal salaryMin) {
        this.salaryMin = salaryMin;
    }
    
    public BigDecimal getSalaryMax() {
        return salaryMax;
    }
    
    public void setSalaryMax(BigDecimal salaryMax) {
        this.salaryMax = salaryMax;
    }
    
    public Date getApplicationDeadline() {
        return applicationDeadline;
    }
    
    public void setApplicationDeadline(Date applicationDeadline) {
        this.applicationDeadline = applicationDeadline;
    }
    
    public String getJobStatus() {
        return jobStatus;
    }
    
    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }
    
    public Integer getViewsCount() {
        return viewsCount;
    }
    
    public void setViewsCount(Integer viewsCount) {
        this.viewsCount = viewsCount;
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
    
    public List<Skill> getRequiredSkills() {
        return requiredSkills;
    }
    
    public void setRequiredSkills(List<Skill> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }
    
    public List<Skill> getOptionalSkills() {
        return optionalSkills;
    }
    
    public void setOptionalSkills(List<Skill> optionalSkills) {
        this.optionalSkills = optionalSkills;
    }
    
    public Integer getApplicationCount() {
        return applicationCount;
    }
    
    public void setApplicationCount(Integer applicationCount) {
        this.applicationCount = applicationCount;
    }
    
    public Admin getRecruiter() {
        return recruiter;
    }
    
    public void setRecruiter(Admin recruiter) {
        this.recruiter = recruiter;
    }
    
    public Integer getMatchPercentage() {
        return matchPercentage;
    }
    
    public void setMatchPercentage(Integer matchPercentage) {
        this.matchPercentage = matchPercentage;
    }
    
    // ========================================
    // ALIAS METHODS & ADDITIONAL FIELDS FOR SERVLET COMPATIBILITY
    // ========================================
    
    // Additional fields (not in database - for UI/servlet use)
    private String benefits;
    private String experienceLevel;
    private String educationLevel;
    private String contactEmail;
    private String contactPhone;
    private Integer positionsAvailable;
    private Boolean hasApplied; // For UI to show if student has applied
    
    // Alias for jobTitle
    public String getTitle() { return jobTitle; }
    public void setTitle(String title) { this.jobTitle = title; }
    
    // Alias for companyName
    public String getCompany() { return companyName; }
    public void setCompany(String company) { this.companyName = company; }
    
    // Additional getters/setters
    public String getBenefits() { return benefits; }
    public void setBenefits(String benefits) { this.benefits = benefits; }
    
    public String getExperienceLevel() { return experienceLevel; }
    public void setExperienceLevel(String experienceLevel) { this.experienceLevel = experienceLevel; }
    
    public String getEducationLevel() { return educationLevel; }
    public void setEducationLevel(String educationLevel) { this.educationLevel = educationLevel; }
    
    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    
    public String getContactPhone() { return contactPhone; }
    public void setContactPhone(String contactPhone) { this.contactPhone = contactPhone; }
    
    public Integer getPositionsAvailable() { return positionsAvailable; }
    public void setPositionsAvailable(Integer positionsAvailable) { this.positionsAvailable = positionsAvailable; }
    
    public Boolean getHasApplied() { return hasApplied; }
    public void setHasApplied(Boolean hasApplied) { this.hasApplied = hasApplied; }
    public boolean hasApplied() { return Boolean.TRUE.equals(hasApplied); }
    
    // Alias for jobStatus active check
    public void setIsActive(Boolean active) {
        this.jobStatus = active ? "Active" : "Draft";
    }
    
    public Boolean getIsActive() {
        return "Active".equals(jobStatus);
    }
    
    /**
     * Check if job is active
     */
    public boolean isActive() {
        return "Active".equals(jobStatus);
    }
    
    /**
     * Check if job is closed
     */
    public boolean isClosed() {
        return "Closed".equals(jobStatus);
    }
    
    /**
     * Check if job is a draft
     */
    public boolean isDraft() {
        return "Draft".equals(jobStatus);
    }
    
    /**
     * Check if application deadline has passed
     */
    public boolean isDeadlinePassed() {
        if (applicationDeadline == null) {
            return false;
        }
        return applicationDeadline.before(new Date(System.currentTimeMillis()));
    }
    
    /**
     * Get days until deadline
     */
    public long getDaysUntilDeadline() {
        if (applicationDeadline == null) {
            return -1;
        }
        long diff = applicationDeadline.getTime() - System.currentTimeMillis();
        return diff / (1000 * 60 * 60 * 24);
    }
    
    /**
     * Get salary range as string
     */
    public String getSalaryRange() {
        if (salaryMin == null && salaryMax == null) {
            return "Not specified";
        }
        if (salaryMin == null) {
            return "Up to $" + salaryMax;
        }
        if (salaryMax == null) {
            return "$" + salaryMin + "+";
        }
        return "$" + salaryMin + " - $" + salaryMax;
    }
    
    /**
     * Get location display string
     */
    public String getLocationDisplay() {
        if (isRemote != null && isRemote) {
            return location != null ? location + " (Remote)" : "Remote";
        }
        return location != null ? location : "Not specified";
    }
    
    /**
     * Increment view count
     */
    public void incrementViews() {
        if (viewsCount == null) {
            viewsCount = 1;
        } else {
            viewsCount++;
        }
    }
    
    @Override
    public String toString() {
        return "Job{" +
                "jobId=" + jobId +
                ", jobTitle='" + jobTitle + '\'' +
                ", companyName='" + companyName + '\'' +
                ", jobType='" + jobType + '\'' +
                ", location='" + location + '\'' +
                ", isRemote=" + isRemote +
                ", jobStatus='" + jobStatus + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Job job = (Job) o;
        return jobId != null && jobId.equals(job.jobId);
    }
    
    @Override
    public int hashCode() {
        return jobId != null ? jobId.hashCode() : 0;
    }
}

