package com.example.tim2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Date;
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

    @Column(name = "timeSent", nullable = false)
    private Date timeSent;

    @JsonBackReference(value = "endusermes_mov")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private EndUser endUser;

    @JsonBackReference(value = "entrepreneurmes_mov")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Entrepreneur entrepreneur;

    public Message() {
    }

    public Message(String text, String subject, Entrepreneur entrepreneur,EndUser endUser,Date timeSent) {
        this.text = text;
        this.subject = subject;
        this.entrepreneur = entrepreneur;
        this.endUser = endUser;
        this.timeSent = timeSent;
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

    public EndUser getEndUser() {
        return endUser;
    }

    public void setEndUser(EndUser endUser) {
        this.endUser = endUser;
    }

    public Entrepreneur getEntrepreneur() {
        return entrepreneur;
    }

    public void setEntrepreneur(Entrepreneur entrepreneur) {
        this.entrepreneur = entrepreneur;
    }

    public Date getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(Date timeSent) {
        this.timeSent = timeSent;
    }
}
