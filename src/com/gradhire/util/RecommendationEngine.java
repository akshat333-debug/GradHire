package com.gradhire.util;

import com.gradhire.model.Job;
import com.gradhire.model.Skill;
import com.gradhire.model.Student;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * RecommendationEngine provides intelligent job matching using skill-based algorithms.
 * 
 * This class implements multiple recommendation strategies:
 * 1. Jaccard Similarity - Set-based similarity measure
 * 2. Keyword Matching - Simple skill overlap
 * 3. Weighted Scoring - Combined approach with weights
 */
public class RecommendationEngine {
    
    private static final Logger logger = Logger.getLogger(RecommendationEngine.class.getName());
    
    // Scoring weights
    private static final double JACCARD_WEIGHT = 0.6;
    private static final double KEYWORD_WEIGHT = 0.4;
    
    // Minimum score threshold for recommendations (0.0 - 1.0)
    private static final double MIN_RECOMMENDATION_SCORE = 0.1;
    
    /**
     * Calculate Jaccard similarity between two sets of skills.
     * 
     * Jaccard Similarity = |A ∩ B| / |A ∪ B|
     * 
     * Where:
     * - A is the set of student skills
     * - B is the set of job required skills
     * - ∩ is intersection (common skills)
     * - ∪ is union (all unique skills)
     * 
     * Returns a value between 0.0 (no overlap) and 1.0 (perfect match)
     */
    public static double calculateJaccardSimilarity(Set<String> studentSkills, Set<String> jobSkills) {
        if (studentSkills == null || studentSkills.isEmpty() || 
            jobSkills == null || jobSkills.isEmpty()) {
            return 0.0;
        }
        
        // Convert to lowercase for case-insensitive comparison
        Set<String> studentSkillsLower = studentSkills.stream()
            .map(String::toLowerCase)
            .collect(Collectors.toSet());
        
        Set<String> jobSkillsLower = jobSkills.stream()
            .map(String::toLowerCase)
            .collect(Collectors.toSet());
        
        // Calculate intersection (common skills)
        Set<String> intersection = new HashSet<>(studentSkillsLower);
        intersection.retainAll(jobSkillsLower);
        
        // Calculate union (all unique skills)
        Set<String> union = new HashSet<>(studentSkillsLower);
        union.addAll(jobSkillsLower);
        
        // Jaccard similarity = intersection / union
        double similarity = (double) intersection.size() / union.size();
        
        logger.fine(String.format("Jaccard Similarity: %.2f (Intersection: %d, Union: %d)", 
                   similarity, intersection.size(), union.size()));
        
        return similarity;
    }
    
    /**
     * Calculate keyword match score based on skill overlap.
     * 
     * This is a simpler approach that counts how many of the job's required
     * skills the student has, normalized by the total required skills.
     * 
     * Score = (# of matching skills) / (# of job required skills)
     * 
     * Returns a value between 0.0 (no matches) and 1.0 (all skills matched)
     */
    public static double calculateKeywordMatchScore(Set<String> studentSkills, Set<String> jobSkills) {
        if (jobSkills == null || jobSkills.isEmpty()) {
            return 0.0;
        }
        
        if (studentSkills == null || studentSkills.isEmpty()) {
            return 0.0;
        }
        
        // Convert to lowercase for case-insensitive comparison
        Set<String> studentSkillsLower = studentSkills.stream()
            .map(String::toLowerCase)
            .collect(Collectors.toSet());
        
        Set<String> jobSkillsLower = jobSkills.stream()
            .map(String::toLowerCase)
            .collect(Collectors.toSet());
        
        // Count matching skills
        long matchCount = jobSkillsLower.stream()
            .filter(studentSkillsLower::contains)
            .count();
        
        // Calculate match percentage
        double score = (double) matchCount / jobSkillsLower.size();
        
        logger.fine(String.format("Keyword Match Score: %.2f (%d/%d skills matched)", 
                   score, matchCount, jobSkillsLower.size()));
        
        return score;
    }
    
    /**
     * Calculate a combined recommendation score using both Jaccard similarity
     * and keyword matching with weighted averaging.
     * 
     * Combined Score = (Jaccard × weight1) + (Keyword × weight2)
     */
    public static double calculateCombinedScore(Set<String> studentSkills, Set<String> jobSkills) {
        double jaccardScore = calculateJaccardSimilarity(studentSkills, jobSkills);
        double keywordScore = calculateKeywordMatchScore(studentSkills, jobSkills);
        
        double combinedScore = (jaccardScore * JACCARD_WEIGHT) + (keywordScore * KEYWORD_WEIGHT);
        
        logger.fine(String.format("Combined Score: %.2f (Jaccard: %.2f, Keyword: %.2f)", 
                   combinedScore, jaccardScore, keywordScore));
        
        return combinedScore;
    }
    
    /**
     * Rank jobs for a student based on skill matching.
     * 
     * This method calculates a recommendation score for each job and sorts them
     * by score in descending order (best matches first).
     * 
     * @param student The student to get recommendations for
     * @param jobs List of available jobs
     * @return List of jobs sorted by recommendation score (best first)
     */
    public static List<JobRecommendation> rankJobs(Student student, List<Job> jobs) {
        if (student == null || jobs == null || jobs.isEmpty()) {
            return new ArrayList<>();
        }
        
        // Get student skills as a set
        Set<String> studentSkills = getStudentSkillSet(student);
        
        if (studentSkills.isEmpty()) {
            logger.warning("Student has no skills, returning jobs without scoring");
            // Return jobs with zero scores if student has no skills
            return jobs.stream()
                .map(job -> new JobRecommendation(job, 0.0, new HashSet<>()))
                .collect(Collectors.toList());
        }
        
        // Calculate score for each job
        List<JobRecommendation> recommendations = new ArrayList<>();
        
        for (Job job : jobs) {
            Set<String> jobSkills = getJobSkillSet(job);
            
            if (jobSkills.isEmpty()) {
                // Job has no required skills, give it a low score
                recommendations.add(new JobRecommendation(job, 0.1, new HashSet<>()));
                continue;
            }
            
            // Calculate combined score
            double score = calculateCombinedScore(studentSkills, jobSkills);
            
            // Find matching skills for display
            Set<String> matchingSkills = findMatchingSkills(studentSkills, jobSkills);
            
            // Only include jobs that meet minimum threshold
            if (score >= MIN_RECOMMENDATION_SCORE) {
                recommendations.add(new JobRecommendation(job, score, matchingSkills));
            }
        }
        
        // Sort by score (highest first)
        recommendations.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));
        
        logger.info(String.format("Ranked %d jobs for student %d", 
                   recommendations.size(), student.getStudentId()));
        
        return recommendations;
    }
    
    /**
     * Get top N job recommendations for a student.
     */
    public static List<JobRecommendation> getTopRecommendations(Student student, List<Job> jobs, int topN) {
        List<JobRecommendation> allRecommendations = rankJobs(student, jobs);
        
        // Return top N
        return allRecommendations.stream()
            .limit(topN)
            .collect(Collectors.toList());
    }
    
    /**
     * Find skills that match between student and job.
     */
    private static Set<String> findMatchingSkills(Set<String> studentSkills, Set<String> jobSkills) {
        Set<String> studentSkillsLower = studentSkills.stream()
            .map(String::toLowerCase)
            .collect(Collectors.toSet());
        
        return jobSkills.stream()
            .filter(skill -> studentSkillsLower.contains(skill.toLowerCase()))
            .collect(Collectors.toSet());
    }
    
    /**
     * Extract skill names from student's skill list.
     */
    private static Set<String> getStudentSkillSet(Student student) {
        if (student.getSkills() == null || student.getSkills().isEmpty()) {
            return new HashSet<>();
        }
        
        return student.getSkills().stream()
            .map(Skill::getSkillName)
            .filter(name -> name != null && !name.trim().isEmpty())
            .collect(Collectors.toSet());
    }
    
    /**
     * Extract skill names from job's required skills.
     */
    private static Set<String> getJobSkillSet(Job job) {
        if (job.getRequiredSkills() == null || job.getRequiredSkills().isEmpty()) {
            return new HashSet<>();
        }
        
        return job.getRequiredSkills().stream()
            .map(Skill::getSkillName)
            .filter(name -> name != null && !name.trim().isEmpty())
            .collect(Collectors.toSet());
    }
    
    /**
     * JobRecommendation class to hold a job and its recommendation score.
     */
    public static class JobRecommendation {
        private final Job job;
        private final double score;
        private final Set<String> matchingSkills;
        
        public JobRecommendation(Job job, double score, Set<String> matchingSkills) {
            this.job = job;
            this.score = score;
            this.matchingSkills = matchingSkills;
        }
        
        public Job getJob() {
            return job;
        }
        
        public double getScore() {
            return score;
        }
        
        public Set<String> getMatchingSkills() {
            return matchingSkills;
        }
        
        /**
         * Get score as percentage (0-100)
         */
        public int getScorePercentage() {
            return (int) Math.round(score * 100);
        }
        
        /**
         * Get match quality label
         */
        public String getMatchQuality() {
            if (score >= 0.8) return "Excellent Match";
            if (score >= 0.6) return "Great Match";
            if (score >= 0.4) return "Good Match";
            if (score >= 0.2) return "Fair Match";
            return "Possible Match";
        }
        
        @Override
        public String toString() {
            return String.format("JobRecommendation{job=%s, score=%.2f, matchingSkills=%d}", 
                               job.getJobTitle(), score, matchingSkills.size());
        }
    }
}

