# GradHire - Quick Start Guide 🚀

Get the GradHire database up and running in 5 minutes!

---

## ⚡ Prerequisites

- MySQL 8.0+ installed
- MySQL running on localhost:3306
- MySQL root password

---

## 🏃 Quick Setup (3 Steps)

### Step 1: Create the Database

Open terminal and run:

```bash
mysql -u root -p < /Users/agraw/Desktop/projects/gradHire/sql/schema.sql
```

Enter your MySQL password when prompted.

### Step 2: Verify Installation

```bash
mysql -u root -p -e "USE gradhire_db; SHOW TABLES;"
```

You should see 9 tables and 3 views.

### Step 3: Test the Database

```bash
mysql -u root -p gradhire_db < /Users/agraw/Desktop/projects/gradHire/sql/test_queries.sql
```

---

## ✅ What You Get

- **8 Student Accounts** - Diverse profiles with skills
- **5 Recruiter Accounts** - From different companies  
- **10 Job Postings** - Mix of internships and full-time
- **51 Skills** - Categorized and ready to use
- **Sample Applications** - To test the workflow
- **Activity Logs** - For audit testing

---

## 🎯 Test Logins

### Students:
- `john.doe@student.edu` / `student123` - Full-stack developer
- `alice.smith@student.edu` / `student123` - Data scientist
- `bob.wilson@student.edu` / `student123` - Frontend developer

### Recruiters:
- `recruiter@techcorp.com` / `admin123` - TechCorp Solutions
- `hr@innovate.io` / `admin123` - Innovate Labs
- `admin@gradhire.com` / `admin123` - Platform Admin

> ⚠️ Passwords are hashed in DB - hash user input before comparing!

---

## 🔍 Quick Queries to Try

### Get all active jobs:
```sql
SELECT job_title, company_name, job_type, location 
FROM jobs 
WHERE job_status = 'Active';
```

### Get student with skills:
```sql
SELECT s.full_name, GROUP_CONCAT(sk.skill_name) as skills
FROM students s
JOIN student_skills ss ON s.student_id = ss.student_id
JOIN skills sk ON ss.skill_id = sk.skill_id
WHERE s.student_id = 1;
```

### Get job recommendations for a student:
```sql
CALL sp_get_job_recommendations(1);
```

---

## 📚 What's Next?

1. ✅ **Step 1 Complete** - Database is ready!
2. ⏭️ **Step 2** - Build Java backend (DAO + Servlets)
3. ⏭️ **Step 3** - Create JSP frontend pages
4. ⏭️ **Step 4** - Implement authentication
5. ⏭️ **Step 5** - Add recommendation engine
6. ⏭️ **Step 6** - Deploy to Tomcat

---

## 📖 Full Documentation

- **README.md** - Complete project overview
- **sql/SETUP_GUIDE.md** - Detailed setup instructions
- **sql/DATABASE_DESIGN.md** - Schema documentation
- **sql/test_queries.sql** - 50+ test queries
- **STEP1_COMPLETION.md** - What we built in Step 1

---

## 🆘 Problems?

### Can't connect to MySQL?
```bash
# Check if MySQL is running
mysql --version
mysql -u root -p -e "SELECT 1;"
```

### Forgot MySQL password?
```bash
# Reset MySQL root password (varies by OS)
sudo mysql
ALTER USER 'root'@'localhost' IDENTIFIED BY 'new_password';
```

### Database already exists?
```sql
-- Drop and recreate (WARNING: deletes all data!)
DROP DATABASE IF EXISTS gradhire_db;
-- Then re-run schema.sql
```

---

## 🎉 Success!

If you see tables and data, you're all set! 

**Database Name**: `gradhire_db`  
**Tables**: 9 core tables  
**Views**: 3 helper views  
**Stored Procedures**: 1 recommendation engine  
**Sample Data**: 140+ records ready to test  

---

**Next**: Proceed to Step 2 - Backend Development!

Happy Coding! 💻

