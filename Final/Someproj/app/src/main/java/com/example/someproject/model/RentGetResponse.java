package com.example.someproject.model;

import java.util.List;

public class RentGetResponse {

    private List<Rent> rents;
    private List<Car> cars;

    public RentGetResponse(List<Rent> rents, List<Car> cars) {
        this.rents = rents;
        this.cars = cars;
    }

    public List<Rent> getRents() {
        return rents;
    }

    public void setRents(List<Rent> rents) {
        this.rents = rents;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }
}

