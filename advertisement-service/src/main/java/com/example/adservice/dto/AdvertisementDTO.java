package com.example.adservice.dto;

import com.example.adservice.model.Advertisement;
import com.example.adservice.model.Pricelist;

import java.time.LocalDate;

public class AdvertisementDTO {
    private Long id;
    private CarDTO carAd;
    private LocalDate startDate;
    private LocalDate endDate;
    private Pricelist pricelist;


    public AdvertisementDTO() {
    }

    public AdvertisementDTO(Advertisement a){
        this(a.getId(),new CarDTO(a.getCarAd()),a.getStartDate(),a.getEndDate(),  a.getPricelist());
        if(a.getCarAd()!=null){
            this.carAd = new CarDTO(a.getCarAd());
        }
    }
    public AdvertisementDTO(Long id, CarDTO carAd, LocalDate startDate, LocalDate endDate, Pricelist pricelist) {
        this.id = id;
        this.carAd = carAd;
        this.startDate = startDate;
        this.endDate = endDate;
        this.pricelist = pricelist;
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


    public Pricelist getPricelist() {
        return pricelist;
    }

    public void setPricelist(Pricelist pricelist) {
        this.pricelist = pricelist;
    }
}
