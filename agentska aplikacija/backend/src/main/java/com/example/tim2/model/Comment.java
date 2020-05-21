package com.example.tim2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;

@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "state", nullable = false)
    private String state;

    @JsonBackReference(value = "entrepreneuroutcom_mov")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Entrepreneur fromCom;

    @JsonBackReference(value = "entrepreneurincom_mov")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Entrepreneur toCom;

    public Comment() {
    }

    public Comment(String text, String state, Entrepreneur from, Entrepreneur to) {
        this.text = text;
        this.state = state;
        this.fromCom = from;
        this.toCom = to;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getState() {
        return state;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Entrepreneur getFromCom() {
        return fromCom;
    }

    public void setFromCom(Entrepreneur from) {
        this.fromCom = from;
    }

    public Entrepreneur getToCom() {
        return toCom;
    }

    public void setToCom(Entrepreneur to) {
        this.toCom = to;
    }
}
