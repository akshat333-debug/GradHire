# GradHire Database Design Documentation

## 📊 Entity Relationship Overview

This document provides detailed documentation of the GradHire database schema, including entity relationships, design decisions, and usage patterns.

---

## 🗂️ Table Descriptions

### 1. **students** Table

**Purpose**: Store student user accounts and profile information

**Columns**:
| Column            | Type          | Constraints | Description                          |
| ----------------- | ------------- | ----------- | ------------------------------------ |
| student_id        | INT           | PK, AI      | Unique identifier                    |
| email             | VARCHAR(100)  | UNIQUE, NN  | Student email (login credential)     |
| password_hash     | VARCHAR(255)  | NN          | BCrypt hashed password               |
| full_name         | VARCHAR(100)  | NN          | Student's full name                  |
| phone             | VARCHAR(15)   |             | Contact number                       |
| college_name      | VARCHAR(150)  |             | Name of college/university           |
| degree            | VARCHAR(100)  |             | Degree program                       |
| graduation_year   | INT           |             | Expected graduation year             |
| resume_path       | VARCHAR(255)  |             | File path to uploaded resume         |
| profile_picture   | VARCHAR(255)  |             | Profile image path                   |
| bio               | TEXT          |             | Student bio/summary                  |
| linkedin_url      | VARCHAR(255)  |             | LinkedIn profile URL                 |
| github_url        | VARCHAR(255)  |             | GitHub profile URL                   |
| created_at        | TIMESTAMP     | DEFAULT NOW | Account creation timestamp           |
| updated_at        | TIMESTAMP     | AUTO UPDATE | Last profile update timestamp        |
| is_active         | BOOLEAN       | DEFAULT 1   | Account status flag                  |

**Indexes**:
- `idx_email` - Fast login lookups
- `idx_graduation_year` - Filter by graduation year

**Relationships**:
- One-to-Many with `student_skills`
- One-to-Many with `applications`
- One-to-Many with `saved_jobs`
- One-to-Many with `activity_logs`

---

### 2. **admins** Table

**Purpose**: Store recruiter and administrator accounts

**Columns**:
| Column            | Type          | Constraints     | Description                      |
| ----------------- | ------------- | --------------- | -------------------------------- |
| admin_id          | INT           | PK, AI          | Unique identifier                |
| email             | VARCHAR(100)  | UNIQUE, NN      | Admin email (login credential)   |
| password_hash     | VARCHAR(255)  | NN              | BCrypt hashed password           |
| full_name         | VARCHAR(100)  | NN              | Admin's full name                |
| company_name      | VARCHAR(150)  |                 | Company/organization name        |
| company_website   | VARCHAR(255)  |                 | Company website URL              |
| phone             | VARCHAR(15)   |                 | Contact number                   |
| role              | ENUM          | DEFAULT 'recruiter' | admin or recruiter           |
| created_at        | TIMESTAMP     | DEFAULT NOW     | Account creation timestamp       |
| updated_at        | TIMESTAMP     | AUTO UPDATE     | Last update timestamp            |
| is_active         | BOOLEAN       | DEFAULT 1       | Account status flag              |

**Indexes**:
- `idx_email` - Fast login lookups
- `idx_company` - Filter by company

**Relationships**:
- One-to-Many with `jobs`
- One-to-Many with `activity_logs`

---

### 3. **skills** Table

**Purpose**: Master reference table for all skills and technologies

**Columns**:
| Column       | Type         | Constraints | Description                             |
| ------------ | ------------ | ----------- | --------------------------------------- |
| skill_id     | INT          | PK, AI      | Unique identifier                       |
| skill_name   | VARCHAR(100) | UNIQUE, NN  | Name of skill (e.g., "Java", "Python")  |
| category     | VARCHAR(50)  |             | Skill category (e.g., "Programming")    |
| created_at   | TIMESTAMP    | DEFAULT NOW | Creation timestamp                      |

**Categories**:
- Programming
- Web Development
- Database
- Data Science
- Cloud
- DevOps
- Design
- Marketing
- Business

**Indexes**:
- `idx_skill_name` - Fast skill lookups
- `idx_category` - Filter by category

**Relationships**:
- One-to-Many with `student_skills`
- One-to-Many with `job_skills`

---

### 4. **student_skills** Table

**Purpose**: Many-to-many junction table linking students to their skills

**Columns**:
| Column              | Type     | Constraints | Description                          |
| ------------------- | -------- | ----------- | ------------------------------------ |
| student_skill_id    | INT      | PK, AI      | Unique identifier                    |
| student_id          | INT      | FK, NN      | Reference to students table          |
| skill_id            | INT      | FK, NN      | Reference to skills table            |
| proficiency_level   | ENUM     | DEFAULT 'Intermediate' | Skill level             |
| created_at          | TIMESTAMP| DEFAULT NOW | When skill was added                 |

**Proficiency Levels**:
- Beginner
- Intermediate
- Advanced
- Expert

**Constraints**:
- `unique_student_skill` - Prevents duplicate skill assignments

**Relationships**:
- Many-to-One with `students`
- Many-to-One with `skills`

---

### 5. **jobs** Table

**Purpose**: Store job and internship listings

**Columns**:
| Column                | Type           | Constraints     | Description                       |
| --------------------- | -------------- | --------------- | --------------------------------- |
| job_id                | INT            | PK, AI          | Unique identifier                 |
| admin_id              | INT            | FK, NN          | Who posted the job                |
| job_title             | VARCHAR(200)   | NN              | Job title                         |
| company_name          | VARCHAR(150)   | NN              | Hiring company                    |
| job_type              | ENUM           | NN              | Internship/Full-time/Part-time    |
| domain                | VARCHAR(100)   |                 | Job domain/category               |
| description           | TEXT           | NN              | Detailed job description          |
| requirements          | TEXT           |                 | Job requirements                  |
| responsibilities      | TEXT           |                 | Job responsibilities              |
| location              | VARCHAR(150)   |                 | Job location                      |
| is_remote             | BOOLEAN        | DEFAULT 0       | Remote work flag                  |
| salary_min            | DECIMAL(10,2)  |                 | Minimum salary (hourly/annual)    |
| salary_max            | DECIMAL(10,2)  |                 | Maximum salary                    |
| application_deadline  | DATE           |                 | Application deadline              |
| job_status            | ENUM           | DEFAULT 'Active'| Active/Closed/Draft               |
| views_count           | INT            | DEFAULT 0       | Number of views                   |
| created_at            | TIMESTAMP      | DEFAULT NOW     | Job posting timestamp             |
| updated_at            | TIMESTAMP      | AUTO UPDATE     | Last update timestamp             |

**Job Types**:
- Internship
- Full-time
- Part-time
- Contract

**Job Status**:
- Active - Accepting applications
- Closed - No longer accepting
- Draft - Not yet published

**Indexes**:
- `idx_admin` - Jobs by recruiter
- `idx_job_type` - Filter by type
- `idx_domain` - Filter by domain
- `idx_status` - Active jobs only
- `idx_deadline` - Upcoming deadlines

**Relationships**:
- Many-to-One with `admins`
- One-to-Many with `job_skills`
- One-to-Many with `applications`
- One-to-Many with `saved_jobs`

---

### 6. **job_skills** Table

**Purpose**: Many-to-many junction table linking jobs to required skills

**Columns**:
| Column         | Type      | Constraints | Description                            |
| -------------- | --------- | ----------- | -------------------------------------- |
| job_skill_id   | INT       | PK, AI      | Unique identifier                      |
| job_id         | INT       | FK, NN      | Reference to jobs table                |
| skill_id       | INT       | FK, NN      | Reference to skills table              |
| is_required    | BOOLEAN   | DEFAULT 1   | TRUE=required, FALSE=nice-to-have      |
| created_at     | TIMESTAMP | DEFAULT NOW | Creation timestamp                     |

**Constraints**:
- `unique_job_skill` - Prevents duplicate skill requirements

**Relationships**:
- Many-to-One with `jobs`
- Many-to-One with `skills`

---

### 7. **applications** Table

**Purpose**: Track student applications to jobs

**Columns**:
| Column              | Type      | Constraints       | Description                      |
| ------------------- | --------- | ----------------- | -------------------------------- |
| application_id      | INT       | PK, AI            | Unique identifier                |
| job_id              | INT       | FK, NN            | Which job                        |
| student_id          | INT       | FK, NN            | Which student                    |
| cover_letter        | TEXT      |                   | Application cover letter         |
| application_status  | ENUM      | DEFAULT 'Pending' | Current status                   |
| applied_at          | TIMESTAMP | DEFAULT NOW       | Application timestamp            |
| reviewed_at         | TIMESTAMP | NULL              | Review timestamp                 |
| reviewer_notes      | TEXT      |                   | Internal notes from recruiter    |

**Application Status Workflow**:
1. **Pending** - Initial state after submission
2. **Reviewed** - Recruiter has viewed
3. **Shortlisted** - Candidate selected for next round
4. **Rejected** - Application declined
5. **Accepted** - Offer extended

**Constraints**:
- `unique_application` - One application per student per job

**Indexes**:
- `idx_job` - Applications by job
- `idx_student` - Applications by student
- `idx_status` - Filter by status
- `idx_applied_at` - Sort by date

**Relationships**:
- Many-to-One with `jobs`
- Many-to-One with `students`

---

### 8. **saved_jobs** Table

**Purpose**: Allow students to bookmark jobs for later

**Columns**:
| Column        | Type      | Constraints | Description                |
| ------------- | --------- | ----------- | -------------------------- |
| saved_job_id  | INT       | PK, AI      | Unique identifier          |
| student_id    | INT       | FK, NN      | Which student              |
| job_id        | INT       | FK, NN      | Which job                  |
| saved_at      | TIMESTAMP | DEFAULT NOW | Bookmark timestamp         |

**Constraints**:
- `unique_saved_job` - Prevents duplicate bookmarks

**Relationships**:
- Many-to-One with `students`
- Many-to-One with `jobs`

---

### 9. **activity_logs** Table

**Purpose**: Audit trail for all user activities

**Columns**:
| Column                | Type         | Constraints | Description                      |
| --------------------- | ------------ | ----------- | -------------------------------- |
| log_id                | INT          | PK, AI      | Unique identifier                |
| user_type             | ENUM         | NN          | 'student' or 'admin'             |
| user_id               | INT          | NN          | ID of the user                   |
| activity_type         | VARCHAR(50)  | NN          | Type of activity                 |
| activity_description  | TEXT         |             | Detailed description             |
| ip_address            | VARCHAR(45)  |             | User's IP address (IPv6 ready)   |
| user_agent            | VARCHAR(255) |             | Browser/device information       |
| created_at            | TIMESTAMP    | DEFAULT NOW | Activity timestamp               |

**Activity Types**:
- `login` - User logged in
- `logout` - User logged out
- `job_post` - New job posted (admin)
- `application` - Job application submitted
- `profile_update` - Profile modified
- `application_review` - Application reviewed (admin)
- `job_edit` - Job listing modified
- `resume_upload` - Resume uploaded

**Indexes**:
- `idx_user` - Composite index on (user_type, user_id)
- `idx_activity_type` - Filter by activity
- `idx_created_at` - Sort by date

---

## 🔗 Relationships Diagram

```
┌─────────────┐         ┌──────────────────┐         ┌─────────────┐
│  students   │─────────│ student_skills   │─────────│   skills    │
└─────────────┘  1:N    └──────────────────┘   N:1   └─────────────┘
      │                                                      │
      │ 1:N                                              N:1 │
      │                                                      │
      ▼                                                      ▼
┌──────────────┐                                    ┌──────────────┐
│ applications │                                    │  job_skills  │
└──────────────┘                                    └──────────────┘
      │ N:1                                              N:1 │
      │                                                      │
      │          ┌─────────────┐                            │
      └─────────▶│    jobs     │◀───────────────────────────┘
                 └─────────────┘
                       │ N:1
                       │
                       ▼
                 ┌─────────────┐
                 │   admins    │
                 └─────────────┘

┌─────────────┐
│ saved_jobs  │  (Junction table: students ↔ jobs)
└─────────────┘

┌────────────────┐
│ activity_logs  │  (Tracks all user activities)
└────────────────┘
```

---

## 🎯 Design Decisions

### Normalization

The database follows **Third Normal Form (3NF)**:
- No repeating groups (student skills in separate table)
- No partial dependencies (all non-key fields depend on entire PK)
- No transitive dependencies (no derived data)

### Junction Tables

**student_skills** and **job_skills**:
- Implement many-to-many relationships
- Allow additional attributes (proficiency_level, is_required)
- Enable efficient skill matching queries

### Soft Deletes

The `is_active` flag in students and admins tables:
- Preserves referential integrity
- Maintains historical data
- Allows account reactivation

### Timestamps

All tables include timestamp fields:
- `created_at` - Record creation (audit trail)
- `updated_at` - Last modification (track changes)
- Auto-updated on INSERT/UPDATE

### Enumerations

Using ENUM for constrained values:
- Data integrity at database level
- Prevents invalid states
- Self-documenting schema
- Examples: job_type, application_status, role

---

## 🚀 Performance Optimization

### Indexes

Strategic indexes on:
- Primary keys (automatic)
- Foreign keys (join optimization)
- Email fields (login queries)
- Status fields (filtering)
- Timestamp fields (sorting)

### Views

Pre-computed joins for common queries:
- `vw_student_profiles` - Student summary with skill count
- `vw_active_jobs` - Active jobs with application count
- `vw_application_details` - Complete application info

Benefits:
- Simplified queries
- Consistent results
- Query optimization by MySQL

### Stored Procedures

**sp_get_job_recommendations(student_id)**:
- Complex business logic in database
- Reduces network round trips
- Reusable across applications
- Server-side execution

---

## 📊 Sample Queries

### 1. Get Student Profile with Skills
```sql
SELECT 
    s.full_name, 
    s.email, 
    s.college_name,
    s.graduation_year,
    GROUP_CONCAT(
        CONCAT(sk.skill_name, ' (', ss.proficiency_level, ')') 
        SEPARATOR ', '
    ) as skills
FROM students s
JOIN student_skills ss ON s.student_id = ss.student_id
JOIN skills sk ON ss.skill_id = sk.skill_id
WHERE s.student_id = 1
GROUP BY s.student_id;
```

### 2. Get Jobs with Required Skills
```sql
SELECT 
    j.job_title,
    j.company_name,
    j.job_type,
    j.location,
    GROUP_CONCAT(sk.skill_name SEPARATOR ', ') as required_skills
FROM jobs j
JOIN job_skills js ON j.job_id = js.job_id
JOIN skills sk ON js.skill_id = sk.skill_id
WHERE j.job_status = 'Active' 
  AND js.is_required = TRUE
GROUP BY j.job_id
ORDER BY j.created_at DESC;
```

### 3. Get Applications for a Job
```sql
SELECT 
    a.application_id,
    s.full_name,
    s.email,
    s.college_name,
    a.application_status,
    a.applied_at,
    GROUP_CONCAT(sk.skill_name SEPARATOR ', ') as student_skills
FROM applications a
JOIN students s ON a.student_id = s.student_id
LEFT JOIN student_skills ss ON s.student_id = ss.student_id
LEFT JOIN skills sk ON ss.skill_id = sk.skill_id
WHERE a.job_id = 1
GROUP BY a.application_id
ORDER BY a.applied_at DESC;
```

### 4. Job Match Percentage for Student
```sql
SELECT 
    j.job_id,
    j.job_title,
    j.company_name,
    COUNT(DISTINCT js.skill_id) as total_required_skills,
    COUNT(DISTINCT CASE WHEN ss.student_id IS NOT NULL THEN js.skill_id END) as matching_skills,
    ROUND(
        (COUNT(DISTINCT CASE WHEN ss.student_id IS NOT NULL THEN js.skill_id END) * 100.0) / 
        COUNT(DISTINCT js.skill_id), 
        2
    ) as match_percentage
FROM jobs j
JOIN job_skills js ON j.job_id = js.job_id AND js.is_required = TRUE
LEFT JOIN student_skills ss ON js.skill_id = ss.skill_id AND ss.student_id = 1
WHERE j.job_status = 'Active'
GROUP BY j.job_id
HAVING matching_skills > 0
ORDER BY match_percentage DESC, matching_skills DESC;
```

### 5. Application Statistics
```sql
SELECT 
    j.job_title,
    j.company_name,
    COUNT(a.application_id) as total_applications,
    SUM(CASE WHEN a.application_status = 'Pending' THEN 1 ELSE 0 END) as pending,
    SUM(CASE WHEN a.application_status = 'Reviewed' THEN 1 ELSE 0 END) as reviewed,
    SUM(CASE WHEN a.application_status = 'Shortlisted' THEN 1 ELSE 0 END) as shortlisted,
    SUM(CASE WHEN a.application_status = 'Rejected' THEN 1 ELSE 0 END) as rejected,
    SUM(CASE WHEN a.application_status = 'Accepted' THEN 1 ELSE 0 END) as accepted
FROM jobs j
LEFT JOIN applications a ON j.job_id = a.job_id
WHERE j.admin_id = 1
GROUP BY j.job_id
ORDER BY total_applications DESC;
```

---

## 🔐 Security Considerations

### Password Storage
- Passwords stored as BCrypt hashes (not plaintext)
- Salt automatically included in hash
- Example hash format: `$2a$10$...`

### SQL Injection Prevention
- Use prepared statements in Java (JDBC)
- Never concatenate user input in queries
- Validate and sanitize all inputs

### Access Control
- Role-based authentication (student/admin)
- Separate tables for different user types
- Activity logging for audit trail

### Data Privacy
- Email uniqueness enforced
- Personal data access controls
- Option to soft-delete accounts

---

## 📈 Scalability Considerations

### Current Design Supports:
- Thousands of students and jobs
- Millions of applications
- Efficient skill matching
- Fast search and filtering

### Future Enhancements:
- Partitioning for activity_logs (by date)
- Read replicas for reporting
- Caching layer (Redis)
- Full-text search (Elasticsearch)
- Archive old applications

---

## 🧪 Testing Data

The schema includes sample data for:
- 8 diverse student profiles
- 5 recruiters from different companies
- 50+ skills across multiple categories
- 10 job postings (mix of internships and full-time)
- Sample applications in various states
- Activity logs for common actions

This data enables immediate testing of:
- Login functionality
- Job search and filtering
- Skill matching algorithm
- Application workflow
- Dashboard analytics

---

## 📞 Support

For database-related questions or issues:
1. Check this documentation
2. Review the schema.sql comments
3. Test queries in MySQL workbench
4. Consult the README.md

---

**Database Version**: 1.0
**Last Updated**: October 24, 2025
**MySQL Version Required**: 8.0+

