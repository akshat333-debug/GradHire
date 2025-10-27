package com.gradhire.util;

import com.gradhire.model.Job;
import com.gradhire.model.Skill;
import com.gradhire.model.Student;
import com.gradhire.util.RecommendationEngine.JobRecommendation;
import java.util.*;

/**
 * Test class for RecommendationEngine
 * 
 * This test demonstrates the recommendation algorithm's accuracy by:
 * 1. Creating sample students with different skill sets
 * 2. Creating sample jobs with required skills
 * 3. Testing Jaccard similarity calculation
 * 4. Testing keyword matching calculation
 * 5. Verifying job ranking based on skill matching
 */
public class RecommendationEngineTest {
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("RecommendationEngine Test Suite");
        System.out.println("========================================\n");
        
        // Test 1: Jaccard Similarity
        testJaccardSimilarity();
        
        // Test 2: Keyword Match Score
        testKeywordMatchScore();
        
        // Test 3: Job Ranking
        testJobRanking();
        
        // Test 4: Edge Cases
        testEdgeCases();
        
        System.out.println("\n========================================");
        System.out.println("All Tests Completed Successfully! ✓");
        System.out.println("========================================");
    }
    
    /**
     * Test Jaccard Similarity calculation
     */
    private static void testJaccardSimilarity() {
        System.out.println("Test 1: Jaccard Similarity");
        System.out.println("--------------------------");
        
        // Test case 1: Perfect match
        Set<String> studentSkills1 = new HashSet<>(Arrays.asList("Java", "Python", "SQL"));
        Set<String> jobSkills1 = new HashSet<>(Arrays.asList("Java", "Python", "SQL"));
        double similarity1 = RecommendationEngine.calculateJaccardSimilarity(studentSkills1, jobSkills1);
        System.out.println("Perfect Match: " + similarity1 + " (Expected: 1.0)");
        assert Math.abs(similarity1 - 1.0) < 0.001 : "Perfect match failed";
        
        // Test case 2: Partial match
        Set<String> studentSkills2 = new HashSet<>(Arrays.asList("Java", "Python", "SQL"));
        Set<String> jobSkills2 = new HashSet<>(Arrays.asList("Java", "JavaScript", "React"));
        double similarity2 = RecommendationEngine.calculateJaccardSimilarity(studentSkills2, jobSkills2);
        System.out.println("Partial Match: " + similarity2 + " (Expected: ~0.2)");
        assert similarity2 > 0.1 && similarity2 < 0.3 : "Partial match failed";
        
        // Test case 3: No match
        Set<String> studentSkills3 = new HashSet<>(Arrays.asList("Java", "Python", "SQL"));
        Set<String> jobSkills3 = new HashSet<>(Arrays.asList("Ruby", "Go", "Rust"));
        double similarity3 = RecommendationEngine.calculateJaccardSimilarity(studentSkills3, jobSkills3);
        System.out.println("No Match: " + similarity3 + " (Expected: 0.0)");
        assert similarity3 == 0.0 : "No match failed";
        
        System.out.println("✓ Jaccard Similarity tests passed\n");
    }
    
    /**
     * Test Keyword Match Score calculation
     */
    private static void testKeywordMatchScore() {
        System.out.println("Test 2: Keyword Match Score");
        System.out.println("----------------------------");
        
        // Test case 1: All skills matched
        Set<String> studentSkills1 = new HashSet<>(Arrays.asList("Java", "Python", "SQL", "Git"));
        Set<String> jobSkills1 = new HashSet<>(Arrays.asList("Java", "Python", "SQL"));
        double score1 = RecommendationEngine.calculateKeywordMatchScore(studentSkills1, jobSkills1);
        System.out.println("All Skills Matched: " + score1 + " (Expected: 1.0)");
        assert Math.abs(score1 - 1.0) < 0.001 : "All skills matched failed";
        
        // Test case 2: Half skills matched
        Set<String> studentSkills2 = new HashSet<>(Arrays.asList("Java", "Python"));
        Set<String> jobSkills2 = new HashSet<>(Arrays.asList("Java", "Python", "JavaScript", "React"));
        double score2 = RecommendationEngine.calculateKeywordMatchScore(studentSkills2, jobSkills2);
        System.out.println("Half Skills Matched: " + score2 + " (Expected: 0.5)");
        assert Math.abs(score2 - 0.5) < 0.001 : "Half skills matched failed";
        
        // Test case 3: No skills matched
        Set<String> studentSkills3 = new HashSet<>(Arrays.asList("Java", "Python"));
        Set<String> jobSkills3 = new HashSet<>(Arrays.asList("Ruby", "Go"));
        double score3 = RecommendationEngine.calculateKeywordMatchScore(studentSkills3, jobSkills3);
        System.out.println("No Skills Matched: " + score3 + " (Expected: 0.0)");
        assert score3 == 0.0 : "No skills matched failed";
        
        System.out.println("✓ Keyword Match Score tests passed\n");
    }
    
    /**
     * Test Job Ranking functionality
     */
    private static void testJobRanking() {
        System.out.println("Test 3: Job Ranking");
        System.out.println("--------------------");
        
        // Create student with skills
        Student student = createTestStudent(1, "John Doe", 
            Arrays.asList("Java", "Python", "SQL", "Spring Boot"));
        
        // Create jobs with varying skill matches
        List<Job> jobs = new ArrayList<>();
        
        // Job 1: Perfect match (all skills match)
        jobs.add(createTestJob(1, "Backend Developer", "TechCorp",
            Arrays.asList("Java", "Python", "SQL", "Spring Boot")));
        
        // Job 2: Good match (3 out of 4 skills)
        jobs.add(createTestJob(2, "Full Stack Developer", "WebCo",
            Arrays.asList("Java", "Python", "JavaScript", "React")));
        
        // Job 3: Fair match (2 out of 4 skills)
        jobs.add(createTestJob(3, "Data Analyst", "DataInc",
            Arrays.asList("Python", "SQL", "R", "Excel")));
        
        // Job 4: Poor match (1 out of 4 skills)
        jobs.add(createTestJob(4, "Frontend Developer", "UIStudio",
            Arrays.asList("JavaScript", "React", "CSS", "HTML")));
        
        // Rank jobs
        List<JobRecommendation> recommendations = RecommendationEngine.rankJobs(student, jobs);
        
        // Verify rankings
        System.out.println("Ranked Job Recommendations:");
        for (int i = 0; i < recommendations.size(); i++) {
            JobRecommendation rec = recommendations.get(i);
            System.out.printf("%d. %s - %s (Score: %.2f%%, %s)\n", 
                i + 1,
                rec.getJob().getJobTitle(),
                rec.getJob().getCompanyName(),
                rec.getScore() * 100,
                rec.getMatchQuality()
            );
            System.out.println("   Matching Skills: " + rec.getMatchingSkills());
        }
        
        // Verify first job has highest score
        assert recommendations.get(0).getScore() > recommendations.get(1).getScore() 
            : "Job ranking order incorrect";
        
        // Verify perfect match is first
        assert recommendations.get(0).getJob().getJobId() == 1 
            : "Perfect match should be first";
        
        System.out.println("✓ Job Ranking tests passed\n");
    }
    
    /**
     * Test Edge Cases
     */
    private static void testEdgeCases() {
        System.out.println("Test 4: Edge Cases");
        System.out.println("-------------------");
        
        // Test case 1: Student with no skills
        Student studentNoSkills = createTestStudent(1, "Newbie", new ArrayList<>());
        List<Job> jobs = new ArrayList<>();
        jobs.add(createTestJob(1, "Developer", "TechCorp", 
            Arrays.asList("Java", "Python")));
        
        List<JobRecommendation> recommendations1 = RecommendationEngine.rankJobs(studentNoSkills, jobs);
        System.out.println("Student with no skills: " + recommendations1.size() + " recommendations");
        assert recommendations1.size() == 1 : "Should return jobs even with no skills";
        
        // Test case 2: Job with no required skills
        Student student = createTestStudent(2, "Expert", Arrays.asList("Java", "Python"));
        Job jobNoSkills = createTestJob(2, "Manager", "BizCorp", new ArrayList<>());
        List<Job> jobList = Arrays.asList(jobNoSkills);
        
        List<JobRecommendation> recommendations2 = RecommendationEngine.rankJobs(student, jobList);
        System.out.println("Job with no required skills: " + recommendations2.size() + " recommendations");
        assert recommendations2.size() == 1 : "Should handle jobs with no skills";
        
        // Test case 3: Empty job list
        List<JobRecommendation> recommendations3 = RecommendationEngine.rankJobs(student, new ArrayList<>());
        System.out.println("Empty job list: " + recommendations3.size() + " recommendations");
        assert recommendations3.isEmpty() : "Should return empty for empty job list";
        
        // Test case 4: Case insensitive matching
        Student studentMixedCase = createTestStudent(3, "CaseSensitive", 
            Arrays.asList("java", "python", "sql"));
        Job jobUpperCase = createTestJob(3, "Developer", "TechCorp", 
            Arrays.asList("JAVA", "PYTHON", "SQL"));
        List<JobRecommendation> recommendations4 = RecommendationEngine.rankJobs(
            studentMixedCase, Arrays.asList(jobUpperCase));
        System.out.println("Case insensitive matching score: " + 
            recommendations4.get(0).getScorePercentage() + "%");
        assert recommendations4.get(0).getScorePercentage() >= 80 
            : "Should be case insensitive";
        
        System.out.println("✓ Edge case tests passed\n");
    }
    
    /**
     * Helper method to create test student
     */
    private static Student createTestStudent(int id, String name, List<String> skillNames) {
        Student student = new Student();
        student.setStudentId(id);
        student.setFullName(name);
        student.setEmail(name.toLowerCase().replace(" ", ".") + "@test.com");
        
        List<Skill> skills = new ArrayList<>();
        for (String skillName : skillNames) {
            Skill skill = new Skill();
            skill.setSkillId(skills.size() + 1);
            skill.setSkillName(skillName);
            skills.add(skill);
        }
        student.setSkills(skills);
        
        return student;
    }
    
    /**
     * Helper method to create test job
     */
    private static Job createTestJob(int id, String title, String company, List<String> skillNames) {
        Job job = new Job();
        job.setJobId(id);
        job.setJobTitle(title);
        job.setCompanyName(company);
        job.setJobType("Full-time");
        job.setDomain("Technology");
        job.setDescription("Test job description");
        
        List<Skill> skills = new ArrayList<>();
        for (String skillName : skillNames) {
            Skill skill = new Skill();
            skill.setSkillId(skills.size() + 1);
            skill.setSkillName(skillName);
            skills.add(skill);
        }
        job.setRequiredSkills(skills);
        
        return job;
    }
}

