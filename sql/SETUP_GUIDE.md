# GradHire Database Setup Guide

This guide will help you set up the MySQL database for the GradHire application.

---

## 📋 Prerequisites

Before you begin, ensure you have:

- ✅ MySQL 8.0 or higher installed
- ✅ MySQL command-line client or MySQL Workbench
- ✅ Root or admin access to MySQL
- ✅ At least 100MB of free disk space

---

## 🚀 Quick Setup (Recommended)

### Step 1: Login to MySQL

```bash
mysql -u root -p
```

Enter your MySQL root password when prompted.

### Step 2: Run the Schema File

**Option A: From MySQL prompt**
```sql
source /absolute/path/to/GradHire/sql/schema.sql;
```

**Option B: From command line**
```bash
mysql -u root -p < /absolute/path/to/GradHire/sql/schema.sql
```

### Step 3: Verify Installation

```sql
USE gradhire_db;

-- Check all tables were created
SHOW TABLES;

-- Expected output:
-- +------------------------+
-- | Tables_in_gradhire_db  |
-- +------------------------+
-- | activity_logs          |
-- | admins                 |
-- | applications           |
-- | job_skills             |
-- | jobs                   |
-- | saved_jobs             |
-- | skills                 |
-- | student_skills         |
-- | students               |
-- | vw_active_jobs         |
-- | vw_application_details |
-- | vw_student_profiles    |
-- +------------------------+
```

### Step 4: Verify Sample Data

```sql
-- Count records in each table
SELECT 'students' as table_name, COUNT(*) as records FROM students
UNION ALL
SELECT 'admins', COUNT(*) FROM admins
UNION ALL
SELECT 'jobs', COUNT(*) FROM jobs
UNION ALL
SELECT 'skills', COUNT(*) FROM skills
UNION ALL
SELECT 'applications', COUNT(*) FROM applications;

-- Expected output:
-- +-------------+---------+
-- | table_name  | records |
-- +-------------+---------+
-- | students    |       8 |
-- | admins      |       5 |
-- | jobs        |      10 |
-- | skills      |      51 |
-- | applications|      10 |
-- +-------------+---------+
```

---

## 🔧 Manual Setup (Step by Step)

### Step 1: Create Database User (Optional but Recommended)

Create a dedicated user for the GradHire application:

```sql
-- Login as root
mysql -u root -p

-- Create user
CREATE USER 'gradhire_user'@'localhost' IDENTIFIED BY 'your_secure_password';

-- Grant privileges
GRANT ALL PRIVILEGES ON gradhire_db.* TO 'gradhire_user'@'localhost';

-- Flush privileges
FLUSH PRIVILEGES;

-- Exit
EXIT;
```

### Step 2: Run Schema as New User

```bash
mysql -u gradhire_user -p < /absolute/path/to/GradHire/sql/schema.sql
```

---

## ✅ Post-Installation Tests

### Test 1: Login Verification

Verify that you can query user tables:

```sql
USE gradhire_db;

-- Test student login data
SELECT email, full_name, college_name 
FROM students 
LIMIT 3;

-- Test admin login data
SELECT email, full_name, company_name, role 
FROM admins 
LIMIT 3;
```

### Test 2: Skill Matching

Test the job recommendation stored procedure:

```sql
-- Get job recommendations for student ID 1 (John Doe)
CALL sp_get_job_recommendations(1);

-- Should return jobs matching John's skills:
-- Java, Python, JavaScript, React, Spring Boot, Machine Learning
```

### Test 3: View Functionality

```sql
-- Test student profiles view
SELECT * FROM vw_student_profiles LIMIT 5;

-- Test active jobs view
SELECT * FROM vw_active_jobs LIMIT 5;

-- Test application details view
SELECT * FROM vw_application_details LIMIT 5;
```

### Test 4: Foreign Key Constraints

```sql
-- This should fail (referential integrity)
INSERT INTO applications (job_id, student_id, cover_letter) 
VALUES (9999, 9999, 'Test');

-- Expected: Error 1452 - Cannot add or update a child row
```

---

## 🔍 Troubleshooting

### Issue 1: "Access denied for user 'root'@'localhost'"

**Solution**:
```bash
# Reset MySQL root password
sudo mysql
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'new_password';
FLUSH PRIVILEGES;
EXIT;
```

### Issue 2: "Database already exists"

**Solution**:
```sql
-- Drop existing database (WARNING: This deletes all data!)
DROP DATABASE IF EXISTS gradhire_db;

-- Then re-run the schema file
source /absolute/path/to/GradHire/sql/schema.sql;
```

### Issue 3: "File not found" when using source command

**Solution**:
- Use absolute path instead of relative path
- Ensure the file path has no typos
- On Windows, use forward slashes: `C:/Users/...` or escape backslashes

### Issue 4: Character encoding issues

**Solution**:
```sql
-- Check current encoding
SHOW VARIABLES LIKE 'character_set%';

-- The database should use utf8mb4
-- If not, recreate with:
CREATE DATABASE gradhire_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;
```

### Issue 5: Stored procedure fails

**Solution**:
```sql
-- Check if procedure exists
SHOW PROCEDURE STATUS WHERE Db = 'gradhire_db';

-- Drop and recreate if needed
DROP PROCEDURE IF EXISTS sp_get_job_recommendations;

-- Then re-run the schema file
```

---

## 🗄️ Database Configuration for Java Application

### Runtime database configuration

```properties
# Database Configuration
db.url=jdbc:mysql://localhost:3306/gradhire_db?useSSL=false&serverTimezone=UTC
db.username=gradhire_user
db.password=your_secure_password
db.driver=com.mysql.cj.jdbc.Driver

# Connection Pool Settings
db.pool.initialSize=5
db.pool.maxTotal=10
db.pool.maxIdle=5
db.pool.minIdle=2
```

### Download MySQL Connector/J

1. Download from: https://dev.mysql.com/downloads/connector/j/
2. Choose "Platform Independent" version
3. Extract and copy `mysql-connector-java-8.x.x.jar` to your project's `lib/` folder

---

## 📊 Sample Login Credentials

Use these credentials for testing:

### Students

| Email                    | Password    | Name           | Specialization  |
| ------------------------ | ----------- | -------------- | --------------- |
| john.doe@student.edu     | student123  | John Doe       | Full-stack + AI |
| alice.smith@student.edu  | student123  | Alice Smith    | Data Science    |
| bob.wilson@student.edu   | student123  | Bob Wilson     | Frontend        |
| emma.davis@student.edu   | student123  | Emma Davis     | Backend Java    |

### Recruiters

| Email                  | Password | Company            | Role      |
| ---------------------- | -------- | ------------------ | --------- |
| recruiter@techcorp.com | admin123 | TechCorp Solutions | Recruiter |
| hr@innovate.io         | admin123 | Innovate Labs      | Recruiter |
| admin@gradhire.com     | admin123 | GradHire           | Admin     |

> ⚠️ **Important**: These passwords are stored as hashes in the database. Your Java application must hash user input before comparing.

---

## 🧪 Running Test Queries

See `test_queries.sql` for comprehensive test queries including:
- Student profile retrieval
- Job search and filtering
- Skill matching
- Application management
- Analytics queries

Run test queries:
```bash
mysql -u root -p gradhire_db < /absolute/path/to/GradHire/sql/test_queries.sql
```

---

## 🔐 Security Best Practices

### For Development:
1. Use a separate database user (not root)
2. Use strong passwords
3. Keep passwords in environment variables or config files (not in code)
4. Don't commit passwords to version control

### For Production:
1. Enable SSL/TLS for database connections
2. Use prepared statements (never concatenate SQL)
3. Implement rate limiting on login attempts
4. Regular backups
5. Audit activity logs
6. Use environment-specific configurations

---

## 💾 Backup and Restore

### Create Backup

```bash
# Full database backup
mysqldump -u root -p gradhire_db > gradhire_backup_$(date +%Y%m%d).sql

# Backup without data (structure only)
mysqldump -u root -p --no-data gradhire_db > gradhire_structure.sql

# Backup specific tables
mysqldump -u root -p gradhire_db students admins > users_backup.sql
```

### Restore from Backup

```bash
# Restore full database
mysql -u root -p gradhire_db < gradhire_backup_20251024.sql

# Or from MySQL prompt
source /path/to/gradhire_backup_20251024.sql;
```

---

## 📈 Performance Tuning

### Check Index Usage

```sql
-- Show indexes on a table
SHOW INDEXES FROM students;

-- Analyze query performance
EXPLAIN SELECT * FROM jobs 
WHERE job_status = 'Active' AND domain = 'Software Development';
```

### Optimize Tables

```sql
-- Analyze tables for optimization
ANALYZE TABLE students, jobs, applications;

-- Optimize tables
OPTIMIZE TABLE students, jobs, applications;
```

---

## 🔄 Schema Updates

If you need to make changes to the schema after initial setup:

### Add a Column
```sql
ALTER TABLE students 
ADD COLUMN middle_name VARCHAR(50) AFTER full_name;
```

### Add an Index
```sql
CREATE INDEX idx_location ON jobs(location);
```

### Modify a Column
```sql
ALTER TABLE students 
MODIFY COLUMN phone VARCHAR(20);
```

> ⚠️ Always backup before making schema changes!

---

## 📞 Next Steps

After successfully setting up the database:

1. ✅ Verify all tables and data
2. ✅ Test the stored procedure
3. ✅ Configure database connection in Java
4. ✅ Test JDBC connectivity
5. ⏭️ Deploy WAR to Tomcat and validate `/auth/register`, `/auth/login`, and `/dashboard` flows

---

## 📚 Additional Resources

- [MySQL Official Documentation](https://dev.mysql.com/doc/)
- [JDBC Tutorial](https://docs.oracle.com/javase/tutorial/jdbc/)
- [SQL Performance Tuning](https://use-the-index-luke.com/)
- [Database Design Best Practices](https://www.vertabelo.com/blog/database-design-best-practices/)

---

## ✉️ Support

If you encounter any issues:
1. Check the [Troubleshooting](#-troubleshooting) section
2. Review `DATABASE_DESIGN.md` for schema details
3. Examine the `schema.sql` file comments
4. Test with sample queries from `test_queries.sql`

---

**Setup Guide Version**: 1.0  
**Last Updated**: October 24, 2025  
**Compatible with**: MySQL 8.0+
