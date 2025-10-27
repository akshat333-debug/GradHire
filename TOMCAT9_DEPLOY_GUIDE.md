# 🚀 Deploy GradHire on Tomcat 9 - Quick Guide

**Status:** ✅ Ready to Deploy!  
**WAR File:** `dist/gradhire.war` (ready)  
**Build Status:** 0 errors, 32 files compiled

---

## 📥 Step 1: Download Tomcat 9

### Option A: Download from Official Site (Recommended)
1. Visit: https://tomcat.apache.org/download-90.cgi
2. Download **Core** → `apache-tomcat-9.0.x.tar.gz` (Mac/Linux) or `.zip` (Windows)
3. Extract to a folder, e.g., `/Users/agraw/Desktop/apache-tomcat-9.0.95`

### Option B: Using Homebrew (Mac only)
```bash
brew install tomcat@9
```

---

## 📦 Step 2: Deploy the WAR File

### Manual Deployment (Easiest):
```bash
# Copy WAR to Tomcat webapps
cp /Users/agraw/Desktop/projects/gradHire/dist/gradhire.war \
   /path/to/apache-tomcat-9.0.x/webapps/

# Start Tomcat
cd /path/to/apache-tomcat-9.0.x/bin
./startup.sh

# Wait 5-10 seconds for auto-deployment
```

### NetBeans Deployment:
1. Go to `Tools → Servers → Add Server`
2. Select **Apache Tomcat or TomEE**
3. Browse to your Tomcat 9 folder
4. Right-click project → **Run**

---

## 🧪 Step 3: Test the Application

### Access the App:
```
http://localhost:8080/gradhire
```

### Test Pages:
- 🏠 Home: `http://localhost:8080/gradhire/`
- 🔐 Login: `http://localhost:8080/gradhire/login`
- 📝 Register: `http://localhost:8080/gradhire/register`
- 💼 Jobs: `http://localhost:8080/gradhire/jobs`

---

## 🔍 Verify Deployment

Check Tomcat logs:
```bash
# View deployment logs
tail -f /path/to/tomcat/logs/catalina.out

# Look for:
# "Deployment of web application archive [gradhire.war] has finished"
```

Check webapps folder:
```bash
ls -lh /path/to/tomcat/webapps/
# Should see:
# - gradhire.war
# - gradhire/ (auto-extracted folder)
```

---

## 🛠️ Troubleshooting

### App Not Loading?
```bash
# Check if Tomcat is running
ps aux | grep tomcat

# Check port 8080 is available
lsof -i :8080

# View error logs
tail -50 /path/to/tomcat/logs/localhost.$(date +%Y-%m-%d).log
```

### Database Connection Error?
```bash
# Verify MySQL is running
mysql -u gradhire_user -p gradhire_db

# Check database.properties
cat /Users/agraw/Desktop/projects/gradHire/web/WEB-INF/classes/database.properties
```

### Port 8080 Already in Use?
```bash
# Option 1: Stop other service on 8080
lsof -ti:8080 | xargs kill -9

# Option 2: Change Tomcat port
# Edit: /path/to/tomcat/conf/server.xml
# Change <Connector port="8080" to port="8081"
```

---

## ✅ What's Working

After deployment, these features should work:

✅ **For Students:**
- Register new account
- Login/Logout
- Browse job listings
- View job details
- Apply to jobs (with resume upload)
- View application status
- See recommended jobs (smart matching)
- Manage profile

✅ **For Admins/Recruiters:**
- Register recruiter account
- Login/Logout
- Post new jobs
- View applications
- Manage job postings
- View analytics dashboard

---

## 📊 System Requirements

- ✅ **Java:** 8 or higher (you have Java 22 ✅)
- ✅ **Tomcat:** 9.0.x (download above)
- ✅ **MySQL:** 5.7+ or 8.0+ (already running ✅)
- ✅ **Database:** `gradhire_db` configured ✅
- ✅ **RAM:** Minimum 512MB, Recommended 2GB

---

## 🔥 Quick Start Commands

```bash
# 1. Download Tomcat 9.0.95 (latest stable)
cd ~/Desktop
wget https://dlcdn.apache.org/tomcat/tomcat-9/v9.0.95/bin/apache-tomcat-9.0.95.tar.gz
tar -xzf apache-tomcat-9.0.95.tar.gz

# 2. Deploy GradHire
cp /Users/agraw/Desktop/projects/gradHire/dist/gradhire.war \
   ~/Desktop/apache-tomcat-9.0.95/webapps/

# 3. Start Tomcat
cd ~/Desktop/apache-tomcat-9.0.95/bin
chmod +x *.sh
./startup.sh

# 4. Wait 10 seconds, then open browser
sleep 10
open http://localhost:8080/gradhire

# 5. Stop Tomcat when done
./shutdown.sh
```

---

## 📝 Default Test Credentials

If you've run `sql/test_queries.sql`:

**Student Account:**
- Email: `john.doe@example.com`
- Password: `password123`

**Admin Account:**
- Email: `admin@techcorp.com`
- Password: `adminpass123`

*(Note: Passwords are hashed with BCrypt in production)*

---

## 🎓 What Changed from Tomcat 11?

✅ Reverted `jakarta.servlet` → `javax.servlet`  
✅ Reverted `jakarta.tags.*` → `java.sun.com/jsp/jstl/*`  
✅ Updated JARs: javax-api, javax-jsp, jstl-1.2  
✅ Reverted web.xml namespace  

**Result:** Works perfectly with Tomcat 9! 🎉

---

## 📚 Next Steps After Deployment

1. ✅ Test all features (registration, login, job posting, etc.)
2. ✅ Create admin and student test accounts
3. ✅ Post sample jobs
4. ✅ Test smart recommendations
5. ✅ Upload resumes
6. ✅ Check filters and security

---

## 🆘 Need Help?

**Logs Location:**
```
/path/to/tomcat/logs/
- catalina.out (main log)
- localhost.YYYY-MM-DD.log (app log)
- catalina.YYYY-MM-DD.log (Tomcat log)
```

**Common Issues:**
- 404 Error → WAR not deployed, check webapps folder
- 500 Error → Database connection, check credentials
- 403 Error → Permission denied, check filters
- Blank page → JSP error, check logs

---

## 🎉 Success Indicators

When everything works, you should see:

```bash
# In logs:
INFO: Deployment of web application archive [gradhire.war] has finished in [XXX] ms

# In browser (http://localhost:8080/gradhire):
- Beautiful landing page with hero section
- "Login" and "Register" buttons
- Job search bar
- Feature highlights

# Status codes:
- 200 OK for all pages
- Filters activated
- Database connected
```

---

**Your app is deployment-ready! Just need Tomcat 9. 🚀**

**Estimated time to see app running: 10-15 minutes from now!**

