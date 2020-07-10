package com.example.demo.model;

import javax.persistence.*;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.util.Date;
import java.util.GregorianCalendar;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String text;

    @Column
    private String subject;

    @Column
    private Date timeSent;

    @Column
    private String senderUsername;

    @Column
    private String receiverUsername;

    public Message() {
    }

    public rs.ac.uns.ftn.xws_tim2.Message getGenerated() throws DatatypeConfigurationException {
        rs.ac.uns.ftn.xws_tim2.Message retValue = new rs.ac.uns.ftn.xws_tim2.Message();
        retValue.setId(getId());
        retValue.setReceiverUsername(getReceiverUsername());
        retValue.setSenderUsername(getSenderUsername());
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

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(Date timeSent) {
        this.timeSent = timeSent;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
