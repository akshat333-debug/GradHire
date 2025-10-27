# 🚀 GradHire Deployment Guide

Complete guide for deploying GradHire application to Apache Tomcat

---

## 📋 Table of Contents

1. [Prerequisites](#prerequisites)
2. [Environment Setup](#environment-setup)
3. [Database Setup](#database-setup)
4. [Building the Application](#building-the-application)
5. [Deploying to Tomcat](#deploying-to-tomcat)
6. [Verification](#verification)
7. [Troubleshooting](#troubleshooting)
8. [Production Deployment](#production-deployment)

---

## 📦 Prerequisites

### Required Software

```
✅ Java Development Kit (JDK) 8 or higher
✅ Apache Tomcat 9.0 or higher
✅ MySQL 8.0 or higher
✅ Apache Ant 1.10 or higher (for building)
✅ Git (optional, for version control)
```

### Verify Installation

```bash
# Check Java version
java -version
javac -version

# Check Tomcat (after installation)
echo $CATALINA_HOME

# Check MySQL
mysql --version

# Check Ant
ant -version
```

---

## 🔧 Environment Setup

### 1. Install Apache Tomcat

#### macOS/Linux:

```bash
# Download Tomcat
cd /usr/local
sudo wget https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.80/bin/apache-tomcat-9.0.80.tar.gz

# Extract
sudo tar xzf apache-tomcat-9.0.80.tar.gz
sudo mv apache-tomcat-9.0.80 tomcat

# Set permissions
sudo chmod +x /usr/local/tomcat/bin/*.sh

# Set environment variable
echo 'export CATALINA_HOME=/usr/local/tomcat' >> ~/.bashrc
echo 'export PATH=$PATH:$CATALINA_HOME/bin' >> ~/.bashrc
source ~/.bashrc
```

#### Windows:

1. Download Tomcat from https://tomcat.apache.org/download-90.cgi
2. Extract to `C:\Program Files\Apache Tomcat 9.0`
3. Set environment variable:
   ```cmd
   setx CATALINA_HOME "C:\Program Files\Apache Tomcat 9.0"
   ```

### 2. Install Required Libraries

Download and place these JAR files in the `lib/` directory:

```
lib/
├── mysql-connector-java-8.0.33.jar    (MySQL JDBC Driver)
├── commons-dbcp2-2.9.0.jar            (Connection Pooling)
├── commons-pool2-2.11.1.jar           (Commons Pool)
├── commons-logging-1.2.jar            (Commons Logging)
├── jbcrypt-0.4.jar                    (Password Hashing)
└── jstl-1.2.jar                       (JSP Standard Tag Library)
```

**Download Links:**
- MySQL Connector: https://dev.mysql.com/downloads/connector/j/
- Apache Commons: https://commons.apache.org/
- jBCrypt: https://www.mindrot.org/projects/jBCrypt/
- JSTL: https://jakarta.ee/specifications/tags/

### 3. Configure Build Properties

Edit `build.properties`:

```properties
# Set your Tomcat installation path
tomcat.home=/usr/local/tomcat

# Database connection
db.url=jdbc:mysql://localhost:3306/gradhire_db
db.username=gradhire_user
db.password=your_secure_password
```

---

## 🗄️ Database Setup

### 1. Create Database and User

```sql
-- Login to MySQL as root
mysql -u root -p

-- Create database
CREATE DATABASE gradhire_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Create user
CREATE USER 'gradhire_user'@'localhost' IDENTIFIED BY 'secure_password';

-- Grant privileges
GRANT ALL PRIVILEGES ON gradhire_db.* TO 'gradhire_user'@'localhost';
FLUSH PRIVILEGES;

-- Verify
SHOW DATABASES;
SELECT User, Host FROM mysql.user WHERE User = 'gradhire_user';
```

### 2. Run Database Schema

```bash
# Navigate to project directory
cd /path/to/gradHire

# Execute schema
mysql -u gradhire_user -p gradhire_db < sql/schema.sql
```

### 3. Verify Tables

```sql
-- Login as application user
mysql -u gradhire_user -p gradhire_db

-- List tables
SHOW TABLES;

-- Check table structure
DESCRIBE students;
DESCRIBE jobs;
DESCRIBE applications;

-- Expected output: 10+ tables
```

### 4. Configure Database Connection

Update `database.properties.template` and rename to `database.properties`:

```properties
# JDBC Configuration
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/gradhire_db?useSSL=false&serverTimezone=UTC
db.username=gradhire_user
db.password=secure_password

# Connection Pool Settings
db.initialSize=5
db.maxTotal=20
db.maxIdle=10
db.minIdle=5
db.maxWaitMillis=10000
```

---

## 🏗️ Building the Application

### Using Apache Ant (Recommended)

#### 1. Build WAR File

```bash
# Navigate to project root
cd /path/to/gradHire

# Clean previous builds
ant clean

# Build WAR file
ant war

# Expected output:
# BUILD SUCCESSFUL
# WAR file created: dist/gradhire.war
```

#### 2. Verify Build

```bash
# Check WAR file
ls -lh dist/gradhire.war

# View contents
jar -tf dist/gradhire.war | head -20
```

### Using Manual Compilation (Alternative)

```bash
# Create directories
mkdir -p build/WEB-INF/classes

# Compile Java sources
javac -d build/WEB-INF/classes \
      -cp "$CATALINA_HOME/lib/servlet-api.jar:lib/*" \
      src/com/gradhire/**/*.java

# Copy web resources
cp -r web/* build/

# Copy libraries
cp lib/*.jar build/WEB-INF/lib/

# Create WAR
cd build
jar -cvf ../dist/gradhire.war *
```

### Available Ant Targets

```bash
ant help          # Display all available targets

ant clean         # Remove build and dist directories
ant compile       # Compile Java sources only
ant war           # Build WAR file (default)
ant deploy        # Build and deploy to local Tomcat
ant undeploy      # Remove from Tomcat
ant all           # Clean build from scratch
```

---

## 🚀 Deploying to Tomcat

### Method 1: Automatic Deployment (Ant)

```bash
# Build and deploy in one command
ant deploy

# Expected output:
# Deployed to: /usr/local/tomcat/webapps/gradhire.war
```

### Method 2: Manual Deployment

```bash
# Copy WAR to Tomcat webapps
cp dist/gradhire.war $CATALINA_HOME/webapps/

# Or on Windows:
copy dist\gradhire.war "%CATALINA_HOME%\webapps\"
```

### Method 3: Tomcat Manager (GUI)

1. Access Tomcat Manager: http://localhost:8080/manager/html
2. Scroll to "WAR file to deploy"
3. Click "Choose File" and select `dist/gradhire.war`
4. Click "Deploy"

### Starting Tomcat

#### macOS/Linux:

```bash
# Start Tomcat
$CATALINA_HOME/bin/startup.sh

# Or using catalina
catalina.sh start

# Check logs
tail -f $CATALINA_HOME/logs/catalina.out
```

#### Windows:

```cmd
# Start Tomcat
"%CATALINA_HOME%\bin\startup.bat"

# Or install as service
```

### Stopping Tomcat

```bash
# macOS/Linux
$CATALINA_HOME/bin/shutdown.sh

# Windows
"%CATALINA_HOME%\bin\shutdown.bat"
```

---

## ✅ Verification

### 1. Check Deployment Status

```bash
# List deployed applications
ls -l $CATALINA_HOME/webapps/

# Expected: gradhire.war and gradhire/ directory
```

### 2. Access Application

Open web browser and navigate to:

```
http://localhost:8080/gradhire
```

**Expected Result:**
- GradHire landing page loads
- No error messages
- Login/Register links work

### 3. Test Key Features

```
✅ Landing Page:       http://localhost:8080/gradhire/
✅ Login Page:         http://localhost:8080/gradhire/login
✅ Register Page:      http://localhost:8080/gradhire/register
✅ Job Listings:       http://localhost:8080/gradhire/jobs
```

### 4. Check Logs

```bash
# Tomcat catalina log
tail -f $CATALINA_HOME/logs/catalina.out

# Tomcat localhost log
tail -f $CATALINA_HOME/logs/localhost.$(date +%Y-%m-%d).log

# Look for:
# ✅ "Deploying web application directory..."
# ✅ "Deployment of web application directory... has finished in..."
# ❌ Check for errors or exceptions
```

### 5. Database Connection Test

```bash
# Login to application
# Go to: http://localhost:8080/gradhire/register

# Try registering a test user
# If successful, check database:
mysql -u gradhire_user -p gradhire_db
SELECT * FROM students;
```

---

## 🔧 Troubleshooting

### Issue: Port 8080 Already in Use

**Solution:**

```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>

# Or change Tomcat port in $CATALINA_HOME/conf/server.xml
```

### Issue: ClassNotFoundException

**Solution:**

```bash
# Ensure all JAR files are in lib/ directory
ls -l lib/

# Rebuild
ant clean all deploy
```

### Issue: Database Connection Failed

**Checklist:**

```sql
-- Verify MySQL is running
sudo service mysql status

-- Check database exists
SHOW DATABASES LIKE 'gradhire_db';

-- Check user privileges
SHOW GRANTS FOR 'gradhire_user'@'localhost';

-- Test connection
mysql -u gradhire_user -p gradhire_db
```

**Fix database.properties:**

```properties
# Add timezone parameter
db.url=jdbc:mysql://localhost:3306/gradhire_db?useSSL=false&serverTimezone=UTC
```

### Issue: 404 Not Found

**Checklist:**

```bash
# 1. Check WAR is deployed
ls $CATALINA_HOME/webapps/gradhire.war

# 2. Check directory exists
ls $CATALINA_HOME/webapps/gradhire/

# 3. Check web.xml
cat $CATALINA_HOME/webapps/gradhire/WEB-INF/web.xml

# 4. Restart Tomcat
$CATALINA_HOME/bin/shutdown.sh && $CATALINA_HOME/bin/startup.sh
```

### Issue: JSP Compilation Error

**Solution:**

```bash
# Clear Tomcat work directory
rm -rf $CATALINA_HOME/work/Catalina/localhost/gradhire

# Redeploy
ant undeploy deploy
```

### Issue: File Upload Not Working

**Checklist:**

```bash
# 1. Create upload directories
mkdir -p $CATALINA_HOME/webapps/gradhire/uploads/resumes
mkdir -p $CATALINA_HOME/webapps/gradhire/uploads/profiles

# 2. Set permissions
chmod 755 $CATALINA_HOME/webapps/gradhire/uploads

# 3. Check multipart-config in web.xml
```

---

## 🌐 Production Deployment

### Pre-Deployment Checklist

```
✅ Database backed up
✅ Environment variables configured
✅ SSL certificate installed (for HTTPS)
✅ Firewall rules configured
✅ Security settings reviewed
✅ Performance tuning completed
✅ Monitoring setup
✅ Backup strategy in place
```

### Production Configuration

#### 1. Enable HTTPS (web.xml)

```xml
<!-- Uncomment in web.xml -->
<security-constraint>
    <web-resource-collection>
        <web-resource-name>Entire Application</web-resource-name>
        <url-pattern>/*</url-pattern>
    </web-resource-collection>
    <user-data-constraint>
        <transport-guarantee>CONFIDENTIAL</transport-guarantee>
    </user-data-constraint>
</security-constraint>
```

#### 2. Configure Tomcat for Production

**server.xml:**

```xml
<!-- Enable GZIP compression -->
<Connector port="8080" protocol="HTTP/1.1"
           compression="on"
           compressionMinSize="2048"
           compressibleMimeType="text/html,text/xml,text/plain,text/css,text/javascript,application/javascript,application/json"/>

<!-- Connection pool -->
<Resource name="jdbc/GradHireDB"
          auth="Container"
          type="javax.sql.DataSource"
          maxTotal="100"
          maxIdle="30"
          maxWaitMillis="10000"
          username="gradhire_user"
          password="secure_password"
          driverClassName="com.mysql.cj.jdbc.Driver"
          url="jdbc:mysql://localhost:3306/gradhire_db"/>
```

#### 3. Performance Tuning

**catalina.sh (add to top):**

```bash
# JVM Memory Settings
JAVA_OPTS="$JAVA_OPTS -Xms512m -Xmx2048m"
JAVA_OPTS="$JAVA_OPTS -XX:MaxPermSize=256m"
JAVA_OPTS="$JAVA_OPTS -XX:+UseG1GC"

# Timezone
JAVA_OPTS="$JAVA_OPTS -Duser.timezone=UTC"
```

#### 4. Security Hardening

```bash
# Remove default applications
rm -rf $CATALINA_HOME/webapps/ROOT
rm -rf $CATALINA_HOME/webapps/docs
rm -rf $CATALINA_HOME/webapps/examples

# Restrict file permissions
chmod 600 $CATALINA_HOME/conf/tomcat-users.xml
chmod 600 database.properties

# Disable directory listing (web.xml)
<init-param>
    <param-name>listings</param-name>
    <param-value>false</param-value>
</init-param>
```

### Monitoring

```bash
# Check Tomcat status
curl http://localhost:8080/gradhire/

# Monitor logs
tail -f $CATALINA_HOME/logs/catalina.out

# Check resource usage
top
htop
```

---

## 📊 Quick Reference

### Essential Commands

```bash
# Build
ant clean war              # Build WAR from scratch
ant deploy                 # Build and deploy

# Tomcat Control
$CATALINA_HOME/bin/startup.sh      # Start
$CATALINA_HOME/bin/shutdown.sh     # Stop
catalina.sh run                    # Run in foreground (debug)

# Database
mysql -u gradhire_user -p gradhire_db        # Connect
mysql -u gradhire_user -p gradhire_db < schema.sql   # Import

# Logs
tail -f $CATALINA_HOME/logs/catalina.out     # Tomcat log
tail -f $CATALINA_HOME/logs/localhost.*.log  # Application log
```

### Important Paths

```
Project:       /path/to/gradHire
WAR File:      dist/gradhire.war
Tomcat:        $CATALINA_HOME
Webapps:       $CATALINA_HOME/webapps
Logs:          $CATALINA_HOME/logs
Config:        database.properties
Build Script:  build.xml
```

### Default URLs

```
Application:   http://localhost:8080/gradhire
Login:         http://localhost:8080/gradhire/login
Jobs:          http://localhost:8080/gradhire/jobs
Tomcat Manager: http://localhost:8080/manager/html
```

---

## 🆘 Support

### Getting Help

1. **Check Logs**: Always check Tomcat logs first
2. **Review Documentation**: README.md, QUICKSTART.md
3. **Database Status**: Verify MySQL connection
4. **Rebuild**: Try `ant clean all`

### Common Log Locations

```
Application Logs:    $CATALINA_HOME/logs/
Tomcat Logs:        $CATALINA_HOME/logs/catalina.out
MySQL Logs:         /var/log/mysql/error.log
```

---

## ✅ Post-Deployment Checklist

```
✅ Application accessible at http://localhost:8080/gradhire
✅ Login page loads without errors
✅ Registration creates database entry
✅ Job listings display correctly
✅ File uploads work
✅ Session management functional
✅ Database queries execute successfully
✅ No errors in Tomcat logs
✅ All servlets respond correctly
✅ Static resources (CSS, JS) load
```

---

**Deployment Status**: Ready for Production ✅

**Last Updated**: October 24, 2025

**Contact**: GradHire Development Team



