package com.example.demo.service;

import com.example.demo.dto.MessageDTO;
import com.example.demo.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public void addMessage(MessageDTO messageDTO){}

    public void deleteUserMessages(Long id){}

    public void checkRequestState(){}

    public void getUserMessages(Long id){}

    public void deleteConversation(Long id,Long id1){}
}
