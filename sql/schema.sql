-- =============================================
-- GradHire - Job Portal for College Students
-- MySQL Database Schema
-- =============================================

-- Drop database if exists (use with caution in production)
DROP DATABASE IF EXISTS gradhire_db;

-- Create database
CREATE DATABASE gradhire_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE gradhire_db;

-- =============================================
-- Table: students
-- Purpose: Store student user accounts and profiles
-- =============================================
CREATE TABLE students (
    student_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(15),
    college_name VARCHAR(150),
    degree VARCHAR(100),
    graduation_year INT,
    resume_path VARCHAR(255),
    profile_picture VARCHAR(255),
    bio TEXT,
    linkedin_url VARCHAR(255),
    github_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    INDEX idx_email (email),
    INDEX idx_graduation_year (graduation_year)
) ENGINE=InnoDB;

-- =============================================
-- Table: admins
-- Purpose: Store recruiter/admin accounts
-- =============================================
CREATE TABLE admins (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(100) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    company_name VARCHAR(150),
    company_website VARCHAR(255),
    phone VARCHAR(15),
    role ENUM('admin', 'recruiter') DEFAULT 'recruiter',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    is_active BOOLEAN DEFAULT TRUE,
    INDEX idx_email (email),
    INDEX idx_company (company_name)
) ENGINE=InnoDB;

-- =============================================
-- Table: skills
-- Purpose: Master list of skills/technologies
-- =============================================
CREATE TABLE skills (
    skill_id INT AUTO_INCREMENT PRIMARY KEY,
    skill_name VARCHAR(100) UNIQUE NOT NULL,
    category VARCHAR(50), -- e.g., 'Programming', 'Design', 'Marketing', 'Data Science'
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_skill_name (skill_name),
    INDEX idx_category (category)
) ENGINE=InnoDB;

-- =============================================
-- Table: student_skills
-- Purpose: Many-to-many relationship between students and skills
-- =============================================
CREATE TABLE student_skills (
    student_skill_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    skill_id INT NOT NULL,
    proficiency_level ENUM('Beginner', 'Intermediate', 'Advanced', 'Expert') DEFAULT 'Intermediate',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(skill_id) ON DELETE CASCADE,
    UNIQUE KEY unique_student_skill (student_id, skill_id),
    INDEX idx_student (student_id),
    INDEX idx_skill (skill_id)
) ENGINE=InnoDB;

-- =============================================
-- Table: jobs
-- Purpose: Store job/internship listings
-- =============================================
CREATE TABLE jobs (
    job_id INT AUTO_INCREMENT PRIMARY KEY,
    admin_id INT NOT NULL,
    job_title VARCHAR(200) NOT NULL,
    company_name VARCHAR(150) NOT NULL,
    job_type ENUM('Internship', 'Full-time', 'Part-time', 'Contract') NOT NULL,
    domain VARCHAR(100), -- e.g., 'Software Development', 'Data Science', 'Marketing'
    description TEXT NOT NULL,
    requirements TEXT,
    responsibilities TEXT,
    location VARCHAR(150),
    is_remote BOOLEAN DEFAULT FALSE,
    salary_min DECIMAL(10, 2),
    salary_max DECIMAL(10, 2),
    application_deadline DATE,
    job_status ENUM('Active', 'Closed', 'Draft') DEFAULT 'Active',
    views_count INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (admin_id) REFERENCES admins(admin_id) ON DELETE CASCADE,
    INDEX idx_admin (admin_id),
    INDEX idx_job_type (job_type),
    INDEX idx_domain (domain),
    INDEX idx_status (job_status),
    INDEX idx_deadline (application_deadline)
) ENGINE=InnoDB;

-- =============================================
-- Table: job_skills
-- Purpose: Many-to-many relationship between jobs and required skills
-- =============================================
CREATE TABLE job_skills (
    job_skill_id INT AUTO_INCREMENT PRIMARY KEY,
    job_id INT NOT NULL,
    skill_id INT NOT NULL,
    is_required BOOLEAN DEFAULT TRUE, -- TRUE for required, FALSE for nice-to-have
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (job_id) REFERENCES jobs(job_id) ON DELETE CASCADE,
    FOREIGN KEY (skill_id) REFERENCES skills(skill_id) ON DELETE CASCADE,
    UNIQUE KEY unique_job_skill (job_id, skill_id),
    INDEX idx_job (job_id),
    INDEX idx_skill (skill_id)
) ENGINE=InnoDB;

-- =============================================
-- Table: applications
-- Purpose: Track student applications to jobs
-- =============================================
CREATE TABLE applications (
    application_id INT AUTO_INCREMENT PRIMARY KEY,
    job_id INT NOT NULL,
    student_id INT NOT NULL,
    cover_letter TEXT,
    application_status ENUM('Pending', 'Reviewed', 'Shortlisted', 'Rejected', 'Accepted') DEFAULT 'Pending',
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    reviewed_at TIMESTAMP NULL,
    reviewer_notes TEXT,
    FOREIGN KEY (job_id) REFERENCES jobs(job_id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    UNIQUE KEY unique_application (job_id, student_id),
    INDEX idx_job (job_id),
    INDEX idx_student (student_id),
    INDEX idx_status (application_status),
    INDEX idx_applied_at (applied_at)
) ENGINE=InnoDB;

-- =============================================
-- Table: activity_logs
-- Purpose: Track user activities for audit and analytics
-- =============================================
CREATE TABLE activity_logs (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    user_type ENUM('student', 'admin') NOT NULL,
    user_id INT NOT NULL,
    activity_type VARCHAR(50) NOT NULL, -- e.g., 'login', 'job_post', 'application', 'profile_update'
    activity_description TEXT,
    ip_address VARCHAR(45),
    user_agent VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user (user_type, user_id),
    INDEX idx_activity_type (activity_type),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB;

-- =============================================
-- Table: saved_jobs
-- Purpose: Allow students to save/bookmark jobs for later
-- =============================================
CREATE TABLE saved_jobs (
    saved_job_id INT AUTO_INCREMENT PRIMARY KEY,
    student_id INT NOT NULL,
    job_id INT NOT NULL,
    saved_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES students(student_id) ON DELETE CASCADE,
    FOREIGN KEY (job_id) REFERENCES jobs(job_id) ON DELETE CASCADE,
    UNIQUE KEY unique_saved_job (student_id, job_id),
    INDEX idx_student (student_id),
    INDEX idx_job (job_id)
) ENGINE=InnoDB;

-- =============================================
-- SAMPLE DATA INSERTION
-- =============================================

-- Insert Skills (Master Data)
INSERT INTO skills (skill_name, category) VALUES
-- Programming Languages
('Java', 'Programming'),
('Python', 'Programming'),
('JavaScript', 'Programming'),
('C++', 'Programming'),
('C#', 'Programming'),
('PHP', 'Programming'),
('Ruby', 'Programming'),
('Go', 'Programming'),
('Kotlin', 'Programming'),
('Swift', 'Programming'),

-- Web Technologies
('HTML/CSS', 'Web Development'),
('React', 'Web Development'),
('Angular', 'Web Development'),
('Vue.js', 'Web Development'),
('Node.js', 'Web Development'),
('Spring Boot', 'Web Development'),
('Django', 'Web Development'),
('Flask', 'Web Development'),
('Bootstrap', 'Web Development'),
('Tailwind CSS', 'Web Development'),

-- Database
('MySQL', 'Database'),
('PostgreSQL', 'Database'),
('MongoDB', 'Database'),
('Oracle', 'Database'),
('SQL Server', 'Database'),
('Redis', 'Database'),

-- Data Science & ML
('Machine Learning', 'Data Science'),
('Deep Learning', 'Data Science'),
('TensorFlow', 'Data Science'),
('PyTorch', 'Data Science'),
('Data Analysis', 'Data Science'),
('Pandas', 'Data Science'),
('NumPy', 'Data Science'),
('Scikit-learn', 'Data Science'),

-- Cloud & DevOps
('AWS', 'Cloud'),
('Azure', 'Cloud'),
('Google Cloud', 'Cloud'),
('Docker', 'DevOps'),
('Kubernetes', 'DevOps'),
('Jenkins', 'DevOps'),
('Git', 'DevOps'),
('CI/CD', 'DevOps'),

-- Design & UI/UX
('Figma', 'Design'),
('Adobe XD', 'Design'),
('Photoshop', 'Design'),
('UI/UX Design', 'Design'),

-- Business & Marketing
('Digital Marketing', 'Marketing'),
('SEO', 'Marketing'),
('Content Writing', 'Marketing'),
('Social Media Marketing', 'Marketing'),
('Excel', 'Business'),
('Power BI', 'Business'),
('Tableau', 'Business');

-- Insert Admin/Recruiter Accounts
-- Password: admin123 (In production, these should be properly hashed)
INSERT INTO admins (email, password_hash, full_name, company_name, company_website, phone, role) VALUES
('recruiter@techcorp.com', '$2a$10$abcdefghijklmnopqrstuvwxyz123456789', 'Sarah Johnson', 'TechCorp Solutions', 'https://techcorp.com', '+1-555-0101', 'recruiter'),
('hr@innovate.io', '$2a$10$abcdefghijklmnopqrstuvwxyz987654321', 'Michael Chen', 'Innovate Labs', 'https://innovate.io', '+1-555-0102', 'recruiter'),
('admin@gradhire.com', '$2a$10$abcdefghijklmnopqrstuvwxyz111222333', 'Admin User', 'GradHire', 'https://gradhire.com', '+1-555-0100', 'admin'),
('jobs@dataanalytics.com', '$2a$10$abcdefghijklmnopqrstuvwxyz444555666', 'Emily Rodriguez', 'Data Analytics Inc', 'https://dataanalytics.com', '+1-555-0103', 'recruiter'),
('contact@designstudio.com', '$2a$10$abcdefghijklmnopqrstuvwxyz777888999', 'James Taylor', 'Creative Design Studio', 'https://designstudio.com', '+1-555-0104', 'recruiter');

-- Insert Student Accounts
-- Password: student123 (In production, these should be properly hashed)
INSERT INTO students (email, password_hash, full_name, phone, college_name, degree, graduation_year, bio, linkedin_url, github_url) VALUES
('john.doe@student.edu', '$2a$10$studenthash1234567890abcdef', 'John Doe', '+1-555-1001', 'MIT', 'B.Tech Computer Science', 2025, 'Passionate about full-stack development and AI', 'https://linkedin.com/in/johndoe', 'https://github.com/johndoe'),
('alice.smith@student.edu', '$2a$10$studenthash2234567890abcdef', 'Alice Smith', '+1-555-1002', 'Stanford University', 'B.S. Data Science', 2025, 'Data science enthusiast with strong Python skills', 'https://linkedin.com/in/alicesmith', 'https://github.com/alicesmith'),
('bob.wilson@student.edu', '$2a$10$studenthash3234567890abcdef', 'Bob Wilson', '+1-555-1003', 'UC Berkeley', 'B.S. Computer Science', 2026, 'Frontend developer interested in React and modern web tech', 'https://linkedin.com/in/bobwilson', 'https://github.com/bobwilson'),
('emma.davis@student.edu', '$2a$10$studenthash4234567890abcdef', 'Emma Davis', '+1-555-1004', 'Carnegie Mellon', 'B.Tech Software Engineering', 2025, 'Backend developer with experience in Java and Spring Boot', 'https://linkedin.com/in/emmadavis', 'https://github.com/emmadavis'),
('ryan.brown@student.edu', '$2a$10$studenthash5234567890abcdef', 'Ryan Brown', '+1-555-1005', 'Georgia Tech', 'B.S. Information Technology', 2026, 'Cloud computing and DevOps enthusiast', 'https://linkedin.com/in/ryanbrown', 'https://github.com/ryanbrown'),
('sophia.martinez@student.edu', '$2a$10$studenthash6234567890abcdef', 'Sophia Martinez', '+1-555-1006', 'University of Texas', 'B.Design UI/UX', 2025, 'Creative designer focused on user-centered design', 'https://linkedin.com/in/sophiamartinez', NULL),
('liam.anderson@student.edu', '$2a$10$studenthash7234567890abcdef', 'Liam Anderson', '+1-555-1007', 'UCLA', 'B.S. Computer Science', 2026, 'Machine learning and AI researcher', 'https://linkedin.com/in/liamanderson', 'https://github.com/liamanderson'),
('olivia.taylor@student.edu', '$2a$10$studenthash8234567890abcdef', 'Olivia Taylor', '+1-555-1008', 'Harvard University', 'B.A. Digital Marketing', 2025, 'Digital marketing specialist with SEO expertise', 'https://linkedin.com/in/oliviataylor', NULL);

-- Assign Skills to Students
INSERT INTO student_skills (student_id, skill_id, proficiency_level) VALUES
-- John Doe (Full-stack + AI)
(1, 1, 'Advanced'),   -- Java
(1, 2, 'Advanced'),   -- Python
(1, 3, 'Advanced'),   -- JavaScript
(1, 12, 'Advanced'),  -- React
(1, 16, 'Intermediate'), -- Spring Boot
(1, 21, 'Intermediate'), -- MySQL
(1, 27, 'Intermediate'), -- Machine Learning
(1, 41, 'Advanced'),  -- Git

-- Alice Smith (Data Science)
(2, 2, 'Expert'),     -- Python
(2, 27, 'Advanced'),  -- Machine Learning
(2, 28, 'Advanced'),  -- Deep Learning
(2, 29, 'Advanced'),  -- TensorFlow
(2, 31, 'Expert'),    -- Data Analysis
(2, 32, 'Expert'),    -- Pandas
(2, 33, 'Advanced'),  -- NumPy
(2, 34, 'Advanced'),  -- Scikit-learn
(2, 21, 'Intermediate'), -- MySQL
(2, 51, 'Advanced'),  -- Tableau

-- Bob Wilson (Frontend)
(3, 3, 'Expert'),     -- JavaScript
(3, 11, 'Advanced'),  -- HTML/CSS
(3, 12, 'Expert'),    -- React
(3, 14, 'Intermediate'), -- Vue.js
(3, 19, 'Advanced'),  -- Bootstrap
(3, 20, 'Intermediate'), -- Tailwind CSS
(3, 41, 'Advanced'),  -- Git

-- Emma Davis (Backend - Java/Spring)
(4, 1, 'Expert'),     -- Java
(4, 16, 'Advanced'),  -- Spring Boot
(4, 21, 'Advanced'),  -- MySQL
(4, 22, 'Intermediate'), -- PostgreSQL
(4, 15, 'Intermediate'), -- Node.js
(4, 38, 'Intermediate'), -- Docker
(4, 41, 'Advanced'),  -- Git

-- Ryan Brown (Cloud/DevOps)
(5, 2, 'Intermediate'), -- Python
(5, 35, 'Advanced'),  -- AWS
(5, 36, 'Advanced'),  -- Azure
(5, 38, 'Advanced'),  -- Docker
(5, 39, 'Advanced'),  -- Kubernetes
(5, 40, 'Intermediate'), -- Jenkins
(5, 41, 'Expert'),    -- Git
(5, 42, 'Advanced'),  -- CI/CD
(5, 21, 'Intermediate'), -- MySQL

-- Sophia Martinez (UI/UX Designer)
(6, 43, 'Expert'),    -- Figma
(6, 44, 'Advanced'),  -- Adobe XD
(6, 45, 'Advanced'),  -- Photoshop
(6, 46, 'Expert'),    -- UI/UX Design
(6, 11, 'Advanced'),  -- HTML/CSS
(6, 3, 'Intermediate'), -- JavaScript

-- Liam Anderson (ML/AI)
(7, 2, 'Expert'),     -- Python
(7, 27, 'Expert'),    -- Machine Learning
(7, 28, 'Expert'),    -- Deep Learning
(7, 29, 'Expert'),    -- TensorFlow
(7, 30, 'Advanced'),  -- PyTorch
(7, 32, 'Advanced'),  -- Pandas
(7, 33, 'Advanced'),  -- NumPy
(7, 4, 'Intermediate'), -- C++

-- Olivia Taylor (Digital Marketing)
(8, 47, 'Advanced'),  -- Digital Marketing
(8, 48, 'Expert'),    -- SEO
(8, 49, 'Advanced'),  -- Content Writing
(8, 50, 'Advanced'),  -- Social Media Marketing
(8, 3, 'Intermediate'), -- JavaScript
(8, 11, 'Intermediate'); -- HTML/CSS

-- Insert Job Postings
INSERT INTO jobs (admin_id, job_title, company_name, job_type, domain, description, requirements, responsibilities, location, is_remote, salary_min, salary_max, application_deadline, job_status) VALUES
(1, 'Software Development Intern', 'TechCorp Solutions', 'Internship', 'Software Development', 
'Join our dynamic team as a Software Development Intern. Work on real-world projects using modern technologies like Java, Spring Boot, and React.', 
'- Pursuing B.Tech/B.S. in Computer Science or related field\n- Strong knowledge of Java and OOP concepts\n- Familiarity with web development frameworks\n- Good problem-solving skills', 
'- Develop and maintain web applications\n- Collaborate with senior developers\n- Write clean, maintainable code\n- Participate in code reviews', 
'San Francisco, CA', FALSE, 20.00, 30.00, '2025-12-31', 'Active'),

(2, 'Data Science Intern', 'Innovate Labs', 'Internship', 'Data Science', 
'Exciting opportunity for data science enthusiasts to work on machine learning projects and big data analytics.', 
'- Strong Python programming skills\n- Knowledge of ML libraries (scikit-learn, TensorFlow)\n- Understanding of statistics and data analysis\n- Experience with Pandas and NumPy', 
'- Build and train ML models\n- Analyze large datasets\n- Create data visualizations\n- Present findings to stakeholders', 
'Remote', TRUE, 25.00, 35.00, '2025-11-30', 'Active'),

(1, 'Full Stack Developer', 'TechCorp Solutions', 'Full-time', 'Software Development', 
'We are seeking a talented Full Stack Developer to join our growing team. Work on cutting-edge projects with modern tech stack.', 
'- Bachelor\'s degree in Computer Science or equivalent\n- 0-2 years of experience\n- Proficiency in JavaScript, React, Node.js\n- Experience with RESTful APIs and databases', 
'- Design and develop full-stack applications\n- Work with cross-functional teams\n- Optimize application performance\n- Mentor junior developers', 
'Austin, TX', FALSE, 70000.00, 90000.00, '2025-12-15', 'Active'),

(4, 'Junior Data Analyst', 'Data Analytics Inc', 'Full-time', 'Data Analytics', 
'Entry-level position for recent graduates passionate about data analysis and business intelligence.', 
'- Bachelor\'s degree in relevant field\n- Strong Excel and SQL skills\n- Experience with Power BI or Tableau\n- Analytical mindset and attention to detail', 
'- Analyze business data and create reports\n- Build dashboards and visualizations\n- Support data-driven decision making\n- Collaborate with business teams', 
'New York, NY', FALSE, 60000.00, 75000.00, '2025-11-20', 'Active'),

(5, 'UI/UX Design Intern', 'Creative Design Studio', 'Internship', 'Design', 
'Creative internship opportunity for design students to work on user interface and user experience projects.', 
'- Portfolio demonstrating UI/UX work\n- Proficiency in Figma or Adobe XD\n- Understanding of design principles\n- Strong communication skills', 
'- Create wireframes and prototypes\n- Conduct user research\n- Design user interfaces\n- Collaborate with developers', 
'Los Angeles, CA', TRUE, 18.00, 25.00, '2025-12-10', 'Active'),

(2, 'Machine Learning Engineer', 'Innovate Labs', 'Full-time', 'Machine Learning', 
'Join our AI team to build next-generation machine learning solutions for enterprise clients.', 
'- Strong background in ML and deep learning\n- Expertise in Python, TensorFlow/PyTorch\n- Experience with model deployment\n- Understanding of MLOps practices', 
'- Develop and deploy ML models\n- Optimize model performance\n- Work with large-scale datasets\n- Research new ML techniques', 
'Seattle, WA', TRUE, 85000.00, 110000.00, '2026-01-15', 'Active'),

(1, 'DevOps Intern', 'TechCorp Solutions', 'Internship', 'DevOps', 
'Learn DevOps practices and cloud technologies in a fast-paced environment.', 
'- Basic knowledge of Linux and cloud platforms\n- Familiarity with Docker and CI/CD\n- Scripting skills (Python/Bash)\n- Eager to learn and adapt', 
'- Assist in CI/CD pipeline setup\n- Monitor cloud infrastructure\n- Automate deployment processes\n- Support development teams', 
'Remote', TRUE, 22.00, 28.00, '2025-11-25', 'Active'),

(4, 'Business Intelligence Analyst', 'Data Analytics Inc', 'Full-time', 'Business Intelligence', 
'Help organizations make data-driven decisions through advanced analytics and reporting.', 
'- Degree in Business, IT, or related field\n- Strong SQL and data modeling skills\n- Experience with BI tools (Tableau, Power BI)\n- Business acumen and communication skills', 
'- Design and build BI dashboards\n- Analyze business metrics\n- Create data models\n- Present insights to executives', 
'Chicago, IL', FALSE, 65000.00, 80000.00, '2025-12-20', 'Active'),

(5, 'Frontend Developer', 'Creative Design Studio', 'Full-time', 'Web Development', 
'Build beautiful and responsive web applications using modern frontend technologies.', 
'- Strong JavaScript and React skills\n- Experience with responsive design\n- Knowledge of HTML/CSS and modern frameworks\n- Eye for design and user experience', 
'- Develop responsive web interfaces\n- Collaborate with designers\n- Optimize frontend performance\n- Ensure cross-browser compatibility', 
'San Diego, CA', FALSE, 70000.00, 85000.00, '2025-12-05', 'Active'),

(3, 'Marketing Intern', 'GradHire', 'Internship', 'Digital Marketing', 
'Help us grow GradHire\'s presence through digital marketing campaigns and content creation.', 
'- Pursuing degree in Marketing or related field\n- Knowledge of SEO and social media\n- Strong writing and communication skills\n- Creative and analytical mindset', 
'- Create social media content\n- Assist in SEO optimization\n- Analyze marketing metrics\n- Support email campaigns', 
'Remote', TRUE, 15.00, 20.00, '2025-11-30', 'Active');

-- Assign Skills to Jobs
INSERT INTO job_skills (job_id, skill_id, is_required) VALUES
-- Job 1: Software Development Intern (TechCorp)
(1, 1, TRUE),   -- Java
(1, 16, TRUE),  -- Spring Boot
(1, 12, TRUE),  -- React
(1, 21, TRUE),  -- MySQL
(1, 41, TRUE),  -- Git

-- Job 2: Data Science Intern (Innovate Labs)
(2, 2, TRUE),   -- Python
(2, 27, TRUE),  -- Machine Learning
(2, 34, TRUE),  -- Scikit-learn
(2, 32, TRUE),  -- Pandas
(2, 33, TRUE),  -- NumPy
(2, 29, FALSE), -- TensorFlow (nice to have)

-- Job 3: Full Stack Developer (TechCorp)
(3, 3, TRUE),   -- JavaScript
(3, 12, TRUE),  -- React
(3, 15, TRUE),  -- Node.js
(3, 21, TRUE),  -- MySQL
(3, 41, TRUE),  -- Git
(3, 38, FALSE), -- Docker

-- Job 4: Junior Data Analyst (Data Analytics Inc)
(4, 21, TRUE),  -- MySQL
(4, 51, TRUE),  -- Excel
(4, 52, TRUE),  -- Power BI
(4, 31, TRUE),  -- Data Analysis
(4, 2, FALSE),  -- Python

-- Job 5: UI/UX Design Intern (Creative Design Studio)
(5, 43, TRUE),  -- Figma
(5, 46, TRUE),  -- UI/UX Design
(5, 44, FALSE), -- Adobe XD
(5, 11, FALSE), -- HTML/CSS

-- Job 6: Machine Learning Engineer (Innovate Labs)
(6, 2, TRUE),   -- Python
(6, 27, TRUE),  -- Machine Learning
(6, 28, TRUE),  -- Deep Learning
(6, 29, TRUE),  -- TensorFlow
(6, 30, TRUE),  -- PyTorch
(6, 38, FALSE), -- Docker

-- Job 7: DevOps Intern (TechCorp)
(7, 35, TRUE),  -- AWS
(7, 38, TRUE),  -- Docker
(7, 41, TRUE),  -- Git
(7, 42, TRUE),  -- CI/CD
(7, 2, FALSE),  -- Python

-- Job 8: Business Intelligence Analyst (Data Analytics Inc)
(8, 21, TRUE),  -- MySQL
(8, 52, TRUE),  -- Power BI
(8, 51, TRUE),  -- Tableau
(8, 31, TRUE),  -- Data Analysis
(8, 2, FALSE),  -- Python

-- Job 9: Frontend Developer (Creative Design Studio)
(9, 3, TRUE),   -- JavaScript
(9, 12, TRUE),  -- React
(9, 11, TRUE),  -- HTML/CSS
(9, 19, TRUE),  -- Bootstrap
(9, 41, TRUE),  -- Git

-- Job 10: Marketing Intern (GradHire)
(10, 47, TRUE), -- Digital Marketing
(10, 48, TRUE), -- SEO
(10, 49, TRUE), -- Content Writing
(10, 50, TRUE); -- Social Media Marketing

-- Insert Sample Applications
INSERT INTO applications (job_id, student_id, cover_letter, application_status, applied_at) VALUES
(1, 1, 'I am excited to apply for the Software Development Intern position. With my strong background in Java and Spring Boot, I believe I can contribute effectively to your team.', 'Reviewed', '2025-10-15 10:30:00'),
(1, 4, 'As a passionate backend developer with extensive Spring Boot experience, I am eager to join TechCorp Solutions.', 'Shortlisted', '2025-10-16 14:20:00'),
(2, 2, 'Data science is my passion, and I have hands-on experience with Python, TensorFlow, and Pandas. I would love to contribute to Innovate Labs.', 'Shortlisted', '2025-10-14 09:15:00'),
(2, 7, 'With my expertise in machine learning and deep learning, I am confident I can add value to your data science team.', 'Pending', '2025-10-20 11:00:00'),
(3, 1, 'I am applying for the Full Stack Developer position. My experience with React and Node.js makes me a great fit.', 'Pending', '2025-10-18 16:45:00'),
(3, 3, 'As a frontend specialist with React expertise, I am interested in expanding my skills to full-stack development.', 'Pending', '2025-10-19 13:30:00'),
(5, 6, 'I am thrilled to apply for the UI/UX Design Intern position. My portfolio showcases my design philosophy and user-centered approach.', 'Reviewed', '2025-10-17 10:00:00'),
(6, 7, 'With my research background in AI and expertise in TensorFlow and PyTorch, I am well-suited for the ML Engineer role.', 'Pending', '2025-10-21 15:20:00'),
(7, 5, 'DevOps is my forte. I have hands-on experience with AWS, Docker, and Kubernetes and am eager to learn more.', 'Pending', '2025-10-22 09:45:00'),
(10, 8, 'As a digital marketing specialist with SEO expertise, I would love to help GradHire grow its online presence.', 'Pending', '2025-10-23 14:00:00');

-- Insert Saved Jobs
INSERT INTO saved_jobs (student_id, job_id) VALUES
(1, 3),  -- John saved Full Stack Developer
(1, 6),  -- John saved ML Engineer
(2, 2),  -- Alice saved Data Science Intern
(2, 6),  -- Alice saved ML Engineer
(3, 9),  -- Bob saved Frontend Developer
(4, 1),  -- Emma saved Software Development Intern
(5, 7),  -- Ryan saved DevOps Intern
(6, 5),  -- Sophia saved UI/UX Design Intern
(7, 6),  -- Liam saved ML Engineer
(8, 10); -- Olivia saved Marketing Intern

-- Insert Activity Logs
INSERT INTO activity_logs (user_type, user_id, activity_type, activity_description, ip_address) VALUES
('student', 1, 'login', 'User logged in successfully', '192.168.1.100'),
('student', 1, 'application', 'Applied to job: Software Development Intern', '192.168.1.100'),
('student', 2, 'login', 'User logged in successfully', '192.168.1.101'),
('student', 2, 'application', 'Applied to job: Data Science Intern', '192.168.1.101'),
('admin', 1, 'login', 'Admin logged in successfully', '192.168.1.200'),
('admin', 1, 'job_post', 'Posted new job: Software Development Intern', '192.168.1.200'),
('admin', 2, 'login', 'Admin logged in successfully', '192.168.1.201'),
('admin', 2, 'job_post', 'Posted new job: Data Science Intern', '192.168.1.201'),
('student', 3, 'login', 'User logged in successfully', '192.168.1.102'),
('student', 4, 'profile_update', 'Updated profile information', '192.168.1.103'),
('admin', 1, 'application_review', 'Reviewed application for Software Development Intern', '192.168.1.200'),
('student', 5, 'login', 'User logged in successfully', '192.168.1.104'),
('student', 6, 'login', 'User logged in successfully', '192.168.1.105'),
('student', 7, 'application', 'Applied to job: Machine Learning Engineer', '192.168.1.106'),
('student', 8, 'login', 'User logged in successfully', '192.168.1.107');

-- =============================================
-- VIEWS FOR COMMON QUERIES
-- =============================================

-- View: Student profiles with skill count
CREATE VIEW vw_student_profiles AS
SELECT 
    s.student_id,
    s.email,
    s.full_name,
    s.college_name,
    s.degree,
    s.graduation_year,
    COUNT(ss.skill_id) as skill_count,
    s.created_at
FROM students s
LEFT JOIN student_skills ss ON s.student_id = ss.student_id
WHERE s.is_active = TRUE
GROUP BY s.student_id;

-- View: Active jobs with application count
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
LEFT JOIN applications a ON j.job_id = a.job_id
WHERE j.job_status = 'Active' AND j.application_deadline >= CURDATE()
GROUP BY j.job_id
ORDER BY j.created_at DESC;

-- View: Application details with student and job info
CREATE VIEW vw_application_details AS
SELECT 
    a.application_id,
    a.application_status,
    a.applied_at,
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
JOIN admins ad ON j.admin_id = ad.admin_id;

-- =============================================
-- STORED PROCEDURES
-- =============================================

-- Procedure: Get job recommendations for a student based on skill matching
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
            WHERE a.job_id = j.job_id AND a.student_id = p_student_id
        )
    GROUP BY j.job_id
    HAVING matching_skills > 0
    ORDER BY matching_skills DESC, j.created_at DESC
    LIMIT 10;
END //

DELIMITER ;

-- =============================================
-- SAMPLE QUERIES FOR TESTING
-- =============================================

-- Query 1: Get all active jobs with their required skills
-- SELECT j.job_title, j.company_name, GROUP_CONCAT(sk.skill_name) as required_skills
-- FROM jobs j
-- JOIN job_skills js ON j.job_id = js.job_id
-- JOIN skills sk ON js.skill_id = sk.skill_id
-- WHERE j.job_status = 'Active' AND js.is_required = TRUE
-- GROUP BY j.job_id;

-- Query 2: Get student profile with all skills
-- SELECT s.full_name, s.email, s.college_name, 
--        GROUP_CONCAT(CONCAT(sk.skill_name, ' (', ss.proficiency_level, ')') SEPARATOR ', ') as skills
-- FROM students s
-- JOIN student_skills ss ON s.student_id = ss.student_id
-- JOIN skills sk ON ss.skill_id = sk.skill_id
-- WHERE s.student_id = 1
-- GROUP BY s.student_id;

-- Query 3: Get job recommendations for a student (using stored procedure)
-- CALL sp_get_job_recommendations(1);

-- Query 4: Get application statistics by status
-- SELECT application_status, COUNT(*) as count
-- FROM applications
-- GROUP BY application_status;

-- Query 5: Get most popular skills (based on student profiles)
-- SELECT sk.skill_name, sk.category, COUNT(ss.student_id) as student_count
-- FROM skills sk
-- JOIN student_skills ss ON sk.skill_id = ss.skill_id
-- GROUP BY sk.skill_id
-- ORDER BY student_count DESC
-- LIMIT 10;

-- =============================================
-- END OF SCHEMA
-- =============================================

