package orders.ordersmicroservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @JsonBackReference(value = "car_report_mov")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Car car;

    public Report() {
    }

    public Report(String additional_text, double predjeniKilometri, Car car) {
        this.additional_text = additional_text;
        this.predjeniKilometri = predjeniKilometri;
        this.car = car;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAdditional_text() {
        return additional_text;
    }

    public void setAdditional_text(String additional_text) {
        this.additional_text = additional_text;
    }

    public double getPredjeniKilometri() {
        return predjeniKilometri;
    }

    public void setPredjeniKilometri(double predjeniKilometri) {
        this.predjeniKilometri = predjeniKilometri;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}
