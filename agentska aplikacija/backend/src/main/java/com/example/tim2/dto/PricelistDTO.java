package com.example.tim2.dto;

import com.example.tim2.model.Advertisement;
import com.example.tim2.model.Pricelist;

import java.util.HashSet;
import java.util.Set;

public class PricelistDTO {

    private Long id;
    private Double priceDay;
    private Double discountDC;
    private Double discount20;
    private Double discount30;

    public PricelistDTO() {

    }

    public PricelistDTO(Pricelist p) {
        this(p.getId(), p.getPriceDay(), p.getDiscountDC(), p.getDiscount20(), p.getDiscount30());
    }

    public PricelistDTO(Long id, Double priceDay, Double discountDC, Double discount20, Double discount30) {
        this.id = id;
        this.priceDay = priceDay;
        this.discountDC = discountDC;
        this.discount20 = discount20;
        this.discount30 = discount30;
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

    public Double getDiscountDC() {
        return discountDC;
    }

    public void setDiscountDC(Double discountDC) {
        this.discountDC = discountDC;
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

}
