package com.example.tim2.dto;

import com.example.tim2.model.WishlistItem;

import java.util.Date;
import java.util.Objects;

public class BasketDTO {
    private Long wishlistId;
    private Long advertisementId;
    private Date startDate;
    private Date endDate;
    private String city;
    private String make;
    private String model;
    private double price;
    private double rating;
    private String entrepreneur;
    private boolean isBundle;

    public BasketDTO() {
    }

    public BasketDTO(WishlistItem ad) {
        this.startDate = ad.getStartDate();
        this.endDate = ad.getEndDate();
        this.city = ad.getAdvertisement().getCity();
        this.make = ad.getAdvertisement().getCarAd().getMake().getName();      // naziv proizvodjaca ?
        this.model = ad.getAdvertisement().getCarAd().getModel().getName();
        this.rating = ad.getAdvertisement().getCarAd().getRaiting();
        this.entrepreneur = ad.getAdvertisement().getEntrepreneur().getCompanyName();
        this.advertisementId = ad.getAdvertisement().getId();
        this.wishlistId = ad.getId();
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

    public boolean isBundle() {
        return isBundle;
    }

    public void setBundle(boolean bundle) {
        isBundle = bundle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BasketDTO)) return false;
        BasketDTO basketDTO = (BasketDTO) o;
        return getAdvertisementId().equals(basketDTO.getAdvertisementId()) &&
                getStartDate().compareTo(basketDTO.getStartDate()) == 0 && getEndDate().compareTo(basketDTO.getEndDate()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAdvertisementId());
    }

    public Long getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(Long wishlistId) {
        this.wishlistId = wishlistId;
    }
}
