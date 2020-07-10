package com.example.demo.endpoint;

import com.example.demo.dto.MessageDTO;
import com.example.demo.repository.MessageRepository;
import com.example.demo.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.MessageCodesResolver;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import rs.ac.uns.ftn.xws_tim2.*;

import javax.xml.datatype.DatatypeConfigurationException;
import java.util.ArrayList;
import java.util.List;

@Endpoint
public class Endpoints {

    private static final String NAMESPACE_URI = "http://www.ftn.uns.ac.rs/xws_tim2" ;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageService messageService;

    @Autowired
    public Endpoints(MessageService messageService, MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
        this.messageService = messageService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "sendMessageRequest")
    @ResponsePayload
    public SendMessageResponse sendMessage(@RequestPayload SendMessageRequest request) {

        Message message = request.getMessage();
        MessageDTO newMassage = new MessageDTO(message);
        com.example.demo.model.Message ret = messageService.addMessage(newMassage, "");
        SendMessageResponse response = new SendMessageResponse();
        response.setMicroId(ret.getId());
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllMessagesRequest")
    @ResponsePayload
    public GetAllMessagesResponse getMessages(@RequestPayload GetAllMessagesRequest request) throws DatatypeConfigurationException {

        List<com.example.demo.model.Message> userMessages = messageRepository.findAllByReceiverUsername(request.getUsername());
        GetAllMessagesResponse response = new GetAllMessagesResponse();

        List<Message> messages = new ArrayList<Message>();
        for(com.example.demo.model.Message m : userMessages){
            if(m != null){
                response.getMessages().add(m.getGenerated());
            }
        }
        return response;
    }
}
