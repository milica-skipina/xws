package com.example.adservice.dto;

import com.example.adservice.model.Advertisement;
import com.example.adservice.model.Pricelist;

import java.time.LocalDate;
import java.util.Date;

public class AdvertisementDTO {
    private Long id;
    private CarDTO carAd;
    private Date startDate;
    private Date endDate;
    private Pricelist pricelist;
    private String city;
    private boolean deleted;
    private boolean request;
    private String name;
    private String username;


    public AdvertisementDTO() {
        this.carAd = new CarDTO();
        this.pricelist = new Pricelist();
    }

    public AdvertisementDTO(Advertisement a){
        this(a.getId(),new CarDTO(a.getCarAd()),a.getStartDate(),a.getEndDate(),  a.getPricelist(), a.getCity(), a.isDeleted(), a.isRequest(), a.getEntrepreneurName(), a.getEntrepreneurUsername());
        if(a.getCarAd()!=null){
            this.carAd = new CarDTO(a.getCarAd());
        }
    }
    public AdvertisementDTO(Long id, CarDTO carAd, Date startDate, Date endDate, Pricelist pricelist, String city, boolean deleted, boolean request, String name, String username) {
        this.id = id;
        this.carAd = carAd;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pricelist = pricelist;
        this.city = city;
        this.deleted = deleted;
        this.request = request;
        this.name = name;
        this.username = username;
    }

    public AdvertisementDTO(rs.ac.uns.ftn.xws_tim2.Advertisement ad){
        this.carAd = new CarDTO(ad.getCarAd());
        this.startDate = ad.getStartDate().toGregorianCalendar().getTime();
        this.endDate = ad.getEndDate().toGregorianCalendar().getTime();
        this.city = ad.getCity();
        this.deleted = ad.isDeleted();
        this.request = ad.isRequest();
        this.name = ad.getEntrepreneurName();
        this.username = ad.getEntrepreneurUsername();
    }

    public boolean isRequest() {
        return request;
    }

    public void setRequest(boolean request) {
        this.request = request;
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

    public Pricelist getPricelist() {
        return pricelist;
    }

    public void setPricelist(Pricelist pricelist) {
        this.pricelist = pricelist;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
