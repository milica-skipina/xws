package com.example.tim2.dto;

import com.example.tim2.model.Car;

public class MiniCarDTO {
    private String make;
    private String model;
    private String fuel;
    private Double price;   // broj dana * cena na dan

    public MiniCarDTO() {
    }

    public MiniCarDTO(Car c) {
        this.make = c.getMake().getName();
        this.model = c.getModel().getName();
        this.fuel = c.getFuel().getName();
    }

    public MiniCarDTO(String make, String model, String fuel, Double price) {
        this.make = make;
        this.model = model;
        this.fuel = fuel;
        this.price = price;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
