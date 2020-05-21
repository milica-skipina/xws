package orders.ordersmicroservice.model;

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

    @Column(name = "discountDC", nullable = false)
    private Double discountDC;

    @Column(name = "discount20", nullable = false)
    private Double discount20;

    @Column(name = "discount30", nullable = false)
    private Double discount30;

    @JsonManagedReference(value = "pricelist_mov")
    @OneToMany(mappedBy = "pricelist", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Advertisement> pricelistAd = new HashSet<Advertisement>();

    public Pricelist() {
    }

    public Pricelist(Double priceDay, Double discountDC, Double discount20, Double discount30, Set<Advertisement> pricelistAd) {
        this.priceDay = priceDay;
        this.discountDC = discountDC;
        this.discount20 = discount20;
        this.discount30 = discount30;
        this.pricelistAd = pricelistAd;
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

    public Set<Advertisement> getPricelistAd() {
        return pricelistAd;
    }

    public void setPricelistAd(Set<Advertisement> pricelistAd) {
        this.pricelistAd = pricelistAd;
    }
}

