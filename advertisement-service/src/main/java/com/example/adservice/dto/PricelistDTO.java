package com.example.adservice.dto;

import com.example.adservice.model.Pricelist;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "pricelist")
@XmlType(namespace = "xws_tim2", propOrder = { "id", "priceDay", "collisionDW", "discount20", "discount30", "exceedMileage", "deleted" })
public class PricelistDTO {

    @XmlElement(required = true)
    private Long id;
    @XmlElement(name = "priceDay", required = true)
    private Double priceDay;
    @XmlElement(name = "collisionDW", required = true)
    private Double collisionDW;
    @XmlElement(name = "discount20", required = true)
    private Double discount20;
    @XmlElement(name = "discount30", required = true)
    private Double discount30;
    @XmlElement(name = "exceedMileage", required = true)
    private Double exceedMileage;
    @XmlElement(name = "deleted", required = true)
    private boolean deleted;

    private String username;

    public PricelistDTO(){

    }

    public PricelistDTO(Long id, Double priceDay, Double collisionDW, Double discount20, Double discount30, Double exceedMileage, boolean deleted, String username){
        this.id = id;
        this.priceDay = priceDay;
        this.collisionDW = collisionDW;
        this.discount20 = discount20;
        this.discount30 = discount30;
        this.exceedMileage = exceedMileage;
        this.deleted = deleted;
        this.username = username;
    }

    public PricelistDTO(Pricelist p){
        this(p.getId(), p.getPriceDay(), p.getCollisionDW(), p.getDiscount20(), p.getDiscount30(), p.getExceedMileage(),p.isDeleted(), p.getUsername());
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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

    public Double getExceedMileage() {
        return exceedMileage;
    }

    public void setExceedMileage(Double exceedMileage) {
        this.exceedMileage = exceedMileage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
