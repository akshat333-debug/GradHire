-- GradHire Database Setup Script
-- Run this with: mysql -u root -p < setup_database.sql

-- Create database
CREATE DATABASE IF NOT EXISTS gradhire_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Create user
CREATE USER IF NOT EXISTS 'gradhire_user'@'localhost' IDENTIFIED BY 'secure_password123';

-- Grant privileges
GRANT ALL PRIVILEGES ON gradhire_db.* TO 'gradhire_user'@'localhost';

-- Flush privileges
FLUSH PRIVILEGES;

-- Use the database
USE gradhire_db;

-- Show confirmation
SELECT 'Database setup completed successfully!' as status;
SHOW DATABASES LIKE 'gradhire_db';
SELECT User, Host FROM mysql.user WHERE User = 'gradhire_user';
