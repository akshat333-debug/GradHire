package com.gradhire.model;

import java.sql.Timestamp;

/**
 * Skill Entity Class
 * Represents a skill or technology in the GradHire system
 * 
 * @author GradHire Team
 * @version 1.0
 */
public class Skill {
    
    private Integer skillId;
    private String skillName;
    private String category;
    private Timestamp createdAt;
    
    // Additional fields (for student_skills and job_skills relationships)
    private String proficiencyLevel; // For student skills: 'Beginner', 'Intermediate', 'Advanced', 'Expert'
    private Boolean isRequired; // For job skills: true for required, false for optional
    
    /**
     * Default constructor
     */
    public Skill() {
    }
    
    /**
     * Constructor with skill name
     */
    public Skill(String skillName) {
        this.skillName = skillName;
    }
    
    /**
     * Constructor with skill name and category
     */
    public Skill(String skillName, String category) {
        this.skillName = skillName;
        this.category = category;
    }
    
    // Getters and Setters
    
    public Integer getSkillId() {
        return skillId;
    }
    
    public void setSkillId(Integer skillId) {
        this.skillId = skillId;
    }
    
    public String getSkillName() {
        return skillName;
    }
    
    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getProficiencyLevel() {
        return proficiencyLevel;
    }
    
    public void setProficiencyLevel(String proficiencyLevel) {
        this.proficiencyLevel = proficiencyLevel;
    }
    
    public Boolean getIsRequired() {
        return isRequired;
    }
    
    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }
    
    /**
     * Get proficiency level as numeric value for sorting
     * Beginner=1, Intermediate=2, Advanced=3, Expert=4
     */
    public int getProficiencyValue() {
        if (proficiencyLevel == null) {
            return 0;
        }
        switch (proficiencyLevel) {
            case "Beginner": return 1;
            case "Intermediate": return 2;
            case "Advanced": return 3;
            case "Expert": return 4;
            default: return 0;
        }
    }
    
    /**
     * Get CSS class for proficiency level badge
     */
    public String getProficiencyBadgeClass() {
        if (proficiencyLevel == null) {
            return "badge-secondary";
        }
        switch (proficiencyLevel) {
            case "Beginner": return "badge-info";
            case "Intermediate": return "badge-primary";
            case "Advanced": return "badge-warning";
            case "Expert": return "badge-success";
            default: return "badge-secondary";
        }
    }
    
    /**
     * Get CSS class for category badge
     */
    public String getCategoryBadgeClass() {
        if (category == null) {
            return "badge-secondary";
        }
        switch (category) {
            case "Programming": return "badge-danger";
            case "Web Development": return "badge-primary";
            case "Database": return "badge-info";
            case "Data Science": return "badge-success";
            case "Cloud": return "badge-warning";
            case "DevOps": return "badge-dark";
            case "Design": return "badge-pink";
            case "Marketing": return "badge-purple";
            default: return "badge-secondary";
        }
    }
    
    @Override
    public String toString() {
        return "Skill{" +
                "skillId=" + skillId +
                ", skillName='" + skillName + '\'' +
                ", category='" + category + '\'' +
                ", proficiencyLevel='" + proficiencyLevel + '\'' +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill = (Skill) o;
        return skillId != null && skillId.equals(skill.skillId);
    }
    
    @Override
    public int hashCode() {
        return skillId != null ? skillId.hashCode() : 0;
    }
}

