package com.example.adservice.dto;


import com.example.adservice.model.Advertisement;

import java.util.Date;

public class AdvertisementOrderDTO {

    private Long id;
    private CarOrderDTO carAd;
    private Date startDate;
    private Date endDate;
    private String city;
    private String make;
    private String model;
    private double rating;
    private PricelistDTO pricelist;
    private String entrepreneur;        //name
    private String customer;


    public AdvertisementOrderDTO(Long id, CarOrderDTO carAd, Date startDate, Date endDate, String city, String make,
                                 String model, double rating, PricelistDTO pricelist, String entrepreneur, String customer) {
        this.id = id;
        this.carAd = carAd;
        this.startDate = startDate;
        this.endDate = endDate;
        this.city = city;
        this.make = make;
        this.model = model;
        this.rating = rating;
        this.pricelist = pricelist;
        this.entrepreneur = entrepreneur;
        this.customer = customer;
    }

    public AdvertisementOrderDTO() {
    }

    public AdvertisementOrderDTO(Advertisement a){
        this.id = a.getId();
        this.carAd = new CarOrderDTO(a.getCarAd());
        this.startDate = a.getStartDate();
        this.endDate = a.getEndDate();
        this.pricelist = new PricelistDTO(a.getPricelist());
        this.city = a.getCity();
        this.make = a.getCarAd().getMake().getName();
        this.model = a.getCarAd().getModel().getName();
        this.rating = a.getCarAd().getRaiting();
        this.entrepreneur = a.getEntrepreneurName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CarOrderDTO getCarAd() {
        return carAd;
    }

    public void setCarAd(CarOrderDTO carAd) {
        this.carAd = carAd;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public PricelistDTO getPricelist() {
        return pricelist;
    }

    public void setPricelist(PricelistDTO pricelist) {
        this.pricelist = pricelist;
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

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
}
