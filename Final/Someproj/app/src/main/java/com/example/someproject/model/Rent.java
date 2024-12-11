package com.example.someproject.model;

public class Rent {

    private String carId;
    private String userId;
    private String rentStartDate;
    private String rentEndDate;

    public Rent(String carId, String userId, String rentStartDate, String rentEndDate) {
        this.carId = carId;
        this.userId = userId;
        this.rentStartDate = rentStartDate;
        this.rentEndDate = rentEndDate;
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

    public String getRentStartDate() {
        return rentStartDate;
    }

    public void setRentStartDate(String rentStartDate) {
        this.rentStartDate = rentStartDate;
    }

    public String getRentEndDate() {
        return rentEndDate;
    }

    public void setRentEndDate(String rentEndDate) {
        this.rentEndDate = rentEndDate;
    }
}