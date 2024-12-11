package com.example.someproject.model;

public class RentResponse {

    private String message;
    private String status;
    private String newusercar; // Can be used to represent the new rental information

    // Constructor
    public RentResponse(String message, String status, String newusercar) {
        this.message = message;
        this.status = status;
        this.newusercar = newusercar;
    }

    // Getters and Setters
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

    public String getNewusercar() {
        return newusercar;
    }

    public void setNewusercar(String newusercar) {
        this.newusercar = newusercar;
    }
}

