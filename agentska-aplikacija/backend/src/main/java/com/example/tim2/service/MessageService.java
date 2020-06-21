package com.example.tim2.service;

import com.example.tim2.common.UserIdentifier;
import com.example.tim2.dto.MessageDTO;
import com.example.tim2.model.*;
import com.example.tim2.repository.EndUserRepository;
import com.example.tim2.repository.EntrepreneurRepository;
import com.example.tim2.repository.MessageRepository;
import com.example.tim2.repository.RequestRepository;
import com.example.tim2.security.TokenUtils;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private EntrepreneurRepository entrepreneurRepository;

    @Autowired
    private EndUserRepository endUserRepository;

    @Autowired
    private RequestRepository requestRepository;

    public List<MessageDTO> getUserMessages(String token,String role){
        String username = tokenUtils.getUsernameFromToken(token);
        List<MessageDTO> dtos = new ArrayList<MessageDTO>();
        List<Message> messages = new ArrayList<Message>();
        if(role.equals("ROLE_CUSTOMER"))
        {
            EndUser endUser = endUserRepository.findByUserUsername(username);
            messages = messageRepository.findAllByEndUserId(endUser.getId());
        }else
        {
            Entrepreneur entrepreneur = entrepreneurRepository.findByUserUsername(username);
            messages = messageRepository.findAllByEntrepreneurId(entrepreneur.getId());
        }

        for(Message m : messages)
        {
            dtos.add(new MessageDTO(m));
        }
        return dtos;
    }

    public boolean saveMessage(MessageDTO dto)
    {
        List<Request> requests = requestRepository.findAllByUserUsernameAndEntrepreneurUserUsernameAndState(dto.getEndUserUsername(),dto.getEntrepreneurUsername(),"RESERVED");
        if(!requests.isEmpty()) {
            Message message = new Message();
            message.setText(dto.getText());
            message.setText(Encode.forHtml(dto.getText()));
            message.setSubject(dto.getSubject());
            message.setTimeSent(dto.getTimeSent());
            message.setEndUser(endUserRepository.findByUserUsername(dto.getEndUserUsername()));
            message.setEntrepreneur(entrepreneurRepository.findByUserUsername(dto.getEntrepreneurUsername()));
            messageRepository.save(message);
            return true;
        }
        return false;
    }
}
