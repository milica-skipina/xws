package com.example.adservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Tesla
    @JsonBackReference(value = "car_make_mov")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Codebook make;

    // Tesla S (nesto)
    @JsonBackReference(value = "car_model_mov")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Codebook model;

    @JsonBackReference(value = "car_fuel_mov")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Codebook fuel;

    // mjenjac
    @JsonBackReference(value = "car_gearbox_mov")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Codebook gearbox;

    @JsonBackReference(value = "car_class_mov")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Codebook carClass;

    @JsonManagedReference(value = "car_review")
    @OneToMany(mappedBy = "car", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Set<Review> reviews = new HashSet<Review>();

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

    @Column(name = "entrepUsername", nullable = false)
    private String entrepreneurUsername;

    /*@Column(name = "entrepName", nullable = false)
    private String entrepreneurName;
*/
    // ???
    @Column(name = "following", nullable = false)
    private boolean following;

    @JsonManagedReference(value = "advertisementcar_mov")
    @OneToMany(mappedBy = "carAd", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Set<Advertisement> carAdvertisement = new HashSet<Advertisement>();

    @JsonManagedReference(value = "image_mov")
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Set<Image>images = new HashSet<Image>();

    @Column(name = "trackingToken", nullable = true)
    private String trackingToken;

    public Car() {
    }

    public Car(rs.ac.uns.ftn.xws_tim2.Car c) {
        this.insurance = c.isInsurance();
        this.mileage = c.getMileage();
        this.mileageLimit = c.getMileageLimit();
        this.kidsSeats = c.getKidsSeats();
        this.raiting = c.getRaiting();
        this.state = c.getState();
        this.following = c.isFollowing();
        for(rs.ac.uns.ftn.xws_tim2.Image i:c.getImages()){
            this.getImages().add(new Image(i, c.getId()));
        }
       // this.carAdvertisement = c.getCarAdvertisement();
       // this.images = c.getImages();
        this.entrepreneurUsername = c.getEntrepreneurUsername();
     //   this.reviews = c.getReviews();
    }

    public Car(Long id, Codebook make, Codebook model, Codebook fuel, Codebook gearbox,
               Codebook carClass, Boolean insurance, Double mileage, Double mileageLimit,
               Integer kidsSeats, Double raiting, String state, boolean following,
               Set<Advertisement> carAdvertisement, Set<Image> images, String id1, Set<Review> reviews) {
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
        this.carAdvertisement = carAdvertisement;
        this.images = images;
        this.entrepreneurUsername = id1;
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

    public Set<Review> getReviews() {
        return reviews;
    }

    public void setReviews(Set<Review> reviews) {
        this.reviews = reviews;
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

    public String getEntrepreneurUsername() {
        return entrepreneurUsername;
    }

    public void setEntrepreneurUsername(String entrepreneurUsername) {
        this.entrepreneurUsername = entrepreneurUsername;
    }

    /*public String getEntrepreneurName() {
        return entrepreneurName;
    }

    public void setEntrepreneurName(String entrepreneurName) {
        this.entrepreneurName = entrepreneurName;
    }*/

    public String getTrackingToken() {
        return trackingToken;
    }

    public void setTrackingToken(String trackingToken) {
        this.trackingToken = trackingToken;
    }
}
