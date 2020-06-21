package com.example.tim2.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Pricelist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price_day", nullable = false)
    private Double priceDay;

    @Column(name = "exceedMileage", nullable = false)
    private Double exceedMileage;

    @Column(name = "collisionDW", nullable = false)
    private Double collisionDW;

    @Column(name = "discount20", nullable = false)
    private Double discount20;

    @Column(name = "discount30", nullable = false)
    private Double discount30;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @JsonManagedReference(value = "pricelist_mov")
    @OneToMany(mappedBy = "pricelist", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Advertisement> pricelistAd = new HashSet<Advertisement>();

    @Column
    private String username;

    @Column
    private Long microId;

    public Pricelist() {
    }

    public Pricelist(Double priceDay, Double collisionDW, Double discount20, Double discount30, Set<Advertisement> pricelistAd, Double exceedMileage, boolean deleted, Long micro, String username) {
        this.priceDay = priceDay;
        this.collisionDW = collisionDW;
        this.discount20 = discount20;
        this.discount30 = discount30;
        this.pricelistAd = pricelistAd;
        this.exceedMileage = exceedMileage;
        this.deleted = deleted;
        this.microId = micro;
        this.username = username;
    }

    public com.example.tim2.soap.gen.Pricelist getGenerated(){
        com.example.tim2.soap.gen.Pricelist ret = new com.example.tim2.soap.gen.Pricelist();
        ret.setPriceDay(getPriceDay());
        ret.setExceedMileage(getExceedMileage());
        ret.setDiscount30(getDiscount20());
        ret.setDiscount20(getDiscount20());
        ret.setCollisionDW(getCollisionDW());
        ret.setId(getMicroId());
        return ret;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPriceDay() {
        return priceDay;
    }

    public void setPriceDay(Double priceDay) {
        this.priceDay = priceDay;
    }

    public Double getCollisionDW() {
        return collisionDW;
    }

    public void setCollisionDW(Double collisionDW) {
        this.collisionDW = collisionDW;
    }

    public Double getDiscount20() {
        return discount20;
    }

    public void setDiscount20(Double discount20) {
        this.discount20 = discount20;
    }

    public Double getDiscount30() {
        return discount30;
    }

    public void setDiscount30(Double discount30) {
        this.discount30 = discount30;
    }

    public Set<Advertisement> getPricelistAd() {
        return pricelistAd;
    }

    public void setPricelistAd(Set<Advertisement> pricelistAd) {
        this.pricelistAd = pricelistAd;
    }

    public Double getExceedMileage() {
        return exceedMileage;
    }

    public void setExceedMileage(Double exceedMileage) {
        this.exceedMileage = exceedMileage;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Long getMicroId() {
        return microId;
    }

    public void setMicroId(Long microId) {
        this.microId = microId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
