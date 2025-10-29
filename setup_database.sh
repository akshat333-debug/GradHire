#!/bin/bash

# GradHire Database Setup Script
# This script will set up the MySQL database for GradHire

echo "🚀 Setting up GradHire Database..."
echo ""

# Check if MySQL is running
if ! pgrep -x "mysqld" > /dev/null; then
    echo "❌ MySQL is not running. Please start MySQL first."
    echo "   Try: brew services start mysql"
    exit 1
fi

echo "✅ MySQL is running"
echo ""

# Prompt for MySQL root password
echo "Please enter your MySQL root password:"
read -s MYSQL_PASSWORD

echo ""
echo "📊 Creating database and user..."

# Create database and user
mysql -u root -p"$MYSQL_PASSWORD" << EOF
CREATE DATABASE IF NOT EXISTS gradhire_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'gradhire_user'@'localhost' IDENTIFIED BY 'secure_password123';
GRANT ALL PRIVILEGES ON gradhire_db.* TO 'gradhire_user'@'localhost';
FLUSH PRIVILEGES;
EOF

if [ $? -eq 0 ]; then
    echo "✅ Database and user created successfully"
else
    echo "❌ Failed to create database. Please check your MySQL root password."
    exit 1
fi

echo ""
echo "📋 Importing database schema..."

# Import schema
mysql -u root -p"$MYSQL_PASSWORD" gradhire_db < sql/schema.sql

if [ $? -eq 0 ]; then
    echo "✅ Database schema imported successfully"
else
    echo "❌ Failed to import schema"
    exit 1
fi

echo ""
echo "🔍 Verifying setup..."

# Verify setup
mysql -u gradhire_user -p'secure_password123' gradhire_db -e "SHOW TABLES;" 2>/dev/null

if [ $? -eq 0 ]; then
    echo "✅ Database setup completed successfully!"
    echo ""
    echo "📝 Database Details:"
    echo "   Database: gradhire_db"
    echo "   Username: gradhire_user"
    echo "   Password: secure_password123"
    echo ""
    echo "🌐 You can now register accounts at: http://localhost:8080/gradhire/register"
else
    echo "❌ Database verification failed"
    exit 1
fi
