package orders.ordersmicroservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", nullable = true)
    private String additional_text;

    @Column(name = "predjeni_km", nullable = true)
    private double predjeniKilometri;

    @OneToOne(mappedBy = "report")
    private Car car;

    public Report() {
    }
}
