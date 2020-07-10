package com.example.tim2.soap.clients;

import com.example.tim2.model.Entrepreneur;
import com.example.tim2.model.Message;
import com.example.tim2.repository.AdvertisementRepository;
import com.example.tim2.repository.MessageRepository;
import com.example.tim2.repository.PricelistRepository;
import com.example.tim2.service.MessageService;
import com.example.tim2.soap.gen.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.util.GregorianCalendar;
import java.util.List;

public class MessageClient extends WebServiceGatewaySupport {

    private static final Logger log = LoggerFactory.getLogger(MessageClient.class);

    private String microUsername = Entrepreneur.MICRO_USERNAME;

    @Autowired
    MessageService messageService;

    @Autowired
    MessageRepository messageRepository;

    public SendMessageResponse sendMessage(com.example.tim2.model.Message m,boolean isEndUserSender) throws DatatypeConfigurationException {
        com.example.tim2.soap.gen.Message generated = m.getGenerated(isEndUserSender);
        SendMessageRequest request = new SendMessageRequest();
        request.setMessage(generated);
        SendMessageResponse response = (SendMessageResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request);
        return response;
    }

    public GetAllMessagesResponse getMessages(String username) throws DatatypeConfigurationException {
        GetAllMessagesRequest request = new GetAllMessagesRequest();
        request.setUsername(username);
        GetAllMessagesResponse response = (GetAllMessagesResponse)getWebServiceTemplate().marshalSendAndReceive(request);
        return response;
    }


}