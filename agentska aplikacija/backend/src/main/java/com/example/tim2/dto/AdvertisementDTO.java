package com.example.tim2.dto;

import com.example.tim2.model.*;
import com.example.tim2.model.Discount;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class AdvertisementDTO {

    private Long id;
    private CarDTO carAd;
    private Date startDate;
    private Date endDate;
    private Set<Discount> discounts = new HashSet<Discount>();
    private String city;
    private  Pricelist pricelist;

    public AdvertisementDTO() {
    }

    public AdvertisementDTO(Advertisement a){
        this(a.getId(),new CarDTO(a.getCarAd()),a.getStartDate(),a.getEndDate(), a.getDiscounts(), a.getCity(), a.getPricelist());
        if(a.getCarAd()!=null){
            this.carAd = new CarDTO(a.getCarAd());
        }
    }
    public AdvertisementDTO(Long id, CarDTO carAd, Date startDate, Date endDate, Set<Discount>dis, String city, Pricelist pricelist) {
        this.id = id;
        this.carAd = carAd;
        this.startDate = startDate;
        this.endDate = endDate;
        this.discounts = dis;
        this.pricelist = pricelist;
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CarDTO getCarAd() {
        return carAd;
    }

    public void setCarAd(CarDTO carAd) {
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

    public Set<Discount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(Set<Discount> discounts) {
        this.discounts = discounts;
    }

    public Pricelist getPricelist() {
        return pricelist;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setPricelist(Pricelist pricelist) {
        this.pricelist = pricelist;
    }
}
