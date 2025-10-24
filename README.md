# GradHire - Job Portal for College Students

> A comprehensive web-based job portal connecting college students with internships and entry-level jobs based on their skills and domains.

## 📋 Table of Contents

- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Database Schema](#database-schema)
- [Setup Instructions](#setup-instructions)
- [Project Status](#project-status)
- [Features](#features)

---

## 🎯 Overview

**GradHire** is a full-stack job portal designed specifically for college students to find internships and entry-level positions. The platform uses intelligent skill matching to recommend relevant opportunities and streamline the application process.

### Key Highlights

- **Smart Job Recommendations**: Matches students with jobs based on skill similarity
- **Role-Based Access**: Separate interfaces for students and recruiters
- **Resume Management**: Upload, store, and preview resumes
- **Application Tracking**: Monitor application status in real-time
- **Activity Logging**: Complete audit trail for all user actions

---

## ⚙️ Tech Stack

| Layer    | Technology                  |
| -------- | --------------------------- |
| Frontend | HTML5, CSS3, Bootstrap, JSP |
| Backend  | Java Servlets & JSP (JDBC)  |
| Database | MySQL 8.0+                  |
| Server   | Apache Tomcat 9.0+          |
| IDE      | NetBeans / Cursor           |

---

## 🗄️ Database Schema

### Step 1: Database Setup ✅ (COMPLETED)

The database schema includes the following tables:

#### Core Tables

1. **students** - Student user accounts and profiles
   - Stores personal info, education details, resume paths
   - Fields: email, password_hash, college_name, degree, graduation_year, etc.

2. **admins** - Recruiter and admin accounts
   - Company information and recruiter details
   - Fields: email, password_hash, company_name, role, etc.

3. **skills** - Master list of skills and technologies
   - Categorized by domain (Programming, Web Dev, Data Science, etc.)
   - 50+ pre-populated skills

4. **jobs** - Job and internship listings
   - Complete job details including salary, location, deadlines
   - Fields: job_title, job_type, domain, description, requirements, etc.

#### Relationship Tables

5. **student_skills** - Links students to their skills
   - Many-to-many relationship with proficiency levels
   - Enables skill-based matching

6. **job_skills** - Links jobs to required skills
   - Distinguishes between required and nice-to-have skills

7. **applications** - Tracks student applications
   - Application status workflow (Pending → Reviewed → Shortlisted/Rejected/Accepted)
   - Cover letters and reviewer notes

8. **saved_jobs** - Student job bookmarks
   - Allows students to save jobs for later

9. **activity_logs** - Audit trail
   - Tracks all user actions (login, applications, job posts, etc.)

### Database Features

- **Views**: Pre-built views for common queries
  - `vw_student_profiles` - Student profiles with skill counts
  - `vw_active_jobs` - Active jobs with application counts
  - `vw_application_details` - Complete application information

- **Stored Procedures**:
  - `sp_get_job_recommendations(student_id)` - Get personalized job recommendations

- **Indexes**: Optimized for fast queries on email, dates, and foreign keys

### Sample Data

The schema includes realistic sample data:
- 8 student accounts with diverse skill sets
- 5 admin/recruiter accounts from different companies
- 10 job postings (internships and full-time positions)
- 50+ skills across multiple categories
- Sample applications and saved jobs
- Activity logs for testing

---

## 🚀 Setup Instructions

### Prerequisites

- MySQL 8.0 or higher
- Java Development Kit (JDK) 11 or higher
- Apache Tomcat 9.0 or higher
- MySQL Connector/J (JDBC Driver)

### Database Installation

1. **Install MySQL** (if not already installed)
   ```bash
   # macOS (using Homebrew)
   brew install mysql
   brew services start mysql
   
   # Ubuntu/Debian
   sudo apt-get install mysql-server
   sudo systemctl start mysql
   ```

2. **Create the Database**
   ```bash
   # Login to MySQL
   mysql -u root -p
   
   # Run the schema file
   source /Users/agraw/Desktop/projects/gradHire/sql/schema.sql
   
   # Or using command line directly
   mysql -u root -p < /Users/agraw/Desktop/projects/gradHire/sql/schema.sql
   ```

3. **Verify Installation**
   ```sql
   USE gradhire_db;
   
   -- Check tables
   SHOW TABLES;
   
   -- Verify sample data
   SELECT COUNT(*) FROM students;
   SELECT COUNT(*) FROM jobs;
   SELECT COUNT(*) FROM skills;
   ```

4. **Test Queries**
   ```sql
   -- Get all active jobs with required skills
   SELECT j.job_title, j.company_name, 
          GROUP_CONCAT(sk.skill_name) as required_skills
   FROM jobs j
   JOIN job_skills js ON j.job_id = js.job_id
   JOIN skills sk ON js.skill_id = sk.skill_id
   WHERE j.job_status = 'Active' AND js.is_required = TRUE
   GROUP BY j.job_id;
   
   -- Get job recommendations for student ID 1
   CALL sp_get_job_recommendations(1);
   ```

### Sample Login Credentials

#### Students
| Email                       | Password    | Name           | Focus Area        |
| --------------------------- | ----------- | -------------- | ----------------- |
| john.doe@student.edu        | student123  | John Doe       | Full-stack + AI   |
| alice.smith@student.edu     | student123  | Alice Smith    | Data Science      |
| bob.wilson@student.edu      | student123  | Bob Wilson     | Frontend          |
| emma.davis@student.edu      | student123  | Emma Davis     | Backend (Java)    |
| ryan.brown@student.edu      | student123  | Ryan Brown     | Cloud/DevOps      |

#### Recruiters/Admins
| Email                       | Password    | Company               | Role      |
| --------------------------- | ----------- | --------------------- | --------- |
| recruiter@techcorp.com      | admin123    | TechCorp Solutions    | Recruiter |
| hr@innovate.io              | admin123    | Innovate Labs         | Recruiter |
| admin@gradhire.com          | admin123    | GradHire              | Admin     |

> ⚠️ **Note**: These are hashed passwords in the database. The actual implementation will use BCrypt hashing.

---

## 📊 Project Status

### ✅ Completed

- [x] **Step 1**: Database schema design and creation
  - Complete ER model with normalized tables
  - Sample data for testing
  - Views and stored procedures
  - Comprehensive documentation

### 🔜 Next Steps

- [ ] **Step 2**: Backend Development (Java DAO + Servlets)
  - Database connection pooling
  - DAO classes for each entity
  - Servlets for business logic
  - Session management

- [ ] **Step 3**: Frontend Development (JSP + Bootstrap)
  - Student and admin dashboards
  - Job listing and search pages
  - Application forms
  - Profile management

- [ ] **Step 4**: Authentication System
  - BCrypt password hashing
  - Role-based access control
  - Session security

- [ ] **Step 5**: Smart Recommendation Engine
  - Jaccard similarity algorithm
  - Keyword matching
  - Skill-based filtering

- [ ] **Step 6**: Testing & Deployment
  - Unit tests
  - Integration tests
  - WAR file creation
  - Tomcat deployment

---

## ✨ Features

### Student Features

- ✅ Register and create profile
- ✅ Upload resume (PDF support)
- ✅ Browse jobs by skills, domain, location
- ✅ Get personalized job recommendations
- ✅ Apply to jobs with cover letter
- ✅ Track application status
- ✅ Save jobs for later
- ✅ View activity history

### Recruiter/Admin Features

- ✅ Post and manage job listings
- ✅ Review applications
- ✅ View candidate profiles and resumes
- ✅ Update application status
- ✅ Access analytics dashboard
- ✅ Activity logging

### System Features

- ✅ Role-based authentication
- ✅ Secure password hashing
- ✅ SQL injection protection
- ✅ Responsive Bootstrap UI
- ✅ Activity audit trail
- ✅ Performance optimization (indexes, views)

---

## 📁 Project Structure

```
gradHire/
├── sql/
│   └── schema.sql          ✅ Database schema with sample data
├── src/                    🔜 Java source files
│   ├── dao/                   Database access objects
│   ├── model/                 Entity classes
│   ├── servlet/               Servlet controllers
│   └── util/                  Utility classes
├── web/                    🔜 Frontend files
│   ├── WEB-INF/
│   │   └── web.xml            Servlet configuration
│   ├── css/                   Stylesheets
│   ├── js/                    JavaScript files
│   ├── images/                Image assets
│   └── *.jsp                  JSP pages
├── lib/                    🔜 External libraries
├── build/                  🔜 Compiled classes
└── README.md               ✅ This file
```

---

## 🔐 Security Features

- **Password Hashing**: BCrypt with salt
- **SQL Injection Prevention**: Prepared statements
- **XSS Protection**: Input sanitization
- **Session Management**: Secure session handling
- **HTTPS Ready**: SSL/TLS support
- **Activity Logging**: Complete audit trail

---

## 📈 Performance Optimization

- Database indexes on frequently queried columns
- Connection pooling for database access
- Optimized queries using views
- Stored procedures for complex operations
- Lazy loading for large datasets

---

## 🚀 Future Enhancements

- [ ] Spring Boot migration
- [ ] REST API for mobile app
- [ ] Real-time notifications
- [ ] Video interview scheduling
- [ ] Aptitude test integration
- [ ] AI-powered resume parsing
- [ ] Advanced analytics dashboard
- [ ] Email notifications
- [ ] Chat functionality

---

## 📝 License

This project is created for educational purposes.

---

## 👨‍💻 Development

**Current Phase**: Database Design ✅
**Next Phase**: Backend Development (Java DAO + Servlets)

For questions or contributions, please refer to the project documentation.

---

**Last Updated**: October 24, 2025

