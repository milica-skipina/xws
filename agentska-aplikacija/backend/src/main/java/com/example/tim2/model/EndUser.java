package com.example.tim2.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.owasp.encoder.Encode;

import javax.persistence.*;

@Entity
@Table(name = "customer")
public class EndUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "surname", nullable = true)
    private String surname;

    @Column(name = "address", nullable = true)
    private String address;

    @Column(name = "city", nullable = true)
    private String city;

    @JsonManagedReference(value = "enduser_movement")
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User user;

    public EndUser() {
    }

    public EndUser(String name, String surname, String address, String city) {
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.city = city;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public EndUser escapeParameters(EndUser u) {
        u.setCity(Encode.forHtml(u.getCity()));
        u.setAddress(Encode.forHtml(u.getAddress()));
        u.setName(Encode.forHtml(u.getName()));
        u.setSurname(Encode.forHtml(u.getSurname()));
        return u;
    }
}
