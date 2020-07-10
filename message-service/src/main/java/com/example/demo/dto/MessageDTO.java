package com.example.demo.dto;

import com.example.demo.model.Message;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private Long id;
    private String text;
    private String subject;
    private Date timeSent;
    private String senderUsername;
    private String receiverUsername;

    public MessageDTO(Message message) {
        this.id = message.getId();
        this.text = message.getText();
        this.subject = message.getSubject();
        this.timeSent = message.getTimeSent();
        this.senderUsername = message.getSenderUsername();
        this.receiverUsername = message.getReceiverUsername();
    }

    public MessageDTO(rs.ac.uns.ftn.xws_tim2.Message m){
        this.receiverUsername = m.getReceiverUsername();
        this.senderUsername = m.getSenderUsername();
        this.subject = m.getSubject();
        this.text = m.getText();
        this.timeSent = m.getTimeSent().toGregorianCalendar().getTime();
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
}
