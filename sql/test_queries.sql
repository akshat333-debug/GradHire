-- =============================================
-- GradHire - Comprehensive Test Queries
-- =============================================
-- This file contains test queries to verify database functionality
-- and demonstrate common use cases
-- =============================================

USE gradhire_db;

-- =============================================
-- SECTION 1: BASIC DATA VERIFICATION
-- =============================================

-- Test 1.1: Count records in all tables
SELECT '=== RECORD COUNTS ===' as '';
SELECT 'students' as table_name, COUNT(*) as records FROM students
UNION ALL
SELECT 'admins', COUNT(*) FROM admins
UNION ALL
SELECT 'jobs', COUNT(*) FROM jobs
UNION ALL
SELECT 'skills', COUNT(*) FROM skills
UNION ALL
SELECT 'student_skills', COUNT(*) FROM student_skills
UNION ALL
SELECT 'job_skills', COUNT(*) FROM job_skills
UNION ALL
SELECT 'applications', COUNT(*) FROM applications
UNION ALL
SELECT 'saved_jobs', COUNT(*) FROM saved_jobs
UNION ALL
SELECT 'activity_logs', COUNT(*) FROM activity_logs;

-- Test 1.2: Verify database encoding
SELECT '=== DATABASE ENCODING ===' as '';
SELECT DEFAULT_CHARACTER_SET_NAME, DEFAULT_COLLATION_NAME
FROM INFORMATION_SCHEMA.SCHEMATA
WHERE SCHEMA_NAME = 'gradhire_db';

-- =============================================
-- SECTION 2: STUDENT QUERIES
-- =============================================

-- Test 2.1: Get all students with their basic info
SELECT '=== ALL STUDENTS ===' as '';
SELECT 
    student_id,
    full_name,
    email,
    college_name,
    degree,
    graduation_year,
    created_at
FROM students
ORDER BY full_name;

-- Test 2.2: Get student profile with skills (John Doe - ID 1)
SELECT '=== STUDENT PROFILE: John Doe ===' as '';
SELECT 
    s.full_name,
    s.email,
    s.college_name,
    s.degree,
    s.graduation_year,
    s.bio,
    GROUP_CONCAT(
        CONCAT(sk.skill_name, ' (', ss.proficiency_level, ')') 
        ORDER BY sk.skill_name
        SEPARATOR ', '
    ) as skills
FROM students s
LEFT JOIN student_skills ss ON s.student_id = ss.student_id
LEFT JOIN skills sk ON ss.skill_id = sk.skill_id
WHERE s.student_id = 1
GROUP BY s.student_id;

-- Test 2.3: Students by graduation year
SELECT '=== STUDENTS BY GRADUATION YEAR ===' as '';
SELECT 
    graduation_year,
    COUNT(*) as student_count,
    GROUP_CONCAT(full_name ORDER BY full_name SEPARATOR ', ') as students
FROM students
WHERE graduation_year IS NOT NULL
GROUP BY graduation_year
ORDER BY graduation_year;

-- Test 2.4: Students by college
SELECT '=== STUDENTS BY COLLEGE ===' as '';
SELECT 
    college_name,
    COUNT(*) as student_count
FROM students
WHERE college_name IS NOT NULL
GROUP BY college_name
ORDER BY student_count DESC;

-- =============================================
-- SECTION 3: SKILL QUERIES
-- =============================================

-- Test 3.1: Skills by category
SELECT '=== SKILLS BY CATEGORY ===' as '';
SELECT 
    category,
    COUNT(*) as skill_count,
    GROUP_CONCAT(skill_name ORDER BY skill_name SEPARATOR ', ') as skills
FROM skills
GROUP BY category
ORDER BY skill_count DESC;

-- Test 3.2: Most popular skills (by student count)
SELECT '=== TOP 10 MOST POPULAR SKILLS ===' as '';
SELECT 
    sk.skill_name,
    sk.category,
    COUNT(DISTINCT ss.student_id) as student_count,
    AVG(CASE ss.proficiency_level
        WHEN 'Beginner' THEN 1
        WHEN 'Intermediate' THEN 2
        WHEN 'Advanced' THEN 3
        WHEN 'Expert' THEN 4
    END) as avg_proficiency
FROM skills sk
LEFT JOIN student_skills ss ON sk.skill_id = ss.skill_id
GROUP BY sk.skill_id
ORDER BY student_count DESC, avg_proficiency DESC
LIMIT 10;

-- Test 3.3: Skills distribution by proficiency level
SELECT '=== SKILLS BY PROFICIENCY LEVEL ===' as '';
SELECT 
    proficiency_level,
    COUNT(*) as count,
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM student_skills), 2) as percentage
FROM student_skills
GROUP BY proficiency_level
ORDER BY FIELD(proficiency_level, 'Beginner', 'Intermediate', 'Advanced', 'Expert');

-- =============================================
-- SECTION 4: JOB QUERIES
-- =============================================

-- Test 4.1: All active jobs
SELECT '=== ALL ACTIVE JOBS ===' as '';
SELECT 
    job_id,
    job_title,
    company_name,
    job_type,
    domain,
    location,
    CASE WHEN is_remote THEN 'Yes' ELSE 'No' END as remote,
    application_deadline,
    created_at
FROM jobs
WHERE job_status = 'Active'
ORDER BY created_at DESC;

-- Test 4.2: Jobs by type
SELECT '=== JOBS BY TYPE ===' as '';
SELECT 
    job_type,
    COUNT(*) as job_count,
    AVG(salary_min) as avg_min_salary,
    AVG(salary_max) as avg_max_salary
FROM jobs
WHERE job_status = 'Active'
GROUP BY job_type
ORDER BY job_count DESC;

-- Test 4.3: Jobs by domain
SELECT '=== JOBS BY DOMAIN ===' as '';
SELECT 
    domain,
    COUNT(*) as job_count,
    SUM(CASE WHEN is_remote THEN 1 ELSE 0 END) as remote_jobs
FROM jobs
WHERE job_status = 'Active'
GROUP BY domain
ORDER BY job_count DESC;

-- Test 4.4: Jobs with required skills
SELECT '=== JOBS WITH REQUIRED SKILLS ===' as '';
SELECT 
    j.job_title,
    j.company_name,
    j.job_type,
    GROUP_CONCAT(
        DISTINCT CASE 
            WHEN js.is_required THEN sk.skill_name 
        END 
        ORDER BY sk.skill_name
        SEPARATOR ', '
    ) as required_skills,
    GROUP_CONCAT(
        DISTINCT CASE 
            WHEN NOT js.is_required THEN sk.skill_name 
        END 
        ORDER BY sk.skill_name
        SEPARATOR ', '
    ) as optional_skills
FROM jobs j
LEFT JOIN job_skills js ON j.job_id = js.job_id
LEFT JOIN skills sk ON js.skill_id = sk.skill_id
WHERE j.job_status = 'Active'
GROUP BY j.job_id
ORDER BY j.created_at DESC;

-- Test 4.5: Remote vs On-site jobs
SELECT '=== REMOTE VS ON-SITE JOBS ===' as '';
SELECT 
    CASE WHEN is_remote THEN 'Remote' ELSE 'On-site' END as work_mode,
    COUNT(*) as job_count,
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM jobs WHERE job_status = 'Active'), 2) as percentage
FROM jobs
WHERE job_status = 'Active'
GROUP BY is_remote;

-- =============================================
-- SECTION 5: APPLICATION QUERIES
-- =============================================

-- Test 5.1: All applications with details
SELECT '=== ALL APPLICATIONS ===' as '';
SELECT 
    a.application_id,
    s.full_name as student_name,
    s.college_name,
    j.job_title,
    j.company_name,
    a.application_status,
    a.applied_at
FROM applications a
JOIN students s ON a.student_id = s.student_id
JOIN jobs j ON a.job_id = j.job_id
ORDER BY a.applied_at DESC;

-- Test 5.2: Applications by status
SELECT '=== APPLICATIONS BY STATUS ===' as '';
SELECT 
    application_status,
    COUNT(*) as count,
    ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM applications), 2) as percentage
FROM applications
GROUP BY application_status
ORDER BY FIELD(application_status, 'Pending', 'Reviewed', 'Shortlisted', 'Accepted', 'Rejected');

-- Test 5.3: Applications per job
SELECT '=== APPLICATIONS PER JOB ===' as '';
SELECT 
    j.job_title,
    j.company_name,
    COUNT(a.application_id) as application_count,
    SUM(CASE WHEN a.application_status = 'Pending' THEN 1 ELSE 0 END) as pending,
    SUM(CASE WHEN a.application_status = 'Reviewed' THEN 1 ELSE 0 END) as reviewed,
    SUM(CASE WHEN a.application_status = 'Shortlisted' THEN 1 ELSE 0 END) as shortlisted,
    SUM(CASE WHEN a.application_status = 'Accepted' THEN 1 ELSE 0 END) as accepted,
    SUM(CASE WHEN a.application_status = 'Rejected' THEN 1 ELSE 0 END) as rejected
FROM jobs j
LEFT JOIN applications a ON j.job_id = a.job_id
WHERE j.job_status = 'Active'
GROUP BY j.job_id
ORDER BY application_count DESC;

-- Test 5.4: Student application history
SELECT '=== STUDENT APPLICATION HISTORY: John Doe ===' as '';
SELECT 
    j.job_title,
    j.company_name,
    j.job_type,
    j.location,
    a.application_status,
    a.applied_at,
    DATEDIFF(NOW(), a.applied_at) as days_since_applied
FROM applications a
JOIN jobs j ON a.job_id = j.job_id
WHERE a.student_id = 1
ORDER BY a.applied_at DESC;

-- =============================================
-- SECTION 6: JOB RECOMMENDATION QUERIES
-- =============================================

-- Test 6.1: Job recommendations for John Doe (Student ID 1)
SELECT '=== JOB RECOMMENDATIONS FOR JOHN DOE ===' as '';
CALL sp_get_job_recommendations(1);

-- Test 6.2: Job recommendations for Alice Smith (Student ID 2 - Data Science)
SELECT '=== JOB RECOMMENDATIONS FOR ALICE SMITH ===' as '';
CALL sp_get_job_recommendations(2);

-- Test 6.3: Manual skill match calculation for a specific job and student
SELECT '=== DETAILED SKILL MATCH: John Doe vs Software Dev Intern ===' as '';
SELECT 
    j.job_title,
    j.company_name,
    sk.skill_name,
    js.is_required,
    CASE WHEN ss.student_id IS NOT NULL THEN 'YES' ELSE 'NO' END as student_has_skill,
    ss.proficiency_level
FROM jobs j
JOIN job_skills js ON j.job_id = js.job_id
JOIN skills sk ON js.skill_id = sk.skill_id
LEFT JOIN student_skills ss ON sk.skill_id = ss.skill_id AND ss.student_id = 1
WHERE j.job_id = 1
ORDER BY js.is_required DESC, sk.skill_name;

-- Test 6.4: Best matching students for a job
SELECT '=== BEST MATCHING STUDENTS FOR SOFTWARE DEV INTERN ===' as '';
SELECT 
    s.student_id,
    s.full_name,
    s.email,
    s.college_name,
    COUNT(DISTINCT js.skill_id) as total_required_skills,
    COUNT(DISTINCT CASE WHEN ss.student_id IS NOT NULL THEN js.skill_id END) as matching_skills,
    ROUND(
        (COUNT(DISTINCT CASE WHEN ss.student_id IS NOT NULL THEN js.skill_id END) * 100.0) / 
        COUNT(DISTINCT js.skill_id), 
        2
    ) as match_percentage
FROM jobs j
JOIN job_skills js ON j.job_id = js.job_id AND js.is_required = TRUE
CROSS JOIN students s
LEFT JOIN student_skills ss ON js.skill_id = ss.skill_id AND ss.student_id = s.student_id
WHERE j.job_id = 1 AND s.is_active = TRUE
GROUP BY s.student_id
HAVING matching_skills > 0
ORDER BY match_percentage DESC, matching_skills DESC;

-- =============================================
-- SECTION 7: ANALYTICS QUERIES
-- =============================================

-- Test 7.1: Overall platform statistics
SELECT '=== PLATFORM STATISTICS ===' as '';
SELECT 
    (SELECT COUNT(*) FROM students WHERE is_active = TRUE) as total_students,
    (SELECT COUNT(*) FROM admins WHERE is_active = TRUE) as total_recruiters,
    (SELECT COUNT(*) FROM jobs WHERE job_status = 'Active') as active_jobs,
    (SELECT COUNT(*) FROM applications) as total_applications,
    (SELECT COUNT(*) FROM skills) as total_skills;

-- Test 7.2: Activity by type
SELECT '=== ACTIVITY LOG SUMMARY ===' as '';
SELECT 
    activity_type,
    COUNT(*) as count,
    MIN(created_at) as first_occurrence,
    MAX(created_at) as last_occurrence
FROM activity_logs
GROUP BY activity_type
ORDER BY count DESC;

-- Test 7.3: Most active students
SELECT '=== MOST ACTIVE STUDENTS ===' as '';
SELECT 
    s.full_name,
    s.email,
    COUNT(DISTINCT a.application_id) as applications_submitted,
    COUNT(DISTINCT sj.job_id) as jobs_saved,
    COUNT(DISTINCT al.log_id) as total_activities
FROM students s
LEFT JOIN applications a ON s.student_id = a.student_id
LEFT JOIN saved_jobs sj ON s.student_id = sj.student_id
LEFT JOIN activity_logs al ON al.user_type = 'student' AND al.user_id = s.student_id
GROUP BY s.student_id
ORDER BY total_activities DESC, applications_submitted DESC
LIMIT 5;

-- Test 7.4: Most active recruiters
SELECT '=== MOST ACTIVE RECRUITERS ===' as '';
SELECT 
    ad.full_name,
    ad.company_name,
    COUNT(DISTINCT j.job_id) as jobs_posted,
    COUNT(DISTINCT a.application_id) as applications_received,
    COUNT(DISTINCT al.log_id) as total_activities
FROM admins ad
LEFT JOIN jobs j ON ad.admin_id = j.admin_id
LEFT JOIN applications a ON j.job_id = a.job_id
LEFT JOIN activity_logs al ON al.user_type = 'admin' AND al.user_id = ad.admin_id
GROUP BY ad.admin_id
ORDER BY jobs_posted DESC, total_activities DESC;

-- Test 7.5: Job posting trends (by creation date)
SELECT '=== JOB POSTING TRENDS ===' as '';
SELECT 
    DATE(created_at) as posting_date,
    COUNT(*) as jobs_posted,
    GROUP_CONCAT(CONCAT(job_title, ' (', company_name, ')') SEPARATOR '; ') as jobs
FROM jobs
GROUP BY DATE(created_at)
ORDER BY posting_date DESC;

-- =============================================
-- SECTION 8: VIEW TESTS
-- =============================================

-- Test 8.1: Student profiles view
SELECT '=== STUDENT PROFILES VIEW ===' as '';
SELECT * FROM vw_student_profiles
ORDER BY skill_count DESC, full_name
LIMIT 5;

-- Test 8.2: Active jobs view
SELECT '=== ACTIVE JOBS VIEW ===' as '';
SELECT * FROM vw_active_jobs
ORDER BY application_count DESC, created_at DESC
LIMIT 5;

-- Test 8.3: Application details view
SELECT '=== APPLICATION DETAILS VIEW ===' as '';
SELECT * FROM vw_application_details
ORDER BY applied_at DESC
LIMIT 5;

-- =============================================
-- SECTION 9: SAVED JOBS QUERIES
-- =============================================

-- Test 9.1: Saved jobs by student
SELECT '=== SAVED JOBS BY STUDENT ===' as '';
SELECT 
    s.full_name as student_name,
    COUNT(sj.job_id) as saved_jobs_count
FROM students s
LEFT JOIN saved_jobs sj ON s.student_id = sj.student_id
GROUP BY s.student_id
HAVING saved_jobs_count > 0
ORDER BY saved_jobs_count DESC;

-- Test 9.2: Specific student's saved jobs
SELECT '=== SAVED JOBS FOR JOHN DOE ===' as '';
SELECT 
    j.job_title,
    j.company_name,
    j.job_type,
    j.location,
    sj.saved_at,
    CASE WHEN a.application_id IS NOT NULL THEN 'Applied' ELSE 'Not Applied' END as application_status
FROM saved_jobs sj
JOIN jobs j ON sj.job_id = j.job_id
LEFT JOIN applications a ON j.job_id = a.job_id AND a.student_id = sj.student_id
WHERE sj.student_id = 1
ORDER BY sj.saved_at DESC;

-- =============================================
-- SECTION 10: COMPLEX QUERIES
-- =============================================

-- Test 10.1: Students with specific skill combinations
SELECT '=== STUDENTS WITH JAVA AND SPRING BOOT ===' as '';
SELECT DISTINCT
    s.student_id,
    s.full_name,
    s.email,
    s.college_name,
    GROUP_CONCAT(DISTINCT sk.skill_name ORDER BY sk.skill_name SEPARATOR ', ') as all_skills
FROM students s
JOIN student_skills ss1 ON s.student_id = ss1.student_id
JOIN skills sk1 ON ss1.skill_id = sk1.skill_id AND sk1.skill_name = 'Java'
JOIN student_skills ss2 ON s.student_id = ss2.student_id
JOIN skills sk2 ON ss2.skill_id = sk2.skill_id AND sk2.skill_name = 'Spring Boot'
JOIN student_skills ss ON s.student_id = ss.student_id
JOIN skills sk ON ss.skill_id = sk.skill_id
GROUP BY s.student_id;

-- Test 10.2: Jobs requiring specific skills
SELECT '=== JOBS REQUIRING PYTHON ===' as '';
SELECT 
    j.job_title,
    j.company_name,
    j.job_type,
    j.domain,
    GROUP_CONCAT(DISTINCT sk.skill_name ORDER BY sk.skill_name SEPARATOR ', ') as all_required_skills
FROM jobs j
JOIN job_skills js ON j.job_id = js.job_id
JOIN skills sk ON js.skill_id = sk.skill_id
WHERE j.job_status = 'Active' 
  AND EXISTS (
      SELECT 1 FROM job_skills js2
      JOIN skills sk2 ON js2.skill_id = sk2.skill_id
      WHERE js2.job_id = j.job_id AND sk2.skill_name = 'Python' AND js2.is_required = TRUE
  )
GROUP BY j.job_id;

-- Test 10.3: Application success rate by student
SELECT '=== APPLICATION SUCCESS RATE BY STUDENT ===' as '';
SELECT 
    s.full_name,
    COUNT(a.application_id) as total_applications,
    SUM(CASE WHEN a.application_status IN ('Shortlisted', 'Accepted') THEN 1 ELSE 0 END) as positive_responses,
    ROUND(
        SUM(CASE WHEN a.application_status IN ('Shortlisted', 'Accepted') THEN 1 ELSE 0 END) * 100.0 / 
        COUNT(a.application_id), 
        2
    ) as success_rate
FROM students s
JOIN applications a ON s.student_id = a.student_id
GROUP BY s.student_id
HAVING total_applications > 0
ORDER BY success_rate DESC, total_applications DESC;

-- Test 10.4: Companies with most job postings
SELECT '=== COMPANIES WITH MOST JOB POSTINGS ===' as '';
SELECT 
    company_name,
    COUNT(*) as total_jobs,
    SUM(CASE WHEN job_status = 'Active' THEN 1 ELSE 0 END) as active_jobs,
    SUM(CASE WHEN is_remote THEN 1 ELSE 0 END) as remote_jobs,
    GROUP_CONCAT(DISTINCT job_type ORDER BY job_type SEPARATOR ', ') as job_types_offered
FROM jobs
GROUP BY company_name
ORDER BY total_jobs DESC;

-- Test 10.5: Skills gap analysis (required skills vs student skills)
SELECT '=== TOP SKILLS IN DEMAND (NOT COMMONLY POSSESSED) ===' as '';
SELECT 
    sk.skill_name,
    sk.category,
    COUNT(DISTINCT js.job_id) as jobs_requiring,
    COUNT(DISTINCT ss.student_id) as students_with_skill,
    COUNT(DISTINCT js.job_id) - COUNT(DISTINCT ss.student_id) as demand_gap
FROM skills sk
LEFT JOIN job_skills js ON sk.skill_id = js.skill_id AND js.is_required = TRUE
LEFT JOIN student_skills ss ON sk.skill_id = ss.skill_id
GROUP BY sk.skill_id
HAVING jobs_requiring > 0
ORDER BY demand_gap DESC
LIMIT 10;

-- =============================================
-- END OF TEST QUERIES
-- =============================================

SELECT '=== ALL TESTS COMPLETED ===' as '';
SELECT CONCAT(
    'Database: gradhire_db | ',
    'Date: ', NOW(), ' | ',
    'Total Tables: ', (SELECT COUNT(*) FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'gradhire_db')
) as summary;

