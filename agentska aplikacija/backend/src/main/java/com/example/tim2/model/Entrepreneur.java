package com.example.tim2.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Entrepreneur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name" )
    private String name;

    @Column(name = "surname" )
    private String surname;

    @Column(name = "company_name" )
    private String companyName;

    // Business Identification Number
    @Column(name = "bin", nullable = false, unique = true)
    private String bin;

    @Column(name = "address", nullable = false)
    private String address;

    @JsonManagedReference(value = "entrepreneur_mov")
    @OneToMany(mappedBy = "entrepreneur", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Advertisement> advertisements = new HashSet<Advertisement>();

    @JsonManagedReference(value = "entrepreneuroutcom_mov")
    @OneToMany(mappedBy = "fromCom", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Comment> commentOut = new HashSet<Comment>();

    @JsonManagedReference(value = "entrepreneurincom_mov")
    @OneToMany(mappedBy = "toCom", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Comment> commentIn= new HashSet<Comment>();

    @JsonManagedReference(value = "entrepreneuroutmes_mov")
    @OneToMany(mappedBy = "fromMes", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Message> mesOut = new HashSet<Message>();

    @JsonManagedReference(value = "entrepreneurinmes_mov")
    @OneToMany(mappedBy = "toMes", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Message> mesIn = new HashSet<Message>();

    @JsonManagedReference(value = "agent_movement")
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User user;

    public Entrepreneur() {
    }

    public Entrepreneur(String name, String surname, String companyName, String bin, String address) {
        this.name = name;
        this.surname = surname;
        this.companyName = companyName;
        this.bin = bin;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBin() {
        return bin;
    }

    public void setBin(String bin) {
        this.bin = bin;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Advertisement> getAdvertisements() {
        return advertisements;
    }

    public void setAdvertisements(Set<Advertisement> advertisements) {
        this.advertisements = advertisements;
    }

    public Set<Comment> getCommentOut() { return commentOut; }

    public void setCommentOut(Set<Comment> commentOut) {
        this.commentOut = commentOut;
    }

    public Set<Comment> getCommentIn() {
        return commentIn;
    }

    public void setCommentIn(Set<Comment> commentIn) {
        this.commentIn = commentIn;
    }

    public Set<Message> getMesOut() {
        return mesOut;
    }

    public void setMesOut(Set<Message> mesOut) {
        this.mesOut = mesOut;
    }

    public Set<Message> getMesIn() {
        return mesIn;
    }

    public void setMesIn(Set<Message> mesIn) {
        this.mesIn = mesIn;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
