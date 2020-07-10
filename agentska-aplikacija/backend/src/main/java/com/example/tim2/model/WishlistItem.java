package com.example.tim2.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter @Setter
public class WishlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "startDate", nullable = false)
    private Date startDate;

    @Column(name = "endDate", nullable = false)
    private Date endDate;

    @JsonManagedReference(value = "wishlist_movement")
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Advertisement advertisement;        // jedan oglas je jedna stavka u korpi

    @Column(name = "price", nullable = true)
    private double price;       // price for selected days

    @Column(name = "turnedIntoRequest", nullable = true)
    private boolean turnedIntoRequest;

    @Column(name = "customerUsername", nullable = false)
    private String customerUsername;

    public WishlistItem(Date startDate, Date endDate, Advertisement advertisement, double price, boolean turnedIntoRequest,
                        String customerUsername) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.advertisement = advertisement;
        this.price = price;
        this.turnedIntoRequest = turnedIntoRequest;
        this.customerUsername = customerUsername;
    }

    public WishlistItem() {
    }
}
