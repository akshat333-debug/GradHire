#!/bin/bash
# Revert Jakarta EE migration back to javax.servlet for Tomcat 9
# Run this script from the project root

echo "╔════════════════════════════════════════════════════════════╗"
echo "║  Reverting to javax.servlet for Tomcat 9 Compatibility    ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo ""

# Step 1: Revert Java imports
echo "⏳ Step 1/5: Reverting Java imports (jakarta → javax)..."
find src -name "*.java" -type f -exec sed -i '' 's/import jakarta\.servlet/import javax.servlet/g' {} \;
echo "✅ Java imports reverted"

# Step 2: Revert JSP taglib URIs
echo "⏳ Step 2/5: Reverting JSP taglib URIs..."
find web -name "*.jsp" -type f -exec sed -i '' 's|jakarta\.tags\.core|http://java.sun.com/jsp/jstl/core|g' {} \;
find web -name "*.jsp" -type f -exec sed -i '' 's|jakarta\.tags\.fmt|http://java.sun.com/jsp/jstl/fmt|g' {} \;
find web -name "*.jsp" -type f -exec sed -i '' 's|jakarta\.tags\.functions|http://java.sun.com/jsp/jstl/functions|g' {} \;
echo "✅ JSP taglibs reverted"

# Step 3: Revert web.xml namespace
echo "⏳ Step 3/5: Reverting web.xml namespace..."
sed -i '' 's|https://jakarta.ee/xml/ns/jakartaee|http://xmlns.jcp.org/xml/ns/javaee|g' web/WEB-INF/web.xml
sed -i '' 's|https://jakarta.ee/xml/ns/jakartaee/web-app_6_0.xsd|http://xmlns.jcp.org/xml/ns/javaee/web-app_4_0.xsd|g' web/WEB-INF/web.xml
sed -i '' 's|version="6.0"|version="4.0"|g' web/WEB-INF/web.xml
echo "✅ web.xml reverted"

# Step 4: Remove Jakarta JARs
echo "⏳ Step 4/5: Removing Jakarta JARs..."
rm -f lib/jakarta.servlet-api-*.jar
rm -f lib/jakarta.servlet.jsp-api-*.jar
rm -f lib/jakarta.servlet.jsp.jstl-*.jar
rm -f web/WEB-INF/lib/jakarta.servlet*.jar
echo "✅ Jakarta JARs removed"

# Step 5: Update build.xml exclusions
echo "⏳ Step 5/5: Updating build.xml..."
sed -i '' 's/jakarta\.servlet-api-\*\.jar/javax.servlet-api-*.jar/g' build.xml
sed -i '' 's/jakarta\.servlet\.jsp-api-\*\.jar/javax.servlet.jsp-api-*.jar/g' build.xml
echo "✅ build.xml updated"

echo ""
echo "╔════════════════════════════════════════════════════════════╗"
echo "║  ✅ Reversion Complete!                                    ║"
echo "╚════════════════════════════════════════════════════════════╝"
echo ""
echo "📦 Next: Download javax JARs"
echo "   Run: ./download-javax-jars.sh"
echo ""

