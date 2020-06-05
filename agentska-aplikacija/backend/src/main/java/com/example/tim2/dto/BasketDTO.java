package com.example.tim2.dto;

import com.example.tim2.model.Advertisement;

import java.util.Date;

public class BasketDTO {
    private Long advertisementId;
    private Date startDate;
    private Date endDate;
    private String city;
    private String make;
    private String model;
    private double price;
    private double rating;
    private String entrepreneur;

    public BasketDTO() {
    }

    public BasketDTO(Advertisement ad) {
        this.startDate = ad.getStartDate();
        this.endDate = ad.getEndDate();
        this.city = ad.getCity();
        this.make = ad.getCarAd().getMake().getName();      // naziv proizvodjaca ?
        this.model = ad.getCarAd().getModel().getName();
        this.rating = ad.getCarAd().getRaiting();
        this.entrepreneur = ad.getEntrepreneur().getCompanyName();
        this.advertisementId = ad.getId();
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getEntrepreneur() {
        return entrepreneur;
    }

    public void setEntrepreneur(String entrepreneur) {
        this.entrepreneur = entrepreneur;
    }

    public Long getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(Long advertisementId) {
        this.advertisementId = advertisementId;
    }
}
