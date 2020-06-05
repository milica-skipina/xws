package orders.ordersmicroservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

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

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "REQUEST_AND_CAR", joinColumns = {
            @JoinColumn(name = "request_id") }, inverseJoinColumns = { @JoinColumn(name = "car_id") })
    private Set<Car> cars = new HashSet<Car>();

    @Column(name = "state", nullable = false)
    private String state;

    // predjeni kilometri
    @Column(name = "mileage", nullable = false)
    private double mileage;

    // ko salje
    @Column(name = "customer_username", nullable = false)
    private String customerUsername;            // ovo iz jwta

    // ko odobrava
    @Column(name = "agent_username", nullable = false)
    private String agentUsername;         // ovo iz dobavljenog oglasa

    // ko salje
    @Column(name = "customer_name", nullable = true)
    private String customerName;            // ovo iz jwta

    // ko odobrava
    @Column(name = "agent_name", nullable = true)
    private String agentNamee;

    @Column(name = "startDate", nullable = false)
    private Date startDate;

    @Column(name = "endDate", nullable = false)
    private Date endDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    public Request() {
    }

    public Request(String state, double mileage, Date startDate, Date endDate, String customerUsername,
                   String agentUsername) {
        this.state = state;
        this.mileage = mileage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.customerUsername = customerUsername;
        this.agentUsername = agentUsername;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Car> getCars() {
        return cars;
    }

    public void setCars(Set<Car> cars) {
        this.cars = cars;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getMileage() {
        return mileage;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
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

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }
}

