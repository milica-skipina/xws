package orders.ordersmicroservice.model;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

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

    @JsonBackReference(value = "advertisementcar_mov")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Car carAd;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @JsonBackReference(value = "pricelist_mov")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Pricelist pricelist;

    @Column(name = "entrepreneur_id", nullable = false)
    private Long entrepreneurId;          // vlasnik oglasa

    @Column(name = "customer_id", nullable = false)
    private Long customerId;                // kome je rentirano
/*
    @ManyToMany(mappedBy = "basket")
    private Set<Customer> customers = new HashSet<>();

 */

    //slike

    public Advertisement() {
    }

    public Advertisement(Car car, LocalDate startDate, LocalDate endDate, Long entrepreneurId,
                         Long customerId, Pricelist pricelist) {
        this.carAd = car;
        this.startDate = startDate;
        this.endDate = endDate;
        this.entrepreneurId = entrepreneurId;
        this.customerId = customerId;
        this.pricelist = pricelist;
    }


}

