package com.example.someproject.model;

public class Car {

    private String _id;
    private String name;
    private String description;
    private boolean enabled;
    private double tariff;
    private String image;

    public Car(String _id, String name, String description, boolean enabled, double tariff, String image) {
        this._id = _id;
        this.name = name;
        this.description = description;
        this.enabled = enabled;
        this.tariff = tariff;
        this.image = image;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public double getTariff() {
        return tariff;
    }

    public void setTariff(double tariff) {
        this.tariff = tariff;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Car{" +
                "_id='" + _id + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", enabled=" + enabled +
                ", tariff=" + tariff +
                ", image='" + image + '\'' +
                '}';
    }
}

