package com.example.tim2.service;

import com.example.tim2.common.UserIdentifier;
import com.example.tim2.dto.MessageDTO;
import com.example.tim2.model.*;
import com.example.tim2.repository.*;
import com.example.tim2.security.TokenUtils;
import com.example.tim2.soap.clients.MessageClient;
import com.example.tim2.soap.gen.GetAllMessagesResponse;
import com.example.tim2.soap.gen.SendMessageResponse;
import groovy.transform.AutoExternalize;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.datatype.DatatypeConfigurationException;
import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    private MessageClient messageClient;


    public List<MessageDTO> getUserMessages(String token,String role) throws DatatypeConfigurationException {
        String username = tokenUtils.getUsernameFromToken(token);
        List<MessageDTO> dtos = new ArrayList<MessageDTO>();
        List<Message> messages = new ArrayList<Message>();
        GetAllMessagesResponse response = new GetAllMessagesResponse();
        try {
            response = messageClient.getMessages(username);
        }catch(Exception e){
            e.printStackTrace();
        }
        for(com.example.tim2.soap.gen.Message m : response.getMessages()){
            addFromSoap(m);
        }


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

    public boolean saveMessage(MessageDTO dto,String role)
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

            boolean isEndUserSender = true;
            if(role.equals("ROLE_CUSTOMER"))
                isEndUserSender = true;
            else
                isEndUserSender = false;

            try{
                SendMessageResponse response = messageClient.sendMessage(message,isEndUserSender);
                response.getMicroId();
            }catch (Exception e){
                e.getStackTrace();
            }
            messageRepository.save(message);
            return true;
        }
        return false;
    }

    private void addFromSoap(com.example.tim2.soap.gen.Message message){
        EndUser user = endUserRepository.findByUserUsername(message.getSenderUsername());
        Message oldMessage = messageRepository.findOneById(message.getId());
        if(oldMessage == null){
            Message newMesssage = new Message();
            newMesssage.setTimeSent(message.getTimeSent().toGregorianCalendar().getTime());
            newMesssage.setSubject(message.getSubject());
            newMesssage.setText(message.getText());
            newMesssage.setId(message.getId());
            if(user != null){
                newMesssage.setEndUser(endUserRepository.findByUserUsername(message.getSenderUsername()));
                newMesssage.setEntrepreneur(entrepreneurRepository.findByUserUsername(message.getReceiverUsername()));
            }else{
                newMesssage.setEndUser(endUserRepository.findByUserUsername(message.getReceiverUsername()));
                newMesssage.setEntrepreneur(entrepreneurRepository.findByUserUsername(message.getSenderUsername()));
            }
            messageRepository.save(newMesssage);
        }

    }
}
