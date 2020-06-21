package com.example.tim2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int evaluation;

    @Column
    private String username;

    @JsonBackReference(value = "car_review")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Car car;

    @Column(name = "text", columnDefinition = "TEXT")
    private String text;

    // PENDING, APPROVED, REJECTED
    @Column(name = "state")
    private String state;

    @Column(name="date")
    private Date date;

    public Review() {

    }

    public Review(Integer evaluation, Car car, String comment, String username){
        this.evaluation = evaluation;
        this.car = car;
        this.text = comment;
        this.username = username;
        this.state = "PENDING";
        this.date = new Date();
    }

    public Review(int evaluation, Car car, String text, String state, Date date, String userName) {
        this.evaluation = evaluation;
        this.car = car;
        this.text = text;
        this.state = state;
        this.date = date;
        this.username = userName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
