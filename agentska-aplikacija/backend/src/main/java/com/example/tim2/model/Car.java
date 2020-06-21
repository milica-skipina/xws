package com.example.tim2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.security.core.parameters.P;

import javax.persistence.*;
import java.util.*;

@Entity
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Tesla
    @JsonBackReference(value = "car_make_mov")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Codebook make;

    // Tesla S (nesto)
    @JsonBackReference(value = "car_model_mov")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Codebook model;

    @JsonBackReference(value = "car_fuel_mov")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Codebook fuel;

    // mjenjac
    @JsonBackReference(value = "car_gearbox_mov")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Codebook gearbox;

    @JsonBackReference(value = "car_class_mov")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Codebook carClass;

    @Column(name = "insurance", nullable = false)
    private Boolean insurance;

    @Column(name = "mileage", nullable = false)
    private Double mileage;

    @Column(name = "mileage_limit", nullable = false)
    private Double mileageLimit;

    @Column(name = "kids_seats", nullable = false)
    private Integer kidsSeats;

    @Column(name = "raiting", nullable = false)
    private Double raiting;

    @Column(name = "state", nullable = false)
    private String state;

    // za tracking device
    @Column(name = "following", nullable = true)
    private boolean following;

    // firma ili pojedinac
    @JsonBackReference(value = "entrepreneur_mov")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Entrepreneur entrepreneur;

    @ManyToMany(mappedBy = "cars")
    private Set<Request> request = new HashSet<Request>();

    @JsonManagedReference(value = "advertisementcar_mov")
    @OneToMany(mappedBy = "carAd", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Advertisement> carAdvertisement = new HashSet<Advertisement>();

    @JsonManagedReference(value = "image_mov")
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Image>images = new HashSet<Image>();

    @JsonManagedReference(value = "carreport_mov")
    @OneToMany(mappedBy = "reportCar", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Report> reports = new HashSet<Report>();

    @JsonManagedReference(value = "car_review")
    @OneToMany(mappedBy = "car", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Review> reviews = new HashSet<Review>();

    @Column(name = "trackingToken", nullable = true)
    private String trackingToken;

    @Column
    private Long microId;

    public Car() {
    }

    public com.example.tim2.soap.gen.Car getGenerated(){
        com.example.tim2.soap.gen.Car retValue = new com.example.tim2.soap.gen.Car();
        retValue.setCarClass(getCarClass().getName());
        retValue.setMake(getMake().getName());
        retValue.setModel(getModel().getName());
        retValue.setFollowing(isFollowing());
        retValue.setEntrepreneurUsername(getEntrepreneur().getUser().getUsername());
        retValue.setFuel(getFuel().getName());
        retValue.setGearbox(getGearbox().getName());
        retValue.setKidsSeats(getKidsSeats());
        retValue.setRaiting(getRaiting());
        retValue.setInsurance(insurance);
        retValue.setMileage(getMileage());
        retValue.setState(getState());
        retValue.setMileageLimit(getMileageLimit());
        retValue.setEntrepreneurUsername("prodavac");
        for(Image i : getImages()){
            retValue.getImages().add(i.getGenerated());
        }
        return retValue;
    }

    public Car(Codebook make, Codebook model, Codebook fuel, Codebook gearbox, Codebook carClass, Boolean insurance,
               Double mileage, Double mileageLimit, Integer kidsSeats, Double raiting, String state, boolean following,
               Entrepreneur entrepreneur, Set<Request> request, Set<Advertisement> carAdvertisement, Set<Image> images,
                Set<Review> reviews) {
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
        this.entrepreneur = entrepreneur;
        this.request = request;
        this.carAdvertisement = carAdvertisement;
        this.images = images;
        this.reviews = reviews;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Codebook getMake() {
        return make;
    }

    public void setMake(Codebook make) {
        this.make = make;
    }

    public Codebook getModel() {
        return model;
    }

    public void setModel(Codebook model) {
        this.model = model;
    }

    public Codebook getFuel() {
        return fuel;
    }

    public void setFuel(Codebook fuel) {
        this.fuel = fuel;
    }

    public Codebook getGearbox() {
        return gearbox;
    }

    public void setGearbox(Codebook gearbox) {
        this.gearbox = gearbox;
    }

    public Codebook getCarClass() {
        return carClass;
    }

    public void setCarClass(Codebook carClass) {
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

    public Entrepreneur getEntrepreneur() {
        return entrepreneur;
    }

    public void setEntrepreneur(Entrepreneur entrepreneur) {
        this.entrepreneur = entrepreneur;
    }

    public Set<Request> getRequest() {
        return request;
    }

    public void setRequest(Set<Request> request) {
        this.request = request;
    }

    public Set<Advertisement> getCarAdvertisement() {
        return carAdvertisement;
    }

    public void setCarAdvertisement(Set<Advertisement> carAdvertisement) {
        this.carAdvertisement = carAdvertisement;
    }

    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    public Set<Report> getReports() {
        return reports;
    }

    public void setReports(Set<Report> reports) {
        this.reports = reports;
    }

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
    }

    public String getTrackingToken() {
        return trackingToken;
    }

    public void setTrackingToken(String trackingToken) {
        this.trackingToken = trackingToken;
    }

    public Long getMicroId() {
        return microId;
    }

    public void setMicroId(Long microId) {
        this.microId = microId;
    }
}
