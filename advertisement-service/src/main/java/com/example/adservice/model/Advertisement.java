package com.example.adservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference(value = "advertisementcar_mov")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Car carAd;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "entrepreneur_name")
    private String entrepreneurName;        //company name ili name customera ako on dodaje

    @Column(name = "entrepreneur_username")
    private String entrepreneurUsername;

    @Column(name = "request")
    private boolean request;

    @JsonBackReference(value = "pricelist_mov")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    private Pricelist pricelist;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Column(name = "state", nullable = false)
    private String state;

    public Advertisement(rs.ac.uns.ftn.xws_tim2.Advertisement advertisement) {
        //this.carAd = new P;
        this.startDate = advertisement.getStartDate().toGregorianCalendar().getTime();
        this.endDate = advertisement.getEndDate().toGregorianCalendar().getTime();
        this.entrepreneurName = advertisement.getEntrepreneurName();
        this.city = advertisement.getCity();
        this.deleted = advertisement.isDeleted();
        this.state = "PENDING";
    }
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
//slike

    public Advertisement() {
        this.state = "PENDING";
    }

    public Advertisement(Car car, Date startDate, Date endDate, String entrepreneurName, Pricelist pricelist, String city, boolean request) {
        this.carAd = car;
        this.startDate = startDate;
        this.endDate = endDate;
        this.entrepreneurName = entrepreneurName;
        this.pricelist = pricelist;
        this.city = city;
        this.request = request;
        this.state = "PENDING";
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Car getCarAd() {
        return carAd;
    }

    public void setCaAdr(Car car) {
        this.carAd = car;
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

    public void setCarAd(Car carAd) {
        this.carAd = carAd;
    }


    public Pricelist getPricelist() {
        return pricelist;
    }

    public void setPricelist(Pricelist pricelist) {
        this.pricelist = pricelist;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public boolean isRequest() {
        return request;
    }

    public void setRequest(boolean request) {
        this.request = request;
    }

    public String getEntrepreneurName() {
        return entrepreneurName;
    }

    public void setEntrepreneurName(String entrepreneurName) {
        this.entrepreneurName = entrepreneurName;
    }

    public String getEntrepreneurUsername() {
        return entrepreneurUsername;
    }

    public void setEntrepreneurUsername(String entrepreneurUsername) {
        this.entrepreneurUsername = entrepreneurUsername;
    }
}
