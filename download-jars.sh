#!/bin/bash
# GradHire - Download Required JAR Files
# Usage: chmod +x download-jars.sh && ./download-jars.sh

echo "╔════════════════════════════════════════════════════╗"
echo "║  📦 Downloading Required JAR Files for GradHire   ║"
echo "╚════════════════════════════════════════════════════╝"
echo ""

# Create lib directory if it doesn't exist
mkdir -p lib
cd lib

echo "📥 Downloading 6 JAR files..."
echo ""

# 1. MySQL Connector
echo "[1/6] Downloading MySQL Connector..."
curl -L -O https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.0.33/mysql-connector-j-8.0.33.jar

# 2. Commons DBCP2
echo "[2/6] Downloading Commons DBCP2..."
curl -L -O https://repo1.maven.org/maven2/org/apache/commons/commons-dbcp2/2.9.0/commons-dbcp2-2.9.0.jar

# 3. Commons Pool2
echo "[3/6] Downloading Commons Pool2..."
curl -L -O https://repo1.maven.org/maven2/org/apache/commons/commons-pool2/2.11.1/commons-pool2-2.11.1.jar

# 4. Commons Logging
echo "[4/6] Downloading Commons Logging..."
curl -L -O https://repo1.maven.org/maven2/commons-logging/commons-logging/1.2/commons-logging-1.2.jar

# 5. jBCrypt
echo "[5/6] Downloading jBCrypt..."
curl -L -O https://repo1.maven.org/maven2/org/mindrot/jbcrypt/0.4/jbcrypt-0.4.jar

# 6. JSTL
echo "[6/6] Downloading JSTL..."
curl -L -O https://repo1.maven.org/maven2/javax/servlet/jstl/1.2/jstl-1.2.jar

echo ""
echo "✅ Download complete!"
echo ""
echo "📊 Downloaded files:"
ls -lh *.jar

# Copy to WEB-INF/lib
echo ""
echo "📁 Copying JARs to web/WEB-INF/lib/..."
mkdir -p ../web/WEB-INF/lib
cp *.jar ../web/WEB-INF/lib/

echo ""
echo "╔════════════════════════════════════════════════════╗"
echo "║  ✅ All JAR files downloaded and copied!          ║"
echo "╚════════════════════════════════════════════════════╝"
echo ""
echo "Next steps:"
echo "  1. Setup MySQL database (see NETBEANS_SETUP.md)"
echo "  2. Configure database.properties"
echo "  3. Open project in NetBeans"
echo "  4. Build and Run!"
echo ""



