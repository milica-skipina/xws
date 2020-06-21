package com.example.tim2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.owasp.encoder.Encode;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "REQUEST_AND_CAR", joinColumns = {
            @JoinColumn(name = "request_id") }, inverseJoinColumns = { @JoinColumn(name = "car_id") })
    private Set<Car> cars = new HashSet<Car>();

    @Column(name = "state", nullable = false)
    private String state;           // PENDING, RESERVED , PAID , CANCELED

    // predjeni kilometri
    @Column(name = "mileage", nullable = false)
    private double mileage;

    @Column(name = "startDate", nullable = false)
    private Date startDate;

    @Column(name = "endDate", nullable = false)
    private Date endDate;

    @Column(name = "dateCreated", nullable = false)
    private Date dateCreated;

    @JsonBackReference(value = "request_mov")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User user;       // end user koji je napravio zahtev, tj customer

    @JsonBackReference(value = "entrepreneurrequest_mov")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Entrepreneur entrepreneur;

    public Request() {
    }

    public Request(Set<Car> cars, String state, double mileage, Date startDate, Date endDate, User user, Entrepreneur entrepreneur, Date dateCreated) {
        this.cars = cars;
        this.state = state;
        this.mileage = mileage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
        this.entrepreneur = entrepreneur;
        this.dateCreated = dateCreated;
    }

    public Long getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public double getMileage() {
        return mileage;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setState(String state) {
        this.state = state;
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

    public Set<Car> getCars() {
        return cars;
    }

    public void setCars(Set<Car> cars) {
        this.cars = cars;
    }

    public Request(String state, Double mileage, Date startDate, Date endDate, User user, Date dateCreated) {
        this.state = state;
        this.mileage = mileage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
        this.dateCreated = dateCreated;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Entrepreneur getSender() {
        return entrepreneur;
    }

    public void setSender(Entrepreneur sender) {
        this.entrepreneur = sender;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Entrepreneur getEntrepreneur() {
        return entrepreneur;
    }

    public void setEntrepreneur(Entrepreneur entrepreneur) {
        this.entrepreneur = entrepreneur;
    }

    public Request escapeParameters(Request r) {
        r.setState(Encode.forHtml(r.getState()));
        return r;
    }
}
