package com.example.someproject.model;

public class Rent {

    private String carId;
    private String userId;

    public Rent(String carId, String userId) {
        this.carId = carId;
        this.userId = userId;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}