package com.example.someproject.model;

public class RentRequest {

    private String carId;

    public RentRequest(String carId) {
        this.carId = carId;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }
}
