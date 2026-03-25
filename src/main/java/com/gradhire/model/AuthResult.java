package com.gradhire.model;

public class AuthResult {
    private final int userId;
    private final String userType;
    private final String fullName;

    public AuthResult(int userId, String userType, String fullName) {
        this.userId = userId;
        this.userType = userType;
        this.fullName = fullName;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserType() {
        return userType;
    }

    public String getFullName() {
        return fullName;
    }
}
