package com.example.tim2.dto;


import com.example.tim2.model.Message;


import java.util.Date;

public class MessageDTO {

    private Long id;
    private String text;
    private String subject;
    private Date timeSent;
    private String endUserName;
    private String endUserLastName;
    private String entrepreneurName;
    private String entrepreneurLastName;
    private String endUserUsername;
    private String entrepreneurUsername;

    public MessageDTO(Message message) {
        this.id = message.getId();
        this.text = message.getText();
        this.subject = message.getSubject();
        this.timeSent = message.getTimeSent();
        this.endUserUsername = message.getEndUser().getUser().getUsername();
        this.endUserName = message.getEndUser().getName();
        this.endUserLastName = message.getEndUser().getSurname();
        this.entrepreneurUsername = message.getEntrepreneur().getUser().getUsername();
        this.entrepreneurName = message.getEntrepreneur().getName();
        this.entrepreneurLastName = message.getEntrepreneur().getSurname();
    }

    public MessageDTO() {
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(Date timeSent) {
        this.timeSent = timeSent;
    }

    public String getEndUserName() {
        return endUserName;
    }

    public void setEndUserName(String endUserName) {
        this.endUserName = endUserName;
    }

    public String getEndUserLastName() {
        return endUserLastName;
    }

    public void setEndUserLastName(String endUserLastName) {
        this.endUserLastName = endUserLastName;
    }

    public String getEntrepreneurName() {
        return entrepreneurName;
    }

    public void setEntrepreneurName(String entrepreneurName) {
        this.entrepreneurName = entrepreneurName;
    }

    public String getEntrepreneurLastName() {
        return entrepreneurLastName;
    }

    public void setEntrepreneurLastName(String entrepreneurLastName) {
        this.entrepreneurLastName = entrepreneurLastName;
    }

    public String getEndUserUsername() {
        return endUserUsername;
    }

    public void setEndUserUsername(String endUserUsername) {
        this.endUserUsername = endUserUsername;
    }

    public String getEntrepreneurUsername() {
        return entrepreneurUsername;
    }

    public void setEntrepreneurUsername(String entrepreneurUsername) {
        this.entrepreneurUsername = entrepreneurUsername;
    }
}
