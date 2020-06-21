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

}
