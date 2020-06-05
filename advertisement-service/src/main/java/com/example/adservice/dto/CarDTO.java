package com.example.adservice.dto;

import com.example.adservice.model.Car;
import com.example.adservice.model.Codebook;
import com.example.adservice.model.Image;

import java.util.HashSet;
import java.util.Set;

public class CarDTO {

    private Long id;
    // Tesla
    private String make;
    // Tesla S (nesto)
    private String model;
    private String fuel;
    // mjenjac
    private String gearbox;
    private String carClass;
    private Boolean insurance;
    private Double mileage;
    private Double mileageLimit;
    private Integer kidsSeats;
    private Double raiting;
    private String state;
    private boolean following;
    private Set<Image> images = new HashSet<Image>();
    private String entrepreneurId;      // ovo je username, ali nisam zbog fronta menjala
    private Double price;

    public CarDTO() {
    }

    public CarDTO(Car c) {
        this(c.getId(), c.getMake().getName(), c.getModel().getName(), c.getFuel().getName(), c.getGearbox().getName(),
                c.getCarClass().getName(), c.getInsurance(),  c.getMileage(),
                c.getMileageLimit(), c.getKidsSeats(), c.getRaiting(), c.getState(), c.isFollowing(), c.getImages(),
                c.getEntrepreneurUsername(), 0.0);
    }

    public CarDTO(Long id, String make, String model, String fuel, String gearbox, String carClass, Boolean insurance,
                   Double mileage, Double mileageLimit, Integer kidsSeats,
                  Double raiting, String state, boolean following, Set<Image> images, String entrepreneurId, Double price) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.fuel = fuel;
        this.gearbox = gearbox;
        this.carClass = carClass;
        this.insurance = insurance;
        this.mileage = mileage;
        this.mileageLimit = mileageLimit;
        this.kidsSeats = kidsSeats;
        this.raiting = raiting;
        this.state = state;
        this.following = following;
        this.images = images;
        this.entrepreneurId = entrepreneurId;
        this.price = price;
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

    public String getFuel() {
        return fuel;
    }

    public void setFuel(String fuel) {
        this.fuel = fuel;
    }

    public String getGearbox() {
        return gearbox;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setGearbox(String gearbox) {
        this.gearbox = gearbox;
    }

    public String getCarClass() {
        return carClass;
    }

    public void setCarClass(String carClass) {
        this.carClass = carClass;
    }

    public Boolean getInsurance() {
        return insurance;
    }

    public void setInsurance(Boolean insurance) {
        this.insurance = insurance;
    }

    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }

    public Double getMileageLimit() {
        return mileageLimit;
    }

    public void setMileageLimit(Double mileageLimit) {
        this.mileageLimit = mileageLimit;
    }

    public Integer getKidsSeats() {
        return kidsSeats;
    }

    public void setKidsSeats(Integer kidsSeats) {
        this.kidsSeats = kidsSeats;
    }

    public Double getRaiting() {
        return raiting;
    }

    public void setRaiting(Double raiting) {
        this.raiting = raiting;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public boolean isFollowing() {
        return following;
    }

    public void setFollowing(boolean following) {
        this.following = following;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    public String getEntrepreneurId() {
        return entrepreneurId;
    }

    public void setEntrepreneurId(String entrepreneurId) {
        this.entrepreneurId = entrepreneurId;
    }
}
