package com.gradhire.model;

import java.sql.Timestamp;
import java.util.List;

/**
 * Student Entity Class
 * Represents a student user in the GradHire system
 * 
 * @author GradHire Team
 * @version 1.0
 */
public class Student {
    
    private Integer studentId;
    private String email;
    private String passwordHash;
    private String fullName;
    private String phone;
    private String collegeName;
    private String degree;
    private Integer graduationYear;
    private String resumePath;
    private String profilePicture;
    private String bio;
    private String linkedinUrl;
    private String githubUrl;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Boolean isActive;
    
    // Additional fields (not in database, populated by joins)
    private List<Skill> skills;
    private Integer applicationCount;
    private Integer savedJobCount;
    
    /**
     * Default constructor
     */
    public Student() {
        this.isActive = true;
    }
    
    /**
     * Constructor with essential fields
     */
    public Student(String email, String passwordHash, String fullName) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.isActive = true;
    }
    
    // Getters and Setters
    
    public Integer getStudentId() {
        return studentId;
    }
    
    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
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
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getCollegeName() {
        return collegeName;
    }
    
    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }
    
    public String getDegree() {
        return degree;
    }
    
    public void setDegree(String degree) {
        this.degree = degree;
    }
    
    public Integer getGraduationYear() {
        return graduationYear;
    }
    
    public void setGraduationYear(Integer graduationYear) {
        this.graduationYear = graduationYear;
    }
    
    public String getResumePath() {
        return resumePath;
    }
    
    public void setResumePath(String resumePath) {
        this.resumePath = resumePath;
    }
    
    public String getProfilePicture() {
        return profilePicture;
    }
    
    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
    
    public String getBio() {
        return bio;
    }
    
    public void setBio(String bio) {
        this.bio = bio;
    }
    
    public String getLinkedinUrl() {
        return linkedinUrl;
    }
    
    public void setLinkedinUrl(String linkedinUrl) {
        this.linkedinUrl = linkedinUrl;
    }
    
    public String getGithubUrl() {
        return githubUrl;
    }
    
    public void setGithubUrl(String githubUrl) {
        this.githubUrl = githubUrl;
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
    
    public List<Skill> getSkills() {
        return skills;
    }
    
    public void setSkills(List<Skill> skills) {
        this.skills = skills;
    }
    
    public Integer getApplicationCount() {
        return applicationCount;
    }
    
    public void setApplicationCount(Integer applicationCount) {
        this.applicationCount = applicationCount;
    }
    
    public Integer getSavedJobCount() {
        return savedJobCount;
    }
    
    public void setSavedJobCount(Integer savedJobCount) {
        this.savedJobCount = savedJobCount;
    }
    
    // ========================================
    // ALIAS METHODS FOR SERVLET COMPATIBILITY
    // ========================================
    
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
    
    /**
     * Set first name (updates full name)
     */
    public void setFirstName(String firstName) {
        if (firstName != null) {
            String lastName = getLastName();
            this.fullName = lastName.isEmpty() ? firstName : firstName + " " + lastName;
        }
    }
    
    /**
     * Get last name from full name
     */
    public String getLastName() {
        if (fullName == null || fullName.isEmpty()) {
            return "";
        }
        String[] parts = fullName.split("\\s+");
        return parts.length > 1 ? parts[parts.length - 1] : "";
    }
    
    /**
     * Set last name (updates full name)
     */
    public void setLastName(String lastName) {
        if (lastName != null) {
            String firstName = getFirstName();
            this.fullName = firstName.isEmpty() ? lastName : firstName + " " + lastName;
        }
    }
    
    // Alias for collegeName
    public String getUniversity() { return collegeName; }
    public void setUniversity(String university) { this.collegeName = university; }
    
    // Alias for degree
    public String getMajor() { return degree; }
    public void setMajor(String major) { this.degree = major; }
    
    // Additional fields (not in database - for UI only)
    private String minor;
    private String degreeLevel;
    private String portfolio;
    private java.math.BigDecimal gpa;
    
    public String getMinor() { return minor; }
    public void setMinor(String minor) { this.minor = minor; }
    
    public String getDegreeLevel() { return degreeLevel; }
    public void setDegreeLevel(String degreeLevel) { this.degreeLevel = degreeLevel; }
    
    public String getPortfolio() { return portfolio; }
    public void setPortfolio(String portfolio) { this.portfolio = portfolio; }
    
    public java.math.BigDecimal getGpa() { return gpa; }
    public void setGpa(java.math.BigDecimal gpa) { this.gpa = gpa; }
    
    // Alias for resumePath
    public String getResume() { return resumePath; }
    public void setResume(String resume) { this.resumePath = resume; }
    
    // Alias for linkedinUrl
    public String getLinkedIn() { return linkedinUrl; }
    public void setLinkedIn(String linkedIn) { this.linkedinUrl = linkedIn; }
    
    // Alias for githubUrl
    public String getGitHub() { return githubUrl; }
    public void setGitHub(String gitHub) { this.githubUrl = gitHub; }
    
    /**
     * Check if profile is complete
     */
    public boolean isProfileComplete() {
        return fullName != null && !fullName.isEmpty() &&
               collegeName != null && !collegeName.isEmpty() &&
               degree != null && !degree.isEmpty() &&
               graduationYear != null &&
               bio != null && !bio.isEmpty() &&
               skills != null && !skills.isEmpty();
    }
    
    /**
     * Get profile completion percentage
     */
    public int getProfileCompletionPercentage() {
        int completed = 0;
        int total = 10;
        
        if (fullName != null && !fullName.isEmpty()) completed++;
        if (email != null && !email.isEmpty()) completed++;
        if (collegeName != null && !collegeName.isEmpty()) completed++;
        if (degree != null && !degree.isEmpty()) completed++;
        if (graduationYear != null) completed++;
        if (bio != null && !bio.isEmpty()) completed++;
        if (resumePath != null && !resumePath.isEmpty()) completed++;
        if (phone != null && !phone.isEmpty()) completed++;
        if (linkedinUrl != null && !linkedinUrl.isEmpty()) completed++;
        if (skills != null && !skills.isEmpty()) completed++;
        
        return (completed * 100) / total;
    }
    
    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", email='" + email + '\'' +
                ", fullName='" + fullName + '\'' +
                ", collegeName='" + collegeName + '\'' +
                ", degree='" + degree + '\'' +
                ", graduationYear=" + graduationYear +
                ", isActive=" + isActive +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return studentId != null && studentId.equals(student.studentId);
    }
    
    @Override
    public int hashCode() {
        return studentId != null ? studentId.hashCode() : 0;
    }
}

