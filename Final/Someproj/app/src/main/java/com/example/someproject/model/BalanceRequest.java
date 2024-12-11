package com.example.someproject.model;

public class BalanceRequest {

    private double amount;

    public BalanceRequest(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}

