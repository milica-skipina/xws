package com.example.tim2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", nullable = false)
    private String text;

    @Column(name = "Subject", nullable = false)
    private String subject;

    @JsonBackReference(value = "entrepreneuroutmes_mov")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Entrepreneur fromMes;

    @JsonBackReference(value = "entrepreneurinmes_mov")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Entrepreneur toMes;

    public Message() {
    }

    public Message(String text, String subject, Entrepreneur from, Entrepreneur to) {
        this.text = text;
        this.subject = subject;
        this.fromMes = from;
        this.toMes = to;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getSubject() {
        return subject;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Entrepreneur getFromMes() {
        return fromMes;
    }

    public void setFromMes(Entrepreneur from) {
        this.fromMes = from;
    }

    public Entrepreneur getToMes() {
        return toMes;
    }

    public void setToMes(Entrepreneur toMes) {
        this.toMes = toMes;
    }
}
