package com.example.tim2.model;


import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text")
    private String text;

    @Column(name = "mileage")
    private Double mileage;

    @JsonBackReference(value = "carreport_mov")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Car reportCar;

    public Report() {
    }

    public Report(String text, Car reportCar, Double mileage) {
        this.text = text;
        this.reportCar = reportCar;
        this.mileage = mileage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Car getReportCar() {
        return reportCar;
    }

    public void setReportCar(Car reportCar) {
        this.reportCar = reportCar;
    }

    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }
}
