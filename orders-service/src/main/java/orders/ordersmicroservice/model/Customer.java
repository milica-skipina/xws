package orders.ordersmicroservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
/*
    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "customer_basket",
            joinColumns = { @JoinColumn(name = "customer_id") },
            inverseJoinColumns = { @JoinColumn(name = "advertisement_id") }
    )
    private Set<Advertisement> basket = new HashSet<Advertisement>();       // od ovih oglasa iz korpe, moze da se formira zahtev

 */
}
