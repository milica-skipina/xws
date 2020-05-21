package orders.ordersmicroservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
/*
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "REQUEST_AND_ADVERTISEMENT", joinColumns = {
            @JoinColumn(name = "request_id") }, inverseJoinColumns = { @JoinColumn(name = "adv_id") })
    private Set<Advertisement> ads = new HashSet<Advertisement>();
*/
    @Column(name = "state", nullable = false)
    private String state;

    // predjeni kilometri
    @Column(name = "mileage", nullable = false)
    private String mileage;

    // ko salje
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "startDate", nullable = false)
    private LocalDate startDate;

    @Column(name = "endDate", nullable = false)
    private LocalDate endDate;

    @Column(name = "bundle", nullable = false)
    private boolean bundle;     // ako je vise kola, da li se svi moraju odobriti

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public Request() {
    }

    public Request(String state, String mileage, Long customerId, LocalDate startDate, LocalDate endDate) {
        this.state = state;
        this.mileage = mileage;
        this.customerId = customerId;
        this.startDate = startDate;
        this.endDate = endDate;
    }


}

