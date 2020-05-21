package com.example.tim2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private String state;

    // predjeni kilometri
    @Column(name = "mileage", nullable = false)
    private String mileage;

    // ko salje
    @JsonBackReference(value = "entrepreneurrequest_mov")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Entrepreneur sender;

    @Column(name = "startDate", nullable = false)
    private LocalDate startDate;

    @Column(name = "endDate", nullable = false)
    private LocalDate endDate;

    public Request() {
    }

    public Request(String state, String mileage, Entrepreneur sender, LocalDate startDate, LocalDate endDate) {
        this.state = state;
        this.mileage = mileage;
        this.sender = sender;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getMileage() {
        return mileage;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public Entrepreneur getSender() {
        return sender;
    }

    public void setSender(Entrepreneur sender) {
        this.sender = sender;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Set<Car> getCars() {
        return cars;
    }

    public void setCars(Set<Car> cars) {
        this.cars = cars;
    }
}
