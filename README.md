# 🎓 GradHire - Smart Job Portal for College Students

> **Connecting talented students with their dream opportunities through intelligent skill-based matching**

[![Status](https://img.shields.io/badge/status-production%20ready-brightgreen)]()
[![Version](https://img.shields.io/badge/version-1.0-blue)]()
[![Java](https://img.shields.io/badge/java-8%2B-orange)]()
[![License](https://img.shields.io/badge/license-MIT-green)]()

> 🧪 **Latest Test Results**: [FINAL_TEST_REPORT.md](FINAL_TEST_REPORT.md) - **71/71 tests passed (100%)** ✅  
> 🎉 **Build Status**: [COMPILATION_SUCCESS.md](COMPILATION_SUCCESS.md) - **0 errors, WAR ready** ✅

---

## 📖 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Quick Start](#quick-start)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Running the Application](#running-the-application)
- [Project Structure](#project-structure)
- [Architecture](#architecture)
- [API Endpoints](#api-endpoints)
- [Database Schema](#database-schema)
- [Documentation](#documentation)
- [Contributing](#contributing)
- [License](#license)

---

## 🌟 Overview

**GradHire** is a comprehensive job portal designed specifically for college students and recent graduates. It uses **intelligent skill-based matching** to connect students with relevant internships and entry-level positions, streamlining the job search process for both candidates and recruiters.

### Why GradHire?

- 🎯 **Smart Recommendations**: AI-powered matching using Jaccard similarity and keyword algorithms
- 🔒 **Secure Authentication**: Role-based access control with BCrypt password hashing
- 📊 **Real-time Dashboard**: Track applications, view analytics, and manage profiles
- 🚀 **Modern UI**: Responsive design with Bootstrap 5
- 💼 **Dual Portal**: Separate interfaces for students and recruiters
- 📝 **Resume Management**: Built-in resume upload and management system

---

## ✨ Features

### For Students

- ✅ **Personalized Job Recommendations** - Smart matching based on skills and profile
- ✅ **One-Click Applications** - Apply to jobs with resume and cover letter
- ✅ **Application Tracking** - Monitor application status in real-time
- ✅ **Skill Management** - Add and showcase your technical skills
- ✅ **Profile Builder** - Complete profile with education and experience
- ✅ **Dashboard Analytics** - View application statistics and insights

### For Recruiters/Admins

- ✅ **Job Posting** - Create detailed job listings with requirements
- ✅ **Application Management** - Review, shortlist, and manage candidates
- ✅ **Candidate Filtering** - Filter by skills, education, and experience
- ✅ **Analytics Dashboard** - Track job performance and applicant metrics
- ✅ **Bulk Operations** - Manage multiple applications efficiently

### Technical Features

- ✅ **Smart Recommendation Engine** - Jaccard similarity + keyword matching
- ✅ **Soft Delete** - Data integrity with audit trails
- ✅ **Session Management** - Secure 30-minute sessions with HTTP-only cookies
- ✅ **File Upload** - Resume and profile picture uploads (10MB limit)
- ✅ **Error Handling** - Comprehensive error pages and logging
- ✅ **Database Pooling** - Optimized connection management
- ✅ **Input Validation** - Client and server-side validation
- ✅ **Responsive Design** - Mobile-friendly interface

---

## 🛠️ Technology Stack

### Backend

```
☕ Java 8+
🌐 Java Servlets & JSP
🔧 Apache Tomcat 9+
🗄️ MySQL 8.0+
🔌 JDBC with Connection Pooling
🔐 BCrypt for password hashing
```

### Frontend

```
🎨 Bootstrap 5
📝 JSP with JSTL
💻 JavaScript (ES6+)
🎭 Font Awesome Icons
📱 Responsive Design
```

### Build & Deployment

```
🏗️ Apache Ant
📦 WAR Packaging
🚀 Tomcat Deployment
⚙️ Environment Configuration
```

### Dependencies

```
mysql-connector-java-8.0.33.jar   - MySQL JDBC Driver
commons-dbcp2-2.9.0.jar           - Connection Pooling
commons-pool2-2.11.1.jar          - Object Pooling
commons-logging-1.2.jar           - Logging Framework
jbcrypt-0.4.jar                   - Password Hashing
jstl-1.2.jar                      - JSP Tag Library
```

---

## 🚀 Quick Start

### Prerequisites

- **Java JDK 8+**
- **Apache Tomcat 9+**
- **MySQL 8.0+**
- **Apache Ant** (for building)

### Installation (5 Minutes)

```bash
# 1. Clone the repository
git clone https://github.com/yourusername/gradhire.git
cd gradhire

# 2. Setup database
mysql -u root -p
CREATE DATABASE gradhire_db;
CREATE USER 'gradhire_user'@'localhost' IDENTIFIED BY 'your_password';
GRANT ALL PRIVILEGES ON gradhire_db.* TO 'gradhire_user'@'localhost';
exit;

# Import schema
mysql -u gradhire_user -p gradhire_db < sql/schema.sql

# 3. Download dependencies to lib/
# See SETUP_GUIDE.md for download links

# 4. Configure database connection
cp database.properties.template database.properties
# Edit database.properties with your credentials

# 5. Build and deploy
ant clean war
cp dist/gradhire.war $CATALINA_HOME/webapps/

# 6. Start Tomcat
$CATALINA_HOME/bin/startup.sh

# 7. Access the application
open http://localhost:8080/gradhire
```

**That's it!** You should see the GradHire landing page.

---

## 📋 Prerequisites

### Software Requirements

| Software | Version | Purpose |
|----------|---------|---------|
| Java JDK | 8 or higher | Runtime environment |
| Apache Tomcat | 9.0 or higher | Application server |
| MySQL | 8.0 or higher | Database |
| Apache Ant | 1.10 or higher | Build tool (optional) |

### Environment Variables

```bash
# Required
export CATALINA_HOME=/usr/local/tomcat
export JAVA_HOME=/path/to/java

# Optional (for development)
export GRADHIRE_ENV=development
```

---

## 💻 Installation

### Step 1: Install Java

```bash
# macOS
brew install openjdk@11

# Ubuntu/Debian
sudo apt-get update
sudo apt-get install openjdk-11-jdk

# Verify
java -version
javac -version
```

### Step 2: Install Apache Tomcat

```bash
# macOS
brew install tomcat

# Linux (manual)
cd /usr/local
sudo wget https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.80/bin/apache-tomcat-9.0.80.tar.gz
sudo tar xzf apache-tomcat-9.0.80.tar.gz
sudo mv apache-tomcat-9.0.80 tomcat

# Set permissions
chmod +x $CATALINA_HOME/bin/*.sh

# Set environment variable
echo 'export CATALINA_HOME=/usr/local/tomcat' >> ~/.bashrc
source ~/.bashrc
```

### Step 3: Install MySQL

```bash
# macOS
brew install mysql
brew services start mysql

# Ubuntu/Debian
sudo apt-get install mysql-server
sudo systemctl start mysql

# Secure installation
sudo mysql_secure_installation
```

### Step 4: Download Dependencies

Place these JARs in the `lib/` directory:

```bash
cd gradhire/lib

# MySQL Connector
wget https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar

# Apache Commons DBCP2
wget https://repo1.maven.org/maven2/org/apache/commons/commons-dbcp2/2.9.0/commons-dbcp2-2.9.0.jar

# Apache Commons Pool2
wget https://repo1.maven.org/maven2/org/apache/commons/commons-pool2/2.11.1/commons-pool2-2.11.1.jar

# Commons Logging
wget https://repo1.maven.org/maven2/commons-logging/commons-logging/1.2/commons-logging-1.2.jar

# jBCrypt
wget https://repo1.maven.org/maven2/org/mindrot/jbcrypt/0.4/jbcrypt-0.4.jar

# JSTL
wget https://repo1.maven.org/maven2/javax/servlet/jstl/1.2/jstl-1.2.jar
```

### Step 5: Configure Database

```bash
# Create database and user
mysql -u root -p << EOF
CREATE DATABASE gradhire_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'gradhire_user'@'localhost' IDENTIFIED BY 'GradHire2025!';
GRANT ALL PRIVILEGES ON gradhire_db.* TO 'gradhire_user'@'localhost';
FLUSH PRIVILEGES;
EOF

# Import schema
mysql -u gradhire_user -p gradhire_db < sql/schema.sql
```

### Step 6: Configure Application

```bash
# Copy and edit database properties
cp database.properties.template database.properties
nano database.properties
```

Edit with your credentials:
```properties
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/gradhire_db?useSSL=false&serverTimezone=UTC
db.username=gradhire_user
db.password=GradHire2025!
db.initialSize=5
db.maxTotal=20
```

---

## ▶️ Running the Application

### Build and Deploy

```bash
# Using Ant (recommended)
ant clean war      # Build WAR file
ant deploy         # Deploy to Tomcat

# Or manually
cp dist/gradhire.war $CATALINA_HOME/webapps/
```

### Start Tomcat

```bash
# Start server
$CATALINA_HOME/bin/startup.sh

# View logs
tail -f $CATALINA_HOME/logs/catalina.out

# Stop server
$CATALINA_HOME/bin/shutdown.sh
```

### Access Application

```
Landing Page:    http://localhost:8080/gradhire
Login:           http://localhost:8080/gradhire/login
Register:        http://localhost:8080/gradhire/register
Job Listings:    http://localhost:8080/gradhire/jobs
```

### Test Accounts

Create test accounts via the registration page or use SQL:

```sql
-- Student account (password: student123)
INSERT INTO students (email, password_hash, full_name) VALUES 
('student@test.com', '$2a$12$...', 'Test Student');

-- Admin account (password: admin123)
INSERT INTO admins (email, password_hash, full_name) VALUES 
('admin@test.com', '$2a$12$...', 'Test Admin');
```

---

## 📁 Project Structure

```
gradhire/
├── src/                          # Java source code
│   └── com/gradhire/
│       ├── dao/                  # Data Access Objects (6)
│       │   ├── StudentDAO.java
│       │   ├── AdminDAO.java
│       │   ├── JobDAO.java
│       │   ├── ApplicationDAO.java
│       │   └── SkillDAO.java
│       ├── filter/               # Security filters (2)
│       │   ├── AuthenticationFilter.java
│       │   └── AuthorizationFilter.java
│       ├── model/                # Data models (8)
│       │   ├── Student.java
│       │   ├── Admin.java
│       │   ├── Job.java
│       │   ├── Application.java
│       │   └── Skill.java
│       ├── servlet/              # HTTP servlets (10)
│       │   ├── LoginServlet.java
│       │   ├── RegisterServlet.java
│       │   ├── JobListingServlet.java
│       │   └── ...
│       ├── util/                 # Utility classes (5)
│       │   ├── DBConnection.java
│       │   ├── PasswordHasher.java
│       │   ├── SessionManager.java
│       │   ├── Validator.java
│       │   └── RecommendationEngine.java
│       └── exception/            # Custom exceptions
│           └── DataAccessException.java
│
├── web/                          # Web resources
│   ├── WEB-INF/
│   │   ├── web.xml              # Deployment descriptor
│   │   └── jsp/                 # JSP pages
│   │       ├── admin/           # Admin pages
│   │       ├── student/         # Student pages
│   │       ├── jobs/            # Job pages
│   │       └── error/           # Error pages
│   ├── css/                     # Stylesheets
│   ├── js/                      # JavaScript files
│   ├── images/                  # Static images
│   ├── uploads/                 # User uploads
│   └── index.jsp                # Landing page
│
├── sql/                          # Database scripts
│   ├── schema.sql               # Database schema
│   └── test_queries.sql         # Sample queries
│
├── lib/                          # External dependencies
│   ├── mysql-connector-java-8.0.33.jar
│   ├── commons-dbcp2-2.9.0.jar
│   └── ...
│
├── build.xml                     # Ant build script
├── build.properties              # Build configuration
├── database.properties.template  # DB config template
├── README.md                     # This file
├── SETUP_GUIDE.md               # Detailed setup guide
├── DEPLOYMENT_GUIDE.md          # Deployment instructions
└── DEVELOPER_GUIDE.md           # Development guide
```

---

## 🏗️ Architecture

### High-Level Architecture

```
┌─────────────┐
│   Browser   │
└──────┬──────┘
       │ HTTP
┌──────▼──────────────────────────┐
│   Apache Tomcat                 │
│  ┌────────────────────────────┐ │
│  │  Filters (Security)        │ │
│  │  - Authentication          │ │
│  │  - Authorization           │ │
│  └────────────┬───────────────┘ │
│  ┌────────────▼───────────────┐ │
│  │  Servlets (Controllers)    │ │
│  │  - Login, Register         │ │
│  │  - Jobs, Applications      │ │
│  └────────────┬───────────────┘ │
│  ┌────────────▼───────────────┐ │
│  │  DAOs (Data Layer)         │ │
│  │  - Database operations     │ │
│  └────────────┬───────────────┘ │
└───────────────┼─────────────────┘
                │ JDBC
       ┌────────▼────────┐
       │  MySQL Database │
       └─────────────────┘
```

### Design Patterns

- **MVC Pattern**: Model-View-Controller architecture
- **DAO Pattern**: Data Access Objects for database operations
- **Singleton Pattern**: Database connection management
- **Factory Pattern**: Object creation in DAOs
- **Filter Chain**: Security filter pipeline

### Key Components

1. **Filters**: Authentication and authorization
2. **Servlets**: Request handling and routing
3. **DAOs**: Database CRUD operations
4. **Models**: Data entities
5. **Utilities**: Helper classes (hashing, validation, etc.)
6. **Views**: JSP pages with JSTL

---

## 🔌 API Endpoints

### Public Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/` | Landing page |
| GET | `/login` | Login page |
| POST | `/login` | Authenticate user |
| GET | `/register` | Registration page |
| POST | `/register` | Create new account |
| GET | `/jobs` | List all jobs |
| GET | `/job/{id}` | Job details |

### Student Endpoints (Protected)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/student/dashboard` | Student dashboard |
| GET | `/student/profile` | View/edit profile |
| POST | `/student/profile` | Update profile |
| POST | `/apply` | Submit job application |

### Admin Endpoints (Protected)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/admin/dashboard` | Admin dashboard |
| GET | `/admin/post-job` | Job posting form |
| POST | `/admin/post-job` | Create new job |

### Common Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET/POST | `/logout` | Logout user |

---

## 🗄️ Database Schema

### Core Tables

```sql
students          -- Student accounts and profiles
admins            -- Admin/recruiter accounts
jobs              -- Job postings
applications      -- Job applications
skills            -- Master skills list
student_skills    -- Student skill associations
job_skills        -- Job skill requirements
```

### Key Relationships

```
students (1) ──── (N) applications
jobs (1) ──── (N) applications
students (N) ──── (N) skills (through student_skills)
jobs (N) ──── (N) skills (through job_skills)
admins (1) ──── (N) jobs
```

### Entity Relationship Diagram

See `sql/DATABASE_DESIGN.md` for complete schema documentation.

---

## 📚 Documentation

### Available Guides

| Document | Description |
|----------|-------------|
| [README.md](README.md) | This file - project overview |
| [SETUP_GUIDE.md](SETUP_GUIDE.md) | Detailed installation instructions |
| [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md) | Production deployment guide |
| [DEVELOPER_GUIDE.md](DEVELOPER_GUIDE.md) | Development and contribution guide |
| [CHANGELOG.md](CHANGELOG.md) | Version history |

### Additional Resources

- **Database Design**: `sql/DATABASE_DESIGN.md`
- **API Documentation**: In DEVELOPER_GUIDE.md
- **Troubleshooting**: In DEPLOYMENT_GUIDE.md

---

## 🤝 Contributing

We welcome contributions! Please see [DEVELOPER_GUIDE.md](DEVELOPER_GUIDE.md) for:

- Development setup
- Coding standards
- Testing guidelines
- Pull request process

### Quick Contribution Guide

```bash
# 1. Fork the repository
# 2. Create a feature branch
git checkout -b feature/amazing-feature

# 3. Make your changes
# 4. Commit with descriptive messages
git commit -m "Add amazing feature"

# 5. Push to your fork
git push origin feature/amazing-feature

# 6. Open a Pull Request
```

---

## 📊 Project Stats

```
Lines of Code:       ~8,500
Java Files:          32
JSP Pages:           15+
Database Tables:     11
Servlets:            10
Features:            Smart recommendations, auth, dashboards
Test Coverage:       100% (core algorithms)
Documentation:       Comprehensive
Status:              Production Ready
```

---

## 🔒 Security

- ✅ BCrypt password hashing (12 rounds)
- ✅ Session-based authentication
- ✅ HTTP-only cookies
- ✅ CSRF protection ready
- ✅ SQL injection prevention (PreparedStatements)
- ✅ XSS protection (JSTL escaping)
- ✅ Role-based access control
- ✅ Input validation

---

## 📈 Performance

- ✅ Database connection pooling (max 20 connections)
- ✅ Optimized SQL queries with indexes
- ✅ Session timeout (30 minutes)
- ✅ Resource caching
- ✅ Efficient recommendation algorithm (O(n×m))

---

## 🐛 Troubleshooting

### Common Issues

**Issue: Port 8080 already in use**
```bash
lsof -i :8080
kill -9 <PID>
```

**Issue: Database connection failed**
```bash
# Check MySQL is running
sudo systemctl status mysql

# Test connection
mysql -u gradhire_user -p gradhire_db
```

**Issue: ClassNotFoundException**
```bash
# Ensure all JARs are in lib/
ls -l lib/

# Rebuild
ant clean all
```

For more troubleshooting, see [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md).

---

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👥 Authors

**GradHire Development Team**

- Initial work and architecture
- Smart recommendation algorithm
- Full-stack implementation

---

## 🙏 Acknowledgments

- Bootstrap team for the UI framework
- Apache Foundation for Tomcat and Commons libraries
- MySQL team for the database
- jBCrypt for password hashing
- All contributors and testers

---

## 📞 Support

- **Documentation**: See guides in the repo
- **Issues**: Open a GitHub issue
- **Email**: support@gradhire.example.com

---

## 🗺️ Roadmap

### v1.0 (Current)
- ✅ Core job portal functionality
- ✅ Smart recommendations
- ✅ Authentication & authorization
- ✅ Student and admin dashboards

### v1.1 (Planned)
- 📧 Email notifications
- 💬 Chat/messaging system
- 📊 Advanced analytics
- 🔍 Elasticsearch integration

### v2.0 (Future)
- 🤖 Machine learning recommendations
- 📱 Mobile app (React Native)
- 🌐 Internationalization
- ☁️ Cloud deployment

---

## ⭐ Star History

If you find this project useful, please consider giving it a star!

---

**Built with ❤️ for college students everywhere**

*Last Updated: October 24, 2025*
