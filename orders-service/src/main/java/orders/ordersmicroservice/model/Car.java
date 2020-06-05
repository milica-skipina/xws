package orders.ordersmicroservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import orders.ordersmicroservice.dto.CarOrderDTO;

import javax.persistence.*;
import java.util.*;

@Entity
@Getter
@Setter
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Tesla
    @Column(name = "make", nullable = false)
    private String make;

    // Tesla S (nesto)
    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "fuel", nullable = false)
    private String fuel;

    // mjenjac
    @Column(name = "gearbox", nullable = false)
    private String gearbox;

    @Column(name = "car_class", nullable = false)
    private String carClass;

    @Column(name = "insurance", nullable = true)
    private boolean insurance;

    /*@Column(name = "min_price", nullable = true)
    private Double min_price;

    @Column(name = "max_price", nullable = true)
    private Double max_price;*/

    @Column(name = "mileage", nullable = false)
    private Double mileage;

    @Column(name = "mileage_limit", nullable = false)
    private Double mileageLimit;

    @Column(name = "raiting", nullable = true)
    private double raiting;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "entrepreneurUsername", nullable = false)
    private String entrepreneurUsername;        // kad se oglas obrise, auto se moze ponovo iskoristiti za postavljanje novog olgasa


    @ManyToMany(mappedBy = "cars")
    private Set<Request> request = new HashSet<Request>();

    @JsonManagedReference(value = "advertisementcar_mov")
    @OneToMany(mappedBy = "carAd", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Advertisement> carAdvertisement = new HashSet<Advertisement>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "report_id", referencedColumnName = "id")
    private Report report;

    public Car() {
    }

    public Car(CarOrderDTO c) {
        this.make = c.getMake();
        this.model = c.getModel();
        this.fuel = c.getFuel();
        this.carClass = c.getCarClass();
        this.insurance = c.getInsurance();
        this.mileage = c.getMileage();
        this.mileageLimit = c.getMileageLimit();
        this.raiting = c.getRaiting();
        this.state = c.getState();
        this.gearbox = c.getGearbox();
        this.entrepreneurUsername = c.getEntrepreneurUsername();
    }

    public Car(String make, String model, String fuel, String gearbox, String carClass,
               boolean insurance, Double mileage,
               Double mileageLimit, double raiting, String state,
               String entrepreneurUsername, Set<Advertisement> ad) {
        this.make = make;
        this.model = model;
        this.fuel = fuel;
        this.gearbox = gearbox;
        this.carClass = carClass;
        this.insurance = insurance;
        this.mileage = mileage;
        this.mileageLimit = mileageLimit;
        this.entrepreneurUsername = entrepreneurUsername;
        this.raiting = raiting;
        this.state = state;
        this.carAdvertisement = ad;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setGearbox(String gearbox) {
        this.gearbox = gearbox;
    }

    public String getCarClass() {
        return carClass;
    }

    public void setCarClass(String carClass) {
        this.carClass = carClass;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }
}

