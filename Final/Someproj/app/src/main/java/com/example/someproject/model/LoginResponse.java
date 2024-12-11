package com.example.someproject.model;

public class LoginResponse {
    private String message;
    private String status;
    private String token;
    private String role;

    public LoginResponse() {
    }

    public LoginResponse(String message, String status, String token, String role) {
        this.message = message;
        this.status = status;
        this.token = token;
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
