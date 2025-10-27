package com.gradhire.util;

import java.util.regex.Pattern;

/**
 * Input Validation Utility
 * Validates and sanitizes user inputs to prevent security vulnerabilities
 * 
 * @author GradHire Team
 * @version 1.0
 */
public class Validator {
    
    // Email regex pattern (RFC 5322 simplified)
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );
    
    // Phone number pattern (flexible for international formats)
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[+]?[0-9]{1,4}?[-\\s]?[(]?[0-9]{1,4}[)]?[-\\s]?[0-9]{1,4}[-\\s]?[0-9]{1,9}$"
    );
    
    // URL pattern
    private static final Pattern URL_PATTERN = Pattern.compile(
        "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$",
        Pattern.CASE_INSENSITIVE
    );
    
    // Alphanumeric pattern
    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");
    
    // SQL injection keywords (basic check)
    private static final String[] SQL_KEYWORDS = {
        "SELECT", "INSERT", "UPDATE", "DELETE", "DROP", "CREATE", "ALTER", 
        "EXEC", "EXECUTE", "SCRIPT", "UNION", "OR 1=1", "' OR '1'='1"
    };
    
    // XSS patterns
    private static final Pattern[] XSS_PATTERNS = {
        Pattern.compile("<script", Pattern.CASE_INSENSITIVE),
        Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
        Pattern.compile("onerror=", Pattern.CASE_INSENSITIVE),
        Pattern.compile("onclick=", Pattern.CASE_INSENSITIVE),
        Pattern.compile("onload=", Pattern.CASE_INSENSITIVE)
    };
    
    /**
     * Check if string is null or empty
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
    
    /**
     * Check if string is not null and not empty
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }
    
    /**
     * Validate email address
     * 
     * @param email Email to validate
     * @return true if valid email format
     */
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
    
    /**
     * Validate phone number
     * 
     * @param phone Phone number to validate
     * @return true if valid phone format
     */
    public static boolean isValidPhone(String phone) {
        if (isEmpty(phone)) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone.trim()).matches();
    }
    
    /**
     * Validate URL
     * 
     * @param url URL to validate
     * @return true if valid URL format
     */
    public static boolean isValidURL(String url) {
        if (isEmpty(url)) {
            return false;
        }
        return URL_PATTERN.matcher(url.trim()).matches();
    }
    
    /**
     * Validate string length
     * 
     * @param str String to check
     * @param minLength Minimum length (inclusive)
     * @param maxLength Maximum length (inclusive)
     * @return true if length is within range
     */
    public static boolean isValidLength(String str, int minLength, int maxLength) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        return length >= minLength && length <= maxLength;
    }
    
    /**
     * Validate integer within range
     * 
     * @param value Integer value to check
     * @param min Minimum value (inclusive)
     * @param max Maximum value (inclusive)
     * @return true if value is within range
     */
    public static boolean isValidRange(int value, int min, int max) {
        return value >= min && value <= max;
    }
    
    /**
     * Validate year (graduation year, etc.)
     * 
     * @param year Year to validate
     * @return true if valid year (current year to 10 years in future)
     */
    public static boolean isValidYear(int year) {
        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        return year >= currentYear && year <= currentYear + 10;
    }
    
    /**
     * Check if string is alphanumeric
     * 
     * @param str String to check
     * @return true if contains only letters and numbers
     */
    public static boolean isAlphanumeric(String str) {
        if (isEmpty(str)) {
            return false;
        }
        return ALPHANUMERIC_PATTERN.matcher(str).matches();
    }
    
    /**
     * Basic SQL injection detection
     * 
     * @param input String to check
     * @return true if potential SQL injection detected
     */
    public static boolean containsSQLInjection(String input) {
        if (isEmpty(input)) {
            return false;
        }
        
        String upperInput = input.toUpperCase();
        for (String keyword : SQL_KEYWORDS) {
            if (upperInput.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Basic XSS (Cross-Site Scripting) detection
     * 
     * @param input String to check
     * @return true if potential XSS detected
     */
    public static boolean containsXSS(String input) {
        if (isEmpty(input)) {
            return false;
        }
        
        for (Pattern pattern : XSS_PATTERNS) {
            if (pattern.matcher(input).find()) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Sanitize string for HTML output (prevent XSS)
     * 
     * @param input String to sanitize
     * @return Sanitized string
     */
    public static String sanitizeHTML(String input) {
        if (input == null) {
            return "";
        }
        
        return input
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#x27;")
            .replace("/", "&#x2F;");
    }
    
    /**
     * Validate and sanitize string input
     * 
     * @param input String to validate and sanitize
     * @param minLength Minimum length
     * @param maxLength Maximum length
     * @return Sanitized string or null if invalid
     */
    public static String validateAndSanitize(String input, int minLength, int maxLength) {
        if (isEmpty(input)) {
            return null;
        }
        
        // Trim whitespace
        String sanitized = input.trim();
        
        // Check length
        if (!isValidLength(sanitized, minLength, maxLength)) {
            return null;
        }
        
        // Check for SQL injection
        if (containsSQLInjection(sanitized)) {
            return null;
        }
        
        // Sanitize HTML
        return sanitizeHTML(sanitized);
    }
    
    /**
     * Validate file extension
     * 
     * @param filename Filename to check
     * @param allowedExtensions Array of allowed extensions (e.g., ["pdf", "doc", "docx"])
     * @return true if extension is allowed
     */
    public static boolean isValidFileExtension(String filename, String[] allowedExtensions) {
        if (isEmpty(filename)) {
            return false;
        }
        
        int lastDot = filename.lastIndexOf('.');
        if (lastDot == -1) {
            return false;
        }
        
        String extension = filename.substring(lastDot + 1).toLowerCase();
        
        for (String allowed : allowedExtensions) {
            if (extension.equals(allowed.toLowerCase())) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Validate file size
     * 
     * @param sizeInBytes File size in bytes
     * @param maxSizeInMB Maximum allowed size in MB
     * @return true if size is within limit
     */
    public static boolean isValidFileSize(long sizeInBytes, int maxSizeInMB) {
        long maxSizeInBytes = maxSizeInMB * 1024L * 1024L;
        return sizeInBytes > 0 && sizeInBytes <= maxSizeInBytes;
    }
    
    /**
     * Parse integer safely
     * 
     * @param str String to parse
     * @param defaultValue Default value if parsing fails
     * @return Parsed integer or default value
     */
    public static int parseInt(String str, int defaultValue) {
        if (isEmpty(str)) {
            return defaultValue;
        }
        
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    /**
     * Parse double safely
     * 
     * @param str String to parse
     * @param defaultValue Default value if parsing fails
     * @return Parsed double or default value
     */
    public static double parseDouble(String str, double defaultValue) {
        if (isEmpty(str)) {
            return defaultValue;
        }
        
        try {
            return Double.parseDouble(str.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    /**
     * Validate name (letters, spaces, hyphens only)
     * 
     * @param name Name to validate
     * @return true if valid name format
     */
    public static boolean isValidName(String name) {
        if (isEmpty(name)) {
            return false;
        }
        return name.matches("^[a-zA-Z\\s'-]+$");
    }
    
    /**
     * Truncate string to maximum length
     * 
     * @param str String to truncate
     * @param maxLength Maximum length
     * @return Truncated string
     */
    public static String truncate(String str, int maxLength) {
        if (str == null) {
            return "";
        }
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength) + "...";
    }
}

