package com.example.adservice.dto;

import java.util.List;

public class SearchDTO {

    private String city;
    private String startDate;
    private String endDate;
    private List<String> brand;
    private List<String> model;
    private List<String> gearbox;
    private List<String> fuel;
    private List<String> carClass;
    private Double minPrice;
    private Double maxPrice;
    private Double mileage;
    private Integer kidsSeats;
    private Double mileageLimit;
    private Boolean damageWaiver;

    public SearchDTO(){

    }

    public SearchDTO(String city, String startDate, String endDate, List<String> brand, List<String> model, List<String> gearbox, List<String> fuel, List<String> carClass, Double minPrice, Double maxPrice, Double mileage, Integer kidsSeats, Double mileageLimit, Boolean damageWaiver) {
        this.city = city;
        this.startDate = startDate;
        this.endDate = endDate;
        this.brand = brand;
        this.model = model;
        this.gearbox = gearbox;
        this.fuel = fuel;
        this.carClass = carClass;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.mileage = mileage;
        this.kidsSeats = kidsSeats;
        this.mileageLimit = mileageLimit;
        this.damageWaiver = damageWaiver;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public List<String> getBrand() {
        return brand;
    }

    public void setBrand(List<String> brand) {
        this.brand = brand;
    }

    public List<String> getModel() {
        return model;
    }

    public void setModel(List<String> model) {
        this.model = model;
    }

    public List<String> getGearbox() {
        return gearbox;
    }

    public void setGearbox(List<String> gearbox) {
        this.gearbox = gearbox;
    }

    public List<String> getFuel() {
        return fuel;
    }

    public void setFuel(List<String> fuel) {
        this.fuel = fuel;
    }

    public List<String> getCarClass() {
        return carClass;
    }

    public void setCarClass(List<String> carClass) {
        this.carClass = carClass;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(Double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }

    public Integer getKidsSeats() {
        return kidsSeats;
    }

    public void setKidsSeats(Integer kidsSeats) {
        this.kidsSeats = kidsSeats;
    }

    public Double getMileageLimit() {
        return mileageLimit;
    }

    public void setMileageLimit(Double mileageLimit) {
        this.mileageLimit = mileageLimit;
    }

    public Boolean getDamageWaiver() {
        return damageWaiver;
    }

    public void setDamageWaiver(Boolean damageWaiver) {
        this.damageWaiver = damageWaiver;
    }
}
