# 👨‍💻 GradHire Developer Guide

Complete guide for developers contributing to or extending GradHire.

---

## 📋 Table of Contents

1. [Development Setup](#development-setup)
2. [Code Structure](#code-structure)
3. [Coding Standards](#coding-standards)
4. [Adding New Features](#adding-new-features)
5. [Testing](#testing)
6. [Debugging](#debugging)
7. [Contributing](#contributing)
8. [API Reference](#api-reference)

---

## 🛠️ Development Setup

### IDE Setup

**IntelliJ IDEA** (Recommended)

```
1. Install IntelliJ IDEA Community Edition
2. Open project: File → Open → select gradhire folder
3. Configure JDK: File → Project Structure → SDKs → Add JDK (Java 8+)
4. Add Tomcat: Run → Edit Configurations → Add → Tomcat Server → Local
5. Set deployment: Deployment tab → Add → Artifact → gradhire:war
6. Configure build path: Add lib/*.jar to Project Libraries
```

**Eclipse**

```
1. Install Eclipse IDE for Java EE Developers
2. Import project: File → Import → Existing Projects into Workspace
3. Add Tomcat Server: Window → Preferences → Server → Runtime Environments
4. Configure Build Path: Project → Properties → Java Build Path → Add JARs
5. Add project to Tomcat server
```

### Local Development Environment

```bash
# Clone repository
git clone https://github.com/yourusername/gradhire.git
cd gradhire

# Install dependencies (see SETUP_GUIDE.md)

# Configure IDE to use Tomcat

# Enable hot reload in Tomcat
# Edit $CATALINA_HOME/conf/context.xml
<Context reloadable="true">
```

### Development Database

```sql
-- Create development database
CREATE DATABASE gradhire_dev CHARACTER SET utf8mb4;
CREATE USER 'dev_user'@'localhost' IDENTIFIED BY 'dev_password';
GRANT ALL ON gradhire_dev.* TO 'dev_user'@'localhost';

-- Import schema
SOURCE sql/schema.sql;

-- Add test data
SOURCE sql/test_data.sql;  -- (create this)
```

---

## 📁 Code Structure

### Package Organization

```
com.gradhire/
├── dao/              # Data Access Layer
│   ├── StudentDAO.java
│   ├── AdminDAO.java
│   ├── JobDAO.java
│   ├── ApplicationDAO.java
│   └── SkillDAO.java
│
├── model/            # Domain Models
│   ├── Student.java
│   ├── Admin.java
│   ├── Job.java
│   ├── Application.java
│   └── Skill.java
│
├── servlet/          # Controllers
│   ├── LoginServlet.java
│   ├── RegisterServlet.java
│   └── ...
│
├── filter/           # Security Filters
│   ├── AuthenticationFilter.java
│   └── AuthorizationFilter.java
│
├── util/             # Utilities
│   ├── DBConnection.java
│   ├── PasswordHasher.java
│   ├── SessionManager.java
│   ├── Validator.java
│   └── RecommendationEngine.java
│
└── exception/        # Custom Exceptions
    └── DataAccessException.java
```

### Layer Responsibilities

**DAO Layer** (dao/)
- Database operations (CRUD)
- SQL query execution
- Result set mapping
- Connection management

**Model Layer** (model/)
- Data entities
- Business logic
- Getters/setters
- Validation helpers

**Controller Layer** (servlet/)
- HTTP request handling
- User input validation
- Business logic coordination
- Response generation

**Filter Layer** (filter/)
- Request/response interception
- Authentication/authorization
- Logging
- Input sanitization

**Utility Layer** (util/)
- Helper functions
- Common operations
- Third-party integrations
- Algorithms

---

## 📝 Coding Standards

### Java Conventions

```java
// Class names: PascalCase
public class StudentDAO { }

// Method names: camelCase
public Student findById(Integer id) { }

// Constants: UPPER_SNAKE_CASE
private static final int MAX_RETRIES = 3;

// Variables: camelCase
private String userName;

// Package names: lowercase
package com.gradhire.dao;
```

### Documentation

**JavaDoc for public methods:**

```java
/**
 * Finds a student by their unique ID.
 * 
 * @param studentId The unique identifier of the student
 * @return Student object if found, null otherwise
 * @throws IllegalArgumentException if studentId is null
 */
public Student findById(Integer studentId) {
    // implementation
}
```

### Error Handling

```java
// Use try-with-resources for auto-closing
try (Connection conn = DBConnection.getConnection();
     PreparedStatement pstmt = conn.prepareStatement(sql)) {
    // execute query
} catch (SQLException e) {
    logger.log(Level.SEVERE, "Database error", e);
    throw new DataAccessException("Failed to fetch student", e);
}

// Validate inputs
if (studentId == null) {
    throw new IllegalArgumentException("Student ID cannot be null");
}
```

### SQL Best Practices

```java
// Use PreparedStatements (prevents SQL injection)
String sql = "SELECT * FROM students WHERE student_id = ?";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setInt(1, studentId);

// Close resources in finally block or use try-with-resources
try (ResultSet rs = pstmt.executeQuery()) {
    // process results
}

// Use connection pooling
Connection conn = DBConnection.getConnection();
```

---

## ➕ Adding New Features

### Adding a New Servlet

**1. Create Servlet Class**

```java
package com.gradhire.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/new-feature")
public class NewFeatureServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Handle GET request
        request.getRequestDispatcher("/WEB-INF/jsp/new-feature.jsp")
               .forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Handle POST request
        // Process form data
        // Redirect or forward
    }
}
```

**2. Add URL Mapping in web.xml**

```xml
<servlet>
    <servlet-name>NewFeatureServlet</servlet-name>
    <servlet-class>com.gradhire.servlet.NewFeatureServlet</servlet-class>
</servlet>

<servlet-mapping>
    <servlet-name>NewFeatureServlet</servlet-name>
    <url-pattern>/new-feature</url-pattern>
</servlet-mapping>
```

**3. Create JSP View**

```jsp
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>New Feature</title>
</head>
<body>
    <h1>New Feature</h1>
    <!-- Content -->
</body>
</html>
```

### Adding a New DAO Method

```java
/**
 * Find students by graduation year.
 * 
 * @param year Graduation year
 * @return List of students graduating in the specified year
 */
public List<Student> findByGraduationYear(Integer year) {
    String sql = "SELECT * FROM students WHERE graduation_year = ? " +
                 "ORDER BY full_name";
    List<Student> students = new ArrayList<>();
    
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        pstmt.setInt(1, year);
        
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                students.add(extractStudentFromResultSet(rs));
            }
        }
    } catch (SQLException e) {
        logger.log(Level.SEVERE, "Error finding students by year", e);
        throw new DataAccessException("Failed to find students", e);
    }
    
    return students;
}
```

### Adding a New Database Table

**1. Create Migration SQL**

```sql
-- sql/migration_add_notifications.sql
CREATE TABLE notifications (
    notification_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES students(student_id) ON DELETE CASCADE
);

CREATE INDEX idx_notifications_user ON notifications(user_id, is_read);
```

**2. Create Model Class**

```java
package com.gradhire.model;

import java.sql.Timestamp;

public class Notification {
    private Integer notificationId;
    private Integer userId;
    private String message;
    private Boolean isRead;
    private Timestamp createdAt;
    
    // Constructors, getters, setters
}
```

**3. Create DAO Class**

```java
package com.gradhire.dao;

public class NotificationDAO {
    // CRUD operations
}
```

---

## 🧪 Testing

### Unit Testing

```java
// Test class example
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RecommendationEngineTest {
    
    @Test
    public void testJaccardSimilarity() {
        Set<String> set1 = Set.of("Java", "Python", "SQL");
        Set<String> set2 = Set.of("Java", "Python", "SQL");
        
        double similarity = RecommendationEngine.calculateJaccardSimilarity(set1, set2);
        
        assertEquals(1.0, similarity, 0.001);
    }
    
    @Test
    public void testNullInputHandling() {
        assertThrows(IllegalArgumentException.class, () -> {
            studentDAO.findById(null);
        });
    }
}
```

### Integration Testing

```java
// Test database operations
@Test
public void testCreateAndRetrieveStudent() {
    // Create
    Student student = new Student();
    student.setEmail("test@example.com");
    student.setFullName("Test Student");
    
    Integer id = studentDAO.createStudent(student);
    assertNotNull(id);
    
    // Retrieve
    Student retrieved = studentDAO.findById(id);
    assertEquals("test@example.com", retrieved.getEmail());
    
    // Cleanup
    studentDAO.deleteStudent(id);
}
```

### Manual Testing

```bash
# Start application
ant deploy
$CATALINA_HOME/bin/startup.sh

# Test endpoints
curl http://localhost:8080/gradhire/
curl -X POST http://localhost:8080/gradhire/login \
     -d "email=test@example.com&password=password123"

# Check database
mysql -u gradhire_user -p gradhire_db -e "SELECT * FROM students;"
```

---

## 🐛 Debugging

### Enable Debug Logging

```java
// In your DAO or Servlet
import java.util.logging.Logger;
import java.util.logging.Level;

private static final Logger logger = Logger.getLogger(YourClass.class.getName());

logger.log(Level.INFO, "Processing request for user: " + userId);
logger.log(Level.WARNING, "Invalid input detected");
logger.log(Level.SEVERE, "Database error", exception);
```

### Tomcat Debug Mode

```bash
# Start Tomcat in debug mode
export JPDA_ADDRESS=8000
export JPDA_TRANSPORT=dt_socket
$CATALINA_HOME/bin/catalina.sh jpda start

# Connect debugger from IDE to localhost:8000
```

### Common Debugging Techniques

**1. Check Logs**
```bash
tail -f $CATALINA_HOME/logs/catalina.out
tail -f $CATALINA_HOME/logs/localhost.$(date +%Y-%m-%d).log
```

**2. Add Breakpoints in IDE**
- Set breakpoints in servlet doGet/doPost methods
- Debug DAO methods
- Step through recommendation algorithm

**3. Print Stack Traces**
```java
try {
    // code
} catch (Exception e) {
    e.printStackTrace();  // Development only
    logger.log(Level.SEVERE, "Error", e);  // Production
}
```

**4. SQL Query Debugging**
```java
// Log SQL queries
logger.info("Executing SQL: " + sql);
logger.info("With parameters: " + params);
```

---

## 🤝 Contributing

### Git Workflow

```bash
# 1. Fork the repository on GitHub

# 2. Clone your fork
git clone https://github.com/yourusername/gradhire.git
cd gradhire

# 3. Add upstream remote
git remote add upstream https://github.com/original/gradhire.git

# 4. Create feature branch
git checkout -b feature/your-feature-name

# 5. Make changes and commit
git add .
git commit -m "Add feature: your feature description"

# 6. Push to your fork
git push origin feature/your-feature-name

# 7. Create Pull Request on GitHub
```

### Commit Messages

```bash
# Format: <type>: <subject>

# Types: feat, fix, docs, style, refactor, test, chore

# Good examples:
git commit -m "feat: add email notification system"
git commit -m "fix: resolve null pointer in StudentDAO"
git commit -m "docs: update API documentation"
git commit -m "refactor: simplify recommendation algorithm"
git commit -m "test: add unit tests for JobDAO"
```

### Pull Request Process

1. **Update documentation** if needed
2. **Add tests** for new features
3. **Follow coding standards**
4. **Ensure build passes**: `ant clean war`
5. **Write descriptive PR description**
6. **Request review** from maintainers

---

## 📚 API Reference

### Authentication

**Login**
```
POST /login
Content-Type: application/x-www-form-urlencoded

email=user@example.com&password=password123

Response: Redirect to dashboard or error
```

**Register**
```
POST /register
Content-Type: application/x-www-form-urlencoded

email=user@example.com&password=password123&fullName=John Doe&userType=student

Response: Redirect to login or error
```

### Jobs

**List Jobs**
```
GET /jobs?keyword=java&location=remote&page=1

Response: JSP with job listings
```

**Job Details**
```
GET /job/123

Response: JSP with job details and application form
```

### Applications

**Submit Application**
```
POST /apply
Content-Type: multipart/form-data

jobId=123&coverLetter=...&resume=file

Response: Redirect to dashboard with success message
```

### DAOs

**StudentDAO**
```java
// Create
Integer createStudent(Student student)

// Read
Student findById(Integer id)
Student findByEmail(String email)
List<Student> findAll()

// Update
boolean updateStudent(Student student)

// Delete
boolean deleteStudent(Integer id)
```

**JobDAO**
```java
// Create
Integer createJob(Job job)

// Read
Job findById(Integer id)
List<Job> findActiveJobs(int limit, int offset)
List<Job> searchJobs(String keyword)
List<JobRecommendation> getRecommendedJobs(Student student, int limit)

// Update
boolean updateJob(Job job)

// Delete
boolean deleteJob(Integer id)
```

---

## 🎨 Frontend Development

### Adding New JSP Page

```jsp
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Page Title - GradHire</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <%@ include file="/WEB-INF/jsp/common/navbar.jsp" %>
    
    <div class="container mt-4">
        <h1>Page Content</h1>
        
        <!-- Your content here -->
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
```

### CSS Customization

```css
/* web/css/style.css */

/* Add custom styles */
.custom-card {
    border-radius: 15px;
    box-shadow: 0 4px 6px rgba(0,0,0,0.1);
}

/* Override Bootstrap */
.btn-primary {
    background-color: #007bff;
    border-color: #007bff;
}
```

---

## 📖 Resources

- **Java Servlets**: https://docs.oracle.com/javaee/7/tutorial/servlets.htm
- **JSP**: https://docs.oracle.com/javaee/7/tutorial/jsps.htm
- **JDBC**: https://docs.oracle.com/javase/tutorial/jdbc/
- **Bootstrap 5**: https://getbootstrap.com/docs/5.3/
- **MySQL**: https://dev.mysql.com/doc/

---

**Happy Coding!** 🚀

*Last Updated: October 24, 2025*



