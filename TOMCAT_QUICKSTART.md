# 🚀 GradHire - Tomcat Quick Start Guide

**Get GradHire running on Tomcat in 10 minutes!**

---

## ⚡ Prerequisites

```bash
✅ Java 8+
✅ MySQL 8+
✅ Apache Ant
✅ Apache Tomcat 9+
```

---

## 📝 Step-by-Step Setup

### Step 1: Install Tomcat (if not already installed)

#### macOS/Linux:
```bash
# Download and extract
cd /usr/local
sudo wget https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.80/bin/apache-tomcat-9.0.80.tar.gz
sudo tar xzf apache-tomcat-9.0.80.tar.gz
sudo mv apache-tomcat-9.0.80 tomcat

# Set environment variable
export CATALINA_HOME=/usr/local/tomcat
export PATH=$PATH:$CATALINA_HOME/bin

# Make scripts executable
chmod +x $CATALINA_HOME/bin/*.sh
```

#### Windows:
1. Download from: https://tomcat.apache.org/download-90.cgi
2. Extract to: `C:\Program Files\Apache Tomcat 9.0`
3. Set `CATALINA_HOME` environment variable

---

### Step 2: Download Required Libraries

Place these JARs in the `lib/` directory:

```
lib/
├── mysql-connector-java-8.0.33.jar
├── commons-dbcp2-2.9.0.jar
├── commons-pool2-2.11.1.jar
├── commons-logging-1.2.jar
├── jbcrypt-0.4.jar
└── jstl-1.2.jar
```

**Quick Download:**
```bash
# Create lib directory
mkdir -p lib
cd lib

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

cd ..
```

---

### Step 3: Setup MySQL Database

```bash
# Login to MySQL
mysql -u root -p

# Run these commands:
```

```sql
CREATE DATABASE gradhire_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE USER 'gradhire_user'@'localhost' IDENTIFIED BY 'GradHire2025!';

GRANT ALL PRIVILEGES ON gradhire_db.* TO 'gradhire_user'@'localhost';
FLUSH PRIVILEGES;

exit;
```

```bash
# Import schema
mysql -u gradhire_user -p gradhire_db < sql/schema.sql
# Password: GradHire2025!
```

---

### Step 4: Configure Database Connection

Update `build.properties`:

```properties
# Tomcat path
env.CATALINA_HOME=/usr/local/tomcat

# Database
db.url=jdbc:mysql://localhost:3306/gradhire_db
db.username=gradhire_user
db.password=GradHire2025!
```

Also configure `database.properties.template` and save as `database.properties`:

```properties
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/gradhire_db?useSSL=false&serverTimezone=UTC
db.username=gradhire_user
db.password=GradHire2025!
db.initialSize=5
db.maxTotal=20
```

---

### Step 5: Build WAR File

```bash
# Clean and build
ant clean war

# Expected output:
# BUILD SUCCESSFUL
# WAR file created: dist/gradhire.war
```

---

### Step 6: Deploy to Tomcat

```bash
# Copy WAR to Tomcat
cp dist/gradhire.war $CATALINA_HOME/webapps/

# Or use Ant
ant deploy
```

---

### Step 7: Start Tomcat

```bash
# macOS/Linux
$CATALINA_HOME/bin/startup.sh

# Windows
"%CATALINA_HOME%\bin\startup.bat"
```

---

### Step 8: Access Application

**Open browser:**
```
http://localhost:8080/gradhire
```

**Test URLs:**
- Landing Page: `http://localhost:8080/gradhire/`
- Login: `http://localhost:8080/gradhire/login`
- Register: `http://localhost:8080/gradhire/register`
- Jobs: `http://localhost:8080/gradhire/jobs`

---

## 🎉 Success!

If you see the GradHire landing page, you're all set!

---

## 🐛 Troubleshooting

### Application Not Loading?

**Check Tomcat logs:**
```bash
tail -f $CATALINA_HOME/logs/catalina.out
```

**Common Issues:**

1. **Port 8080 in use:**
   ```bash
   # Kill process on port 8080
   lsof -i :8080
   kill -9 <PID>
   ```

2. **Database connection failed:**
   ```bash
   # Verify MySQL is running
   sudo service mysql status
   
   # Test connection
   mysql -u gradhire_user -p gradhire_db
   ```

3. **ClassNotFoundException:**
   ```bash
   # Ensure all JARs are in lib/
   ls -l lib/
   
   # Rebuild
   ant clean all deploy
   ```

4. **404 Not Found:**
   ```bash
   # Check deployment
   ls $CATALINA_HOME/webapps/gradhire.war
   ls $CATALINA_HOME/webapps/gradhire/
   
   # Restart Tomcat
   $CATALINA_HOME/bin/shutdown.sh
   $CATALINA_HOME/bin/startup.sh
   ```

---

## 📚 Next Steps

1. **Create test data:**
   ```sql
   mysql -u gradhire_user -p gradhire_db < sql/test_queries.sql
   ```

2. **Register a student account:**
   - Go to: http://localhost:8080/gradhire/register
   - Fill in details
   - Login and explore!

3. **Review full documentation:**
   - `DEPLOYMENT_GUIDE.md` - Complete deployment guide
   - `README.md` - Project overview
   - `QUICKSTART.md` - Quick start guide

---

## 🛑 Stopping the Application

```bash
# Stop Tomcat
$CATALINA_HOME/bin/shutdown.sh

# Or on Windows
"%CATALINA_HOME%\bin\shutdown.bat"
```

---

## 📞 Need Help?

- Check logs: `$CATALINA_HOME/logs/catalina.out`
- Review: `DEPLOYMENT_GUIDE.md` for detailed troubleshooting
- Verify: Database connection in `database.properties`

---

**Happy Coding! 🚀**



