# 🔧 GradHire Setup Guide

Complete step-by-step installation and configuration guide for GradHire.

---

## 📋 Table of Contents

1. [System Requirements](#system-requirements)
2. [Pre-Installation](#pre-installation)
3. [Installing Java](#installing-java)
4. [Installing Apache Tomcat](#installing-apache-tomcat)
5. [Installing MySQL](#installing-mysql)
6. [Project Setup](#project-setup)
7. [Database Configuration](#database-configuration)
8. [Building the Application](#building-the-application)
9. [Deployment](#deployment)
10. [Verification](#verification)
11. [Troubleshooting](#troubleshooting)

---

## 💻 System Requirements

### Minimum Requirements

- **OS**: macOS 10.14+, Ubuntu 18.04+, Windows 10+
- **RAM**: 4 GB
- **Disk Space**: 2 GB free
- **Processor**: 2 GHz dual-core

### Recommended Requirements

- **OS**: macOS 12+, Ubuntu 22.04+, Windows 11
- **RAM**: 8 GB
- **Disk Space**: 5 GB free
- **Processor**: 2.5 GHz quad-core

---

## 📦 Pre-Installation

### Required Software

| Software | Version | Download Link |
|----------|---------|---------------|
| Java JDK | 8 or 11 | https://adoptium.net/ |
| Apache Tomcat | 9.0.80+ | https://tomcat.apache.org/download-90.cgi |
| MySQL | 8.0+ | https://dev.mysql.com/downloads/mysql/ |
| Apache Ant | 1.10+ | https://ant.apache.org/bindownload.cgi |

### Optional Software

- **Git**: For version control
- **MySQL Workbench**: GUI for database management
- **IntelliJ IDEA / Eclipse**: For development

---

## ☕ Installing Java

### macOS

```bash
# Using Homebrew
brew install openjdk@11

# Add to PATH
echo 'export PATH="/usr/local/opt/openjdk@11/bin:$PATH"' >> ~/.zshrc
source ~/.zshrc

# Verify installation
java -version
javac -version
```

### Ubuntu/Debian

```bash
# Update package list
sudo apt-get update

# Install OpenJDK 11
sudo apt-get install openjdk-11-jdk

# Set JAVA_HOME
echo 'export JAVA_HOME=/usr/lib/jvm/java-11-openjdk-amd64' >> ~/.bashrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.bashrc
source ~/.bashrc

# Verify installation
java -version
javac -version
```

### Windows

1. Download JDK from https://adoptium.net/
2. Run the installer
3. Add to PATH:
   - Right-click "This PC" → Properties
   - Advanced system settings → Environment Variables
   - Add `C:\Program Files\Eclipse Adoptium\jdk-11.x.x\bin` to PATH
   - Set JAVA_HOME to `C:\Program Files\Eclipse Adoptium\jdk-11.x.x`
4. Verify in Command Prompt: `java -version`

---

## 🌐 Installing Apache Tomcat

### macOS

```bash
# Using Homebrew
brew install tomcat

# Or manual installation
cd /usr/local
sudo wget https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.80/bin/apache-tomcat-9.0.80.tar.gz
sudo tar xzf apache-tomcat-9.0.80.tar.gz
sudo mv apache-tomcat-9.0.80 tomcat

# Set permissions
sudo chmod +x /usr/local/tomcat/bin/*.sh

# Set CATALINA_HOME
echo 'export CATALINA_HOME=/usr/local/tomcat' >> ~/.zshrc
echo 'export PATH=$PATH:$CATALINA_HOME/bin' >> ~/.zshrc
source ~/.zshrc
```

### Ubuntu/Debian

```bash
# Create tomcat user
sudo useradd -r -m -U -d /opt/tomcat -s /bin/false tomcat

# Download Tomcat
cd /tmp
wget https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.80/bin/apache-tomcat-9.0.80.tar.gz

# Extract to /opt/tomcat
sudo tar xzf apache-tomcat-9.0.80.tar.gz -C /opt/tomcat --strip-components=1

# Set permissions
sudo chown -R tomcat: /opt/tomcat
sudo chmod +x /opt/tomcat/bin/*.sh

# Set environment variables
echo 'export CATALINA_HOME=/opt/tomcat' >> ~/.bashrc
source ~/.bashrc
```

### Windows

1. Download from https://tomcat.apache.org/download-90.cgi
2. Extract to `C:\Program Files\Apache Tomcat 9.0`
3. Set environment variable:
   ```cmd
   setx CATALINA_HOME "C:\Program Files\Apache Tomcat 9.0"
   ```
4. Run `bin\startup.bat` to start

---

## 🗄️ Installing MySQL

### macOS

```bash
# Using Homebrew
brew install mysql

# Start MySQL
brew services start mysql

# Secure installation
mysql_secure_installation

# Login
mysql -u root -p
```

### Ubuntu/Debian

```bash
# Install MySQL
sudo apt-get update
sudo apt-get install mysql-server

# Start service
sudo systemctl start mysql
sudo systemctl enable mysql

# Secure installation
sudo mysql_secure_installation

# Login
sudo mysql -u root -p
```

### Windows

1. Download MySQL Installer from https://dev.mysql.com/downloads/installer/
2. Run installer and select "Developer Default"
3. Follow the configuration wizard
4. Set root password
5. MySQL will start automatically as a service

---

## 📁 Project Setup

### Clone the Repository

```bash
# Using Git
git clone https://github.com/yourusername/gradhire.git
cd gradhire

# Or download ZIP and extract
```

### Download Dependencies

Create `lib/` directory and download required JARs:

```bash
mkdir -p lib
cd lib

# MySQL Connector/J
wget https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.33/mysql-connector-java-8.0.33.jar

# Apache Commons DBCP2 (Connection Pooling)
wget https://repo1.maven.org/maven2/org/apache/commons/commons-dbcp2/2.9.0/commons-dbcp2-2.9.0.jar

# Apache Commons Pool2
wget https://repo1.maven.org/maven2/org/apache/commons/commons-pool2/2.11.1/commons-pool2-2.11.1.jar

# Commons Logging
wget https://repo1.maven.org/maven2/commons-logging/commons-logging/1.2/commons-logging-1.2.jar

# jBCrypt (Password Hashing)
wget https://repo1.maven.org/maven2/org/mindrot/jbcrypt/0.4/jbcrypt-0.4.jar

# JSTL (JSP Standard Tag Library)
wget https://repo1.maven.org/maven2/javax/servlet/jstl/1.2/jstl-1.2.jar

cd ..
```

### Verify Dependencies

```bash
ls -lh lib/

# Expected output: 6 JAR files
# mysql-connector-java-8.0.33.jar (3.5M)
# commons-dbcp2-2.9.0.jar (218K)
# commons-pool2-2.11.1.jar (124K)
# commons-logging-1.2.jar (62K)
# jbcrypt-0.4.jar (35K)
# jstl-1.2.jar (415K)
```

---

## 🗄️ Database Configuration

### Step 1: Create Database and User

```bash
mysql -u root -p
```

```sql
-- Create database
CREATE DATABASE gradhire_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Create user
CREATE USER 'gradhire_user'@'localhost' IDENTIFIED BY 'GradHire2025!';

-- Grant privileges
GRANT ALL PRIVILEGES ON gradhire_db.* TO 'gradhire_user'@'localhost';
FLUSH PRIVILEGES;

-- Verify
SHOW DATABASES;
SELECT User, Host FROM mysql.user WHERE User = 'gradhire_user';

-- Exit
EXIT;
```

### Step 2: Import Database Schema

```bash
# Import schema
mysql -u gradhire_user -p gradhire_db < sql/schema.sql

# Verify tables
mysql -u gradhire_user -p gradhire_db -e "SHOW TABLES;"

# Expected: 11 tables
# admins, applications, jobs, skills, students, etc.
```

### Step 3: Configure Application Database Connection

```bash
# Copy template
cp database.properties.template database.properties

# Edit configuration
nano database.properties
```

Update with your credentials:

```properties
# JDBC Configuration
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/gradhire_db?useSSL=false&serverTimezone=UTC
db.username=gradhire_user
db.password=GradHire2025!

# Connection Pool Settings
db.initialSize=5
db.maxTotal=20
db.maxIdle=10
db.minIdle=5
db.maxWaitMillis=10000
```

### Step 4: Test Database Connection

```bash
# Login with application credentials
mysql -u gradhire_user -p gradhire_db

# Run a test query
SHOW TABLES;
DESCRIBE students;

# Exit
EXIT;
```

---

## 🏗️ Building the Application

### Using Apache Ant (Recommended)

```bash
# Install Ant
# macOS: brew install ant
# Ubuntu: sudo apt-get install ant

# Verify Ant installation
ant -version

# Clean previous builds
ant clean

# Build WAR file
ant war

# Expected output:
# BUILD SUCCESSFUL
# WAR file created: dist/gradhire.war
```

### Manual Build (Without Ant)

```bash
# Create build directories
mkdir -p build/WEB-INF/classes
mkdir -p dist

# Compile Java sources
javac -d build/WEB-INF/classes \
      -cp "$CATALINA_HOME/lib/servlet-api.jar:lib/*" \
      -sourcepath src \
      $(find src -name "*.java")

# Copy web resources
cp -r web/* build/

# Copy libraries
mkdir -p build/WEB-INF/lib
cp lib/*.jar build/WEB-INF/lib/

# Copy database properties
cp database.properties build/WEB-INF/classes/

# Create WAR file
cd build
jar -cvf ../dist/gradhire.war *
cd ..

echo "WAR file created: dist/gradhire.war"
```

### Verify Build

```bash
# Check WAR file
ls -lh dist/gradhire.war

# View contents
jar -tf dist/gradhire.war | head -20
```

---

## 🚀 Deployment

### Method 1: Ant Deployment (Fastest)

```bash
# Build and deploy in one command
ant deploy

# This will:
# 1. Build the WAR file
# 2. Copy to $CATALINA_HOME/webapps/
# 3. Show deployment confirmation
```

### Method 2: Manual Deployment

```bash
# Copy WAR to Tomcat webapps
cp dist/gradhire.war $CATALINA_HOME/webapps/

# Tomcat will auto-deploy
```

### Method 3: Tomcat Manager (GUI)

1. Start Tomcat
2. Access Manager: http://localhost:8080/manager/html
3. Login with admin credentials
4. Scroll to "WAR file to deploy"
5. Select `dist/gradhire.war`
6. Click "Deploy"

### Start Tomcat

```bash
# macOS/Linux
$CATALINA_HOME/bin/startup.sh

# Windows
%CATALINA_HOME%\bin\startup.bat

# View logs
tail -f $CATALINA_HOME/logs/catalina.out
```

---

## ✅ Verification

### 1. Check Deployment

```bash
# Verify WAR is deployed
ls -l $CATALINA_HOME/webapps/gradhire.war

# Verify auto-extraction
ls -l $CATALINA_HOME/webapps/gradhire/

# Check logs for errors
tail -50 $CATALINA_HOME/logs/catalina.out
```

Expected log output:
```
INFO: Deploying web application archive [gradhire.war]
INFO: Deployment of web application archive [gradhire.war] has finished in [1,234] ms
```

### 2. Access Application

Open browser and test these URLs:

```
✅ Landing Page:  http://localhost:8080/gradhire/
✅ Login:         http://localhost:8080/gradhire/login
✅ Register:      http://localhost:8080/gradhire/register
✅ Job Listings:  http://localhost:8080/gradhire/jobs
```

### 3. Test Registration

1. Go to http://localhost:8080/gradhire/register
2. Register as a student
3. Check database:
   ```sql
   mysql -u gradhire_user -p gradhire_db
   SELECT * FROM students ORDER BY created_at DESC LIMIT 1;
   ```

### 4. Test Login

1. Login with registered credentials
2. Verify redirect to dashboard
3. Check session in browser dev tools

---

## 🐛 Troubleshooting

### Issue: Port 8080 Already in Use

```bash
# Find process using port 8080
lsof -i :8080

# Kill the process
kill -9 <PID>

# Or change Tomcat port
# Edit $CATALINA_HOME/conf/server.xml
# Change port="8080" to port="8081"
```

### Issue: Database Connection Failed

**Symptoms:**
- Error: "Communications link failure"
- Cannot connect to database

**Solutions:**

```bash
# 1. Check MySQL is running
sudo systemctl status mysql   # Linux
brew services list | grep mysql   # macOS

# 2. Start MySQL if stopped
sudo systemctl start mysql   # Linux
brew services start mysql   # macOS

# 3. Verify credentials
mysql -u gradhire_user -p gradhire_db

# 4. Check database exists
mysql -u root -p -e "SHOW DATABASES LIKE 'gradhire_db';"

# 5. Verify user privileges
mysql -u root -p -e "SHOW GRANTS FOR 'gradhire_user'@'localhost';"

# 6. Check database.properties file
cat database.properties
```

### Issue: ClassNotFoundException

**Symptoms:**
- Error: "java.lang.ClassNotFoundException"
- Missing JAR errors

**Solutions:**

```bash
# 1. Verify all JARs are in lib/
ls -l lib/

# Expected: 6 JAR files
# If missing, re-download from maven repository

# 2. Rebuild application
ant clean all

# 3. Check WEB-INF/lib in deployed app
ls -l $CATALINA_HOME/webapps/gradhire/WEB-INF/lib/
```

### Issue: 404 Not Found

**Symptoms:**
- Browser shows "404 - Not Found"
- Application not accessible

**Solutions:**

```bash
# 1. Check WAR is deployed
ls $CATALINA_HOME/webapps/gradhire.war

# 2. Check extraction happened
ls $CATALINA_HOME/webapps/gradhire/

# 3. Check web.xml exists
cat $CATALINA_HOME/webapps/gradhire/WEB-INF/web.xml

# 4. Check Tomcat logs
tail -100 $CATALINA_HOME/logs/catalina.out

# 5. Restart Tomcat
$CATALINA_HOME/bin/shutdown.sh
$CATALINA_HOME/bin/startup.sh

# 6. Undeploy and redeploy
ant undeploy
ant deploy
```

### Issue: JSP Compilation Errors

**Symptoms:**
- JSP pages don't load
- Compilation errors in logs

**Solutions:**

```bash
# 1. Clear Tomcat work directory
rm -rf $CATALINA_HOME/work/Catalina/localhost/gradhire

# 2. Restart Tomcat
$CATALINA_HOME/bin/shutdown.sh
$CATALINA_HOME/bin/startup.sh

# 3. Check JSP syntax
# Review error in logs: $CATALINA_HOME/logs/localhost.<date>.log
```

### Issue: File Upload Not Working

**Symptoms:**
- Resume upload fails
- File upload errors

**Solutions:**

```bash
# 1. Create upload directories
mkdir -p $CATALINA_HOME/webapps/gradhire/uploads/resumes
mkdir -p $CATALINA_HOME/webapps/gradhire/uploads/profiles

# 2. Set permissions
chmod 755 $CATALINA_HOME/webapps/gradhire/uploads
chmod 755 $CATALINA_HOME/webapps/gradhire/uploads/resumes
chmod 755 $CATALINA_HOME/webapps/gradhire/uploads/profiles

# 3. Verify multipart-config in web.xml
# Should have <max-file-size> and <max-request-size>
```

### Issue: Session Not Persisting

**Symptoms:**
- User logged out immediately
- Session lost on refresh

**Solutions:**

```bash
# 1. Check browser cookies enabled

# 2. Verify session timeout in web.xml
# Should have <session-timeout>30</session-timeout>

# 3. Check SessionManager implementation

# 4. Clear browser cookies and retry
```

---

## 📞 Getting Help

If you encounter issues not covered here:

1. **Check Logs**: Always check `$CATALINA_HOME/logs/catalina.out`
2. **Review Documentation**: See DEPLOYMENT_GUIDE.md
3. **Search Issues**: Check GitHub issues
4. **Ask for Help**: Open a new issue with:
   - Error message
   - Steps to reproduce
   - Environment details (OS, Java version, etc.)

---

## ✅ Setup Checklist

Before considering setup complete, verify:

```
✅ Java 8+ installed and verified
✅ Tomcat 9+ installed and running
✅ MySQL 8+ installed and running
✅ Database 'gradhire_db' created
✅ Database schema imported successfully
✅ All 6 JAR dependencies in lib/
✅ database.properties configured
✅ Application built successfully (WAR created)
✅ WAR deployed to Tomcat
✅ Application accessible at http://localhost:8080/gradhire
✅ Registration creates database entry
✅ Login works correctly
✅ Dashboard loads for student/admin
✅ No errors in Tomcat logs
```

---

**Setup Complete!** 🎉

You're now ready to use GradHire locally. For deployment to production, see [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md).

*Last Updated: October 24, 2025*



