# 🚀 Running GradHire in NetBeans - Quick Guide

**Status:** ✅ Configured for Tomcat 9.0.111

---

## ✅ What We Just Fixed

1. ✅ Updated `build.properties` → Points to Tomcat 9.0.111
2. ✅ Updated `build.xml` → Uses Tomcat 9.0.111
3. ✅ Updated `nbproject/private/private.properties` → Tomcat 9 configured
4. ✅ Stopped manual Tomcat instance → Port 8080 free

---

## 🎯 How to Run from NetBeans

### Step 1: Add Tomcat 9 Server (If Not Already Added)

1. Go to **Tools** → **Servers**
2. Click **"Add Server"**
3. Select **"Apache Tomcat or TomEE"**
4. Click **"Next"**
5. Browse to: `/Users/agraw/Desktop/apache-tomcat-9.0.111`
6. Server Name: `Apache Tomcat 9.0.111`
7. Click **"Finish"**

### Step 2: Set Project Server

1. **Right-click** your `gradHire` project
2. Select **"Properties"**
3. Go to **"Run"** category
4. In **"Server"** dropdown:
   - Select **"Apache Tomcat 9.0.111"**
5. Click **"OK"**

### Step 3: Run the Project

1. **Right-click** the `gradHire` project
2. Select **"Run"** (or press **F6**)
3. NetBeans will:
   - Build the project
   - Start Tomcat 9
   - Deploy `gradhire.war`
   - Open browser automatically

---

## 🔍 Verify It's Working

After clicking "Run", you should see in NetBeans Output:

```
BUILD SUCCESSFUL
Starting Tomcat...
Tomcat started.
Deploying gradhire.war...
Deployment successful.
Opening browser: http://localhost:8080/gradhire
```

---

## ⚙️ NetBeans Server Status

### To View Server Status:
- **Services** tab → **Servers** → **Apache Tomcat 9.0.111**
- Right-click → **Start/Stop/Restart**

### Server Indicator:
- 🟢 **Green arrow** = Server running
- 🔴 **Red square** = Server stopped

---

## 🐛 Troubleshooting

### Issue: "Server not found" or dropdown empty

**Solution:**
1. Close NetBeans
2. Delete: `nbproject/private/private.properties`
3. Reopen NetBeans
4. Add Tomcat 9 server again (Step 1 above)

### Issue: "Port 8080 already in use"

**Solution:**
```bash
# Check what's using port 8080
lsof -i :8080

# Kill it
lsof -ti:8080 | xargs kill -9

# Then run from NetBeans again
```

### Issue: "Build successful but not deploying"

**Solution:**
1. Stop server in NetBeans (right-click server → Stop)
2. Clean project (right-click project → Clean)
3. Rebuild (right-click project → Build)
4. Run (right-click project → Run)

### Issue: "ClassNotFoundException" or "NoClassDefFoundError"

**Solution:**
1. Right-click project → **Properties**
2. Go to **"Libraries"**
3. Check that all JARs are present:
   - `lib/*.jar` (bcrypt, commons, jstl, etc.)
   - MySQL connector
   - Javax servlet JARs (for compilation only)

---

## 📁 Current Configuration

**Tomcat Location:**
```
/Users/agraw/Desktop/apache-tomcat-9.0.111
```

**WAR Output:**
```
/Users/agraw/Desktop/projects/gradHire/dist/gradhire.war
```

**Deployment URL:**
```
http://localhost:8080/gradhire
```

**Webapps Folder:**
```
/Users/agraw/Desktop/apache-tomcat-9.0.111/webapps/
```

---

## 🔥 Quick Commands (Terminal Alternative)

If NetBeans isn't working, you can always deploy manually:

```bash
# Build WAR
cd /Users/agraw/Desktop/projects/gradHire
ant clean war

# Deploy to Tomcat
cp dist/gradhire.war /Users/agraw/Desktop/apache-tomcat-9.0.111/webapps/

# Start Tomcat
cd /Users/agraw/Desktop/apache-tomcat-9.0.111/bin
./startup.sh

# Open browser
open http://localhost:8080/gradhire
```

---

## ✅ Success Checklist

- [ ] Tomcat 9 server added in NetBeans
- [ ] Project server set to Tomcat 9
- [ ] Build successful (check Output tab)
- [ ] Tomcat starts (green arrow in Services)
- [ ] Browser opens automatically
- [ ] Landing page loads (HTTP 200)
- [ ] Can navigate to login/register

---

## 🎯 Next Steps After Running

1. ✅ Test student registration
2. ✅ Test admin registration
3. ✅ Browse job listings
4. ✅ Apply to a job
5. ✅ Test smart recommendations
6. ✅ Upload resume
7. ✅ Test profile management

---

**You're all set! Just click Run (F6) in NetBeans and your app will launch!** 🚀

