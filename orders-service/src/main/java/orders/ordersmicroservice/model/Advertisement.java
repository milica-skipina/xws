package orders.ordersmicroservice.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import orders.ordersmicroservice.dto.AdvertisementDTO;
import orders.ordersmicroservice.dto.CarOrderDTO;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Setter
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private Long adId;

    @JsonBackReference(value = "advertisementcar_mov")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Car carAd;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "make", nullable = false)
    private String make;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "rating", nullable = false)
    private double rating;

    @JsonBackReference(value = "pricelist_mov")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Pricelist pricelist;

    @Column(name = "entrepreneur_name")
    private String entrepreneurName;        //company name ili name customera ako on dodaje


    //slike

    public Advertisement() {
    }

    public Advertisement(Long adId, Car carAd, Date startDate, Date endDate, String city, String make, String model,
                         double rating, Pricelist pricelist, String entrepreneurName) {
        this.adId = adId;
        this.carAd = carAd;
        this.startDate = startDate;
        this.endDate = endDate;
        this.city = city;
        this.make = make;
        this.model = model;
        this.rating = rating;
        this.pricelist = pricelist;
        this.entrepreneurName = entrepreneurName;
    }

    public Advertisement(AdvertisementDTO a){
        this.adId = a.getId();
        this.carAd = new Car(a.getCarAd());
        this.startDate = a.getStartDate();
        this.endDate = a.getEndDate();
        this.entrepreneurName = a.getEntrepreneur();
        this.pricelist = new Pricelist(a.getPricelist());
        this.city = a.getCity();
        this.make = a.getMake();
        this.model = a.getModel();
        this.rating = a.getRating();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Car getCarAd() {
        return carAd;
    }

    public void setCarAd(Car carAd) {
        this.carAd = carAd;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Pricelist getPricelist() {
        return pricelist;
    }

    public void setPricelist(Pricelist pricelist) {
        this.pricelist = pricelist;
    }

    public Long getAdId() {
        return adId;
    }

    public void setAdId(Long adId) {
        this.adId = adId;
    }
}

