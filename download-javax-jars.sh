#!/bin/bash
# Download javax servlet JARs for Tomcat 9 compatibility

echo "╔════════════════════════════════════════════════════════════╗"
echo "║  Downloading javax.servlet JARs for Tomcat 9              ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo ""

cd lib

# Download javax servlet API
echo "⏳ Downloading javax.servlet-api..."
curl -L -O https://repo1.maven.org/maven2/javax/servlet/javax.servlet-api/4.0.1/javax.servlet-api-4.0.1.jar
echo "✅ javax.servlet-api downloaded"

# Download javax servlet JSP API
echo "⏳ Downloading javax.servlet.jsp-api..."
curl -L -O https://repo1.maven.org/maven2/javax/servlet/jsp/javax.servlet.jsp-api/2.3.3/javax.servlet.jsp-api-2.3.3.jar
echo "✅ javax.servlet.jsp-api downloaded"

# Download JSTL
echo "⏳ Downloading JSTL 1.2..."
curl -L -O https://repo1.maven.org/maven2/javax/servlet/jstl/1.2/jstl-1.2.jar
echo "✅ JSTL downloaded"

# Copy to WEB-INF/lib
echo "⏳ Copying JARs to WEB-INF/lib..."
cp javax.servlet-api-4.0.1.jar ../web/WEB-INF/lib/
cp javax.servlet.jsp-api-2.3.3.jar ../web/WEB-INF/lib/
cp jstl-1.2.jar ../web/WEB-INF/lib/

cd ..

echo ""
echo "╔════════════════════════════════════════════════════════════╗"
echo "║  ✅ All javax JARs Downloaded!                            ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo ""
echo "📦 JAR Summary:"
ls -lh lib/*.jar | grep -E "javax|jstl"
echo ""
echo "🚀 Ready to build with: ant clean war"
echo ""

