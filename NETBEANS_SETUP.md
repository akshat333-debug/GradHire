# 🚀 NetBeans Setup Guide for GradHire

**Quick Start Guide for NetBeans + Tomcat**

---

## ✅ Prerequisites Check

You have:
- ✅ NetBeans IDE
- ✅ Tomcat Server
- ✅ GradHire project files

You need:
- ⬜ MySQL Server (if not installed)
- ⬜ 6 JAR files (external libraries)
- ⬜ Database setup

**Estimated Time**: 20-30 minutes

---

## 📋 Step-by-Step Instructions

### **STEP 1: Install MySQL (if not already installed)** ⏱️ 5 minutes

#### Option A: macOS (Using Homebrew)
```bash
# Install MySQL
brew install mysql

# Start MySQL service
brew services start mysql

# Secure installation (set root password)
mysql_secure_installation
```

#### Option B: Download MySQL Installer
1. Go to: https://dev.mysql.com/downloads/mysql/
2. Download MySQL Community Server for macOS
3. Install and set root password (remember this!)
4. Start MySQL from System Preferences

#### Verify MySQL is Running
```bash
mysql --version
# Should show: mysql  Ver 8.0.xx
```

---

### **STEP 2: Download Required JAR Files** ⏱️ 5 minutes

You need to download **6 JAR files** and place them in the `lib/` directory.

#### Create lib directory if it doesn't exist:
```bash
cd /Users/agraw/Desktop/projects/gradHire
mkdir -p lib
```

#### Download these JARs:

**1. MySQL Connector (JDBC Driver)**
```
URL: https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar
File: mysql-connector-j-8.0.33.jar
Size: ~2.4 MB
```

**2. Apache Commons DBCP2 (Connection Pooling)**
```
URL: https://repo1.maven.org/maven2/org/apache/commons/commons-dbcp2/2.9.0/commons-dbcp2-2.9.0.jar
File: commons-dbcp2-2.9.0.jar
Size: ~220 KB
```

**3. Apache Commons Pool2 (Object Pooling)**
```
URL: https://repo1.maven.org/maven2/org/apache/commons/commons-pool2/2.11.1/commons-pool2-2.11.1.jar
File: commons-pool2-2.11.1.jar
Size: ~150 KB
```

**4. Apache Commons Logging**
```
URL: https://repo1.maven.org/maven2/commons-logging/commons-logging/1.2/commons-logging-1.2.jar
File: commons-logging-1.2.jar
Size: ~61 KB
```

**5. jBCrypt (Password Hashing)**
```
URL: https://repo1.maven.org/maven2/org/mindrot/jbcrypt/0.4/jbcrypt-0.4.jar
File: jbcrypt-0.4.jar
Size: ~8 KB
```

**6. JSTL (JSP Standard Tag Library)**
```
URL: https://repo1.maven.org/maven2/javax/servlet/jstl/1.2/jstl-1.2.jar
File: jstl-1.2.jar
Size: ~415 KB
```

#### Quick Download Script:
```bash
cd /Users/agraw/Desktop/projects/gradHire/lib

# Download all JARs
curl -O https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar
curl -O https://repo1.maven.org/maven2/org/apache/commons/commons-dbcp2/2.9.0/commons-dbcp2-2.9.0.jar
curl -O https://repo1.maven.org/maven2/org/apache/commons/commons-pool2/2.11.1/commons-pool2-2.11.1.jar
curl -O https://repo1.maven.org/maven2/commons-logging/commons-logging/1.2/commons-logging-1.2.jar
curl -O https://repo1.maven.org/maven2/org/mindrot/jbcrypt/0.4/jbcrypt-0.4.jar
curl -O https://repo1.maven.org/maven2/javax/servlet/jstl/1.2/jstl-1.2.jar

# Verify downloads
ls -lh *.jar
```

**Expected Output:**
```
mysql-connector-j-8.0.33.jar  (~2.4 MB)
commons-dbcp2-2.9.0.jar       (~220 KB)
commons-pool2-2.11.1.jar      (~150 KB)
commons-logging-1.2.jar       (~61 KB)
jbcrypt-0.4.jar               (~8 KB)
jstl-1.2.jar                  (~415 KB)
```

---

### **STEP 3: Setup MySQL Database** ⏱️ 3 minutes

```bash
# Login to MySQL (enter your root password)
mysql -u root -p

# Inside MySQL prompt, run:
```

```sql
-- Create database
CREATE DATABASE gradhire_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Create user
CREATE USER 'gradhire_user'@'localhost' IDENTIFIED BY 'secure_password123';

-- Grant permissions
GRANT ALL PRIVILEGES ON gradhire_db.* TO 'gradhire_user'@'localhost';
FLUSH PRIVILEGES;

-- Switch to database
USE gradhire_db;

-- Import schema (copy-paste from sql/schema.sql or use source command)
SOURCE /Users/agraw/Desktop/projects/gradHire/sql/schema.sql;

-- Verify tables
SHOW TABLES;

-- Should show 9 tables:
-- activity_logs, admins, applications, job_skills, jobs, 
-- saved_jobs, skills, student_skills, students

-- Exit MySQL
EXIT;
```

#### Alternative: Import via command line
```bash
mysql -u root -p gradhire_db < /Users/agraw/Desktop/projects/gradHire/sql/schema.sql
```

---

### **STEP 4: Configure Database Connection** ⏱️ 2 minutes

Create `database.properties` file:

```bash
cd /Users/agraw/Desktop/projects/gradHire
cp database.properties.template database.properties
```

Edit `database.properties` with your MySQL credentials:
```properties
# Database Connection Configuration
db.url=jdbc:mysql://localhost:3306/gradhire_db?useSSL=false&serverTimezone=UTC
db.username=gradhire_user
db.password=secure_password123
db.driver=com.mysql.cj.jdbc.Driver

# Connection Pool Settings
db.pool.initialSize=5
db.pool.maxTotal=20
db.pool.maxIdle=10
db.pool.minIdle=5
```

**Important**: Replace `secure_password123` with the actual password you set!

---

### **STEP 5: Setup Project in NetBeans** ⏱️ 5 minutes

#### 5.1 Open Project in NetBeans

1. **Open NetBeans IDE**
2. **File → Open Project**
3. Navigate to `/Users/agraw/Desktop/projects/gradHire`
4. Select the project and click **Open Project**

#### 5.2 Configure as Web Application

If NetBeans doesn't recognize it as a web project:

1. **Right-click on project → Properties**
2. **Categories → Sources**
   - Source Package Folders: `src`
   - Web Pages: `web`
3. **Categories → Libraries**
   - Click **Add JAR/Folder**
   - Navigate to `lib/` directory
   - **Select ALL 6 JAR files** and add them
4. **Categories → Run**
   - Server: Select your Tomcat server
   - Context Path: `/gradhire`
5. Click **OK**

#### 5.3 Add Servlet API (if needed)

If you see compilation errors about `javax.servlet`:

1. **Right-click project → Properties → Libraries**
2. **Add Library → Java EE 7 API Library** (or Java EE 8)
3. Or add Tomcat libraries: **Add Library → Tomcat**

---

### **STEP 6: Copy JAR Files to WEB-INF/lib** ⏱️ 1 minute

NetBeans might not automatically copy JARs to the WAR. Do this manually:

```bash
cd /Users/agraw/Desktop/projects/gradHire
mkdir -p web/WEB-INF/lib
cp lib/*.jar web/WEB-INF/lib/
```

Verify:
```bash
ls -lh web/WEB-INF/lib/
# Should show all 6 JAR files
```

---

### **STEP 7: Build the Project** ⏱️ 2 minutes

#### Option A: Using NetBeans
1. **Right-click on project → Clean and Build**
2. Check **Output** window for any errors
3. Should see: **BUILD SUCCESSFUL**

#### Option B: Using Terminal (Ant)
```bash
cd /Users/agraw/Desktop/projects/gradHire
ant clean war

# Output location:
# dist/gradhire.war
```

**Expected Output:**
```
BUILD SUCCESSFUL
Total time: XX seconds
WAR file created at: dist/gradhire.war
```

---

### **STEP 8: Deploy to Tomcat** ⏱️ 2 minutes

#### Option A: Deploy from NetBeans (Easiest!)

1. **Right-click on project → Run**
2. NetBeans will automatically:
   - Build the project
   - Deploy to Tomcat
   - Start Tomcat (if not running)
   - Open browser

#### Option B: Manual Deployment

1. **Copy WAR to Tomcat:**
   ```bash
   cp dist/gradhire.war $CATALINA_HOME/webapps/
   ```

2. **Start Tomcat:**
   ```bash
   $CATALINA_HOME/bin/startup.sh
   # Or on Windows: startup.bat
   ```

3. **Verify deployment:**
   ```bash
   ls $CATALINA_HOME/webapps/
   # Should see: gradhire/ and gradhire.war
   ```

---

### **STEP 9: Access the Application** ⏱️ 1 minute

Open your browser and go to:

```
http://localhost:8080/gradhire
```

You should see the **GradHire landing page**! 🎉

---

### **STEP 10: Test the Application** ⏱️ 5 minutes

#### 10.1 Register as Student
1. Click **Register**
2. Choose **Student**
3. Fill in details:
   - Email: `john.doe@college.edu`
   - Password: `Password123!`
   - Name: `John Doe`
4. Submit

#### 10.2 Login as Student
1. Click **Login**
2. Use credentials from registration
3. You should see **Student Dashboard**

#### 10.3 Register as Admin/Recruiter
1. Logout
2. Register again, choose **Recruiter**
3. Fill in company details

#### 10.4 Post a Job (Admin)
1. Login as admin
2. Go to **Post Job**
3. Fill job details
4. Add required skills

#### 10.5 Browse & Apply (Student)
1. Login as student
2. Go to **Browse Jobs**
3. Click on a job
4. Click **Apply**
5. Upload resume and submit

---

## 🔧 Troubleshooting

### Problem: "ClassNotFoundException: com.mysql.cj.jdbc.Driver"
**Solution**: 
- JARs not in `web/WEB-INF/lib/`
- Copy: `cp lib/*.jar web/WEB-INF/lib/`
- Rebuild project

### Problem: "Cannot connect to database"
**Solutions**:
1. Check MySQL is running: `brew services list` or `mysql -u root -p`
2. Verify credentials in `database.properties`
3. Test connection:
   ```bash
   mysql -u gradhire_user -p gradhire_db
   ```

### Problem: Port 8080 already in use
**Solutions**:
1. Stop other Tomcat instances
2. Change Tomcat port in NetBeans: **Tools → Servers → Tomcat → Connection → HTTP Port**

### Problem: "404 - Not Found"
**Solutions**:
1. Check Tomcat logs: `$CATALINA_HOME/logs/catalina.out`
2. Verify deployment: `ls $CATALINA_HOME/webapps/gradhire/`
3. Check context path: should be `/gradhire`

### Problem: JSP compilation errors
**Solution**:
- JSTL not found: Verify `jstl-1.2.jar` is in `web/WEB-INF/lib/`
- Rebuild: **Clean and Build**

### Problem: NetBeans can't find Tomcat
**Solution**:
1. **Tools → Servers → Add Server**
2. Choose **Apache Tomcat**
3. Browse to Tomcat installation directory
4. Enter username/password (from `tomcat-users.xml`)

---

## 📊 Verification Checklist

Before running, verify:

- [ ] MySQL is running
- [ ] Database `gradhire_db` exists
- [ ] All 9 tables are created
- [ ] All 6 JAR files in `lib/`
- [ ] All 6 JAR files copied to `web/WEB-INF/lib/`
- [ ] `database.properties` configured correctly
- [ ] Project builds without errors
- [ ] Tomcat is configured in NetBeans
- [ ] Context path is `/gradhire`

---

## 🎯 Quick Reference

### Start Application
```bash
# From NetBeans: Right-click project → Run
# Or manually:
$CATALINA_HOME/bin/startup.sh
```

### Stop Application
```bash
# From NetBeans: Stop button or Right-click project → Undeploy
# Or manually:
$CATALINA_HOME/bin/shutdown.sh
```

### View Logs
```bash
# Tomcat logs
tail -f $CATALINA_HOME/logs/catalina.out

# Application logs
tail -f $CATALINA_HOME/logs/localhost.*.log
```

### Rebuild
```bash
# From NetBeans: Right-click project → Clean and Build
# Or manually:
ant clean war
```

---

## 🌐 Default URLs

- **Landing Page**: http://localhost:8080/gradhire
- **Login**: http://localhost:8080/gradhire/login
- **Register**: http://localhost:8080/gradhire/register
- **Student Dashboard**: http://localhost:8080/gradhire/student/dashboard
- **Admin Dashboard**: http://localhost:8080/gradhire/admin/dashboard
- **Job Listings**: http://localhost:8080/gradhire/jobs

---

## 📞 Need Help?

1. **Check Logs**: `$CATALINA_HOME/logs/catalina.out`
2. **Review**: [DEPLOYMENT_GUIDE.md](DEPLOYMENT_GUIDE.md)
3. **See**: [FINAL_TEST_REPORT.md](FINAL_TEST_REPORT.md)
4. **Troubleshooting**: [SETUP_GUIDE.md](SETUP_GUIDE.md)

---

## 🎉 Success!

Once you see the GradHire landing page at `http://localhost:8080/gradhire`, you're all set!

**Next steps**:
1. Register test accounts (student + admin)
2. Post sample jobs
3. Test application workflow
4. Explore recommendations feature

---

**Good luck! 🚀**



