package orders.ordersmicroservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

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

    @Column(name = "insurance", nullable = false)
    private String insurance;

    @Column(name = "min_price", nullable = false)
    private Double min_price;

    @Column(name = "max_price", nullable = false)
    private Double max_price;

    @Column(name = "mileage", nullable = false)
    private Double mileage;

    @Column(name = "mileage_limit", nullable = false)
    private Double mileageLimit;

    @Column(name = "kids_seats", nullable = false)
    private Integer kidsSeats;

    @Column(name = "raiting", nullable = false)
    private Integer raiting;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "following", nullable = false)
    private boolean following;

    @Column(name = "entrepreneur", nullable = false)
    private Long entrepreneurId;        // kad se oglas obrise, auto se moze ponovo iskoristiti za postavljanje novog olgasa
/*
    @ManyToMany(mappedBy = "cars")
    private Set<Request> request = new HashSet<Request>();

 */

    @JsonManagedReference(value = "advertisementcar_mov")
    @OneToMany(mappedBy = "carAd", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Advertisement> carAdvertisement = new HashSet<Advertisement>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "report_id", referencedColumnName = "id")
    private Report report;

    public Car() {
    }

    public Car(String make, String model, String fuel, String gearbox, String carClass,
               String insurance, Double min_price, Double max_price, Double mileage,
               Double mileageLimit, Integer kidsSeats, Integer raiting, String state,
               boolean following, Long entrepreneurId, Set<Advertisement> ad) {
        this.make = make;
        this.model = model;
        this.fuel = fuel;
        this.gearbox = gearbox;
        this.carClass = carClass;
        this.insurance = insurance;
        this.min_price = min_price;
        this.max_price = max_price;
        this.mileage = mileage;
        this.mileageLimit = mileageLimit;
        this.kidsSeats = kidsSeats;
        this.raiting = raiting;
        this.state = state;
        this.following = following;
        this.entrepreneurId = entrepreneurId;
        this.carAdvertisement = ad;
    }

}

