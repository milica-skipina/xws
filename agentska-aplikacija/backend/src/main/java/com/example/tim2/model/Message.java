package com.example.tim2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.util.Date;
import java.util.GregorianCalendar;
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

    @Column(nullable = true)
    private Long microId;

    public Message() {
    }

    public Message(String text, String subject, Entrepreneur entrepreneur,EndUser endUser,Date timeSent, Long microId) {
        this.text = text;
        this.subject = subject;
        this.entrepreneur = entrepreneur;
        this.endUser = endUser;
        this.timeSent = timeSent;
        this.microId = microId;
    }

    public com.example.tim2.soap.gen.Message getGenerated(boolean isEndUserSender) throws DatatypeConfigurationException {
        com.example.tim2.soap.gen.Message retValue = new com.example.tim2.soap.gen.Message();
        if(isEndUserSender){
            retValue.setReceiverUsername(getEntrepreneur().getUser().getUsername());
            retValue.setSenderUsername(getEndUser().getUser().getUsername());
        }else{
            retValue.setReceiverUsername(getEndUser().getUser().getUsername());
            retValue.setSenderUsername(getEntrepreneur().getUser().getUsername());
        }

        retValue.setSubject(getSubject());
        retValue.setText(getText());
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(getTimeSent());
        retValue.setTimeSent(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
        return retValue;
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

    public Long getMicroId() {
        return microId;
    }

    public void setMicroId(Long microId) {
        this.microId = microId;
    }
}
