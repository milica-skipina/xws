package com.example.demo.dto;

import java.util.Date;

public class MessageDTO {
    private Long id;
    private String text;
    private String subject;
    private Date timeSent;
    private Long senderId;
    private Long receiverId;

    public MessageDTO() {
    }

    public MessageDTO(Long id, String text, String subject, Date timeSent, Long senderId, Long receiverId) {
        this.id = id;
        this.text = text;
        this.subject = subject;
        this.timeSent = timeSent;
        this.senderId = senderId;
        this.receiverId = receiverId;
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

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }
}
