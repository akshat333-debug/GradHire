-- =============================================
-- Migration: Add Soft Delete Support to Applications
-- Date: 2025-10-24
-- Purpose: Add deleted_at column for soft delete functionality
-- =============================================

USE gradhire_db;

-- Add deleted_at column to applications table
ALTER TABLE applications 
ADD COLUMN deleted_at TIMESTAMP NULL DEFAULT NULL AFTER reviewer_notes,
ADD INDEX idx_deleted_at (deleted_at);

-- Update existing views to exclude soft-deleted applications
DROP VIEW IF EXISTS vw_application_details;

CREATE VIEW vw_application_details AS
SELECT 
    a.application_id,
    a.application_status,
    a.applied_at,
    a.deleted_at,
    s.student_id,
    s.full_name as student_name,
    s.email as student_email,
    s.college_name,
    j.job_id,
    j.job_title,
    j.company_name,
    j.job_type,
    ad.full_name as recruiter_name,
    ad.email as recruiter_email
FROM applications a
JOIN students s ON a.student_id = s.student_id
JOIN jobs j ON a.job_id = j.job_id
JOIN admins ad ON j.admin_id = ad.admin_id
WHERE a.deleted_at IS NULL;  -- Exclude soft-deleted applications

-- Update active jobs view to exclude soft-deleted applications
DROP VIEW IF EXISTS vw_active_jobs;

CREATE VIEW vw_active_jobs AS
SELECT 
    j.job_id,
    j.job_title,
    j.company_name,
    j.job_type,
    j.domain,
    j.location,
    j.is_remote,
    j.application_deadline,
    COUNT(a.application_id) as application_count,
    j.views_count,
    j.created_at
FROM jobs j
LEFT JOIN applications a ON j.job_id = a.job_id AND a.deleted_at IS NULL
WHERE j.job_status = 'Active' AND j.application_deadline >= CURDATE()
GROUP BY j.job_id
ORDER BY j.created_at DESC;

-- Update stored procedure to exclude soft-deleted applications
DROP PROCEDURE IF EXISTS sp_get_job_recommendations;

DELIMITER //

CREATE PROCEDURE sp_get_job_recommendations(IN p_student_id INT)
BEGIN
    SELECT DISTINCT
        j.job_id,
        j.job_title,
        j.company_name,
        j.job_type,
        j.domain,
        j.location,
        j.is_remote,
        j.salary_min,
        j.salary_max,
        j.application_deadline,
        COUNT(DISTINCT js.skill_id) as matching_skills,
        (COUNT(DISTINCT js.skill_id) * 100.0 / 
            (SELECT COUNT(*) FROM job_skills WHERE job_id = j.job_id AND is_required = TRUE)
        ) as match_percentage
    FROM jobs j
    JOIN job_skills js ON j.job_id = js.job_id
    JOIN student_skills ss ON js.skill_id = ss.skill_id
    WHERE ss.student_id = p_student_id
        AND j.job_status = 'Active'
        AND j.application_deadline >= CURDATE()
        AND NOT EXISTS (
            SELECT 1 FROM applications a 
            WHERE a.job_id = j.job_id 
            AND a.student_id = p_student_id
            AND a.deleted_at IS NULL  -- Exclude soft-deleted
        )
    GROUP BY j.job_id
    HAVING matching_skills > 0
    ORDER BY matching_skills DESC, j.created_at DESC
    LIMIT 10;
END //

DELIMITER ;

-- =============================================
-- END OF MIGRATION
-- =============================================



