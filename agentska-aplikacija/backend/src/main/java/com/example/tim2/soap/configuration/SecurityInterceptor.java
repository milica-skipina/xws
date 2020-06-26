package com.example.tim2.soap.configuration;

import com.example.tim2.model.Entrepreneur;
import com.example.tim2.security.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpUrlConnection;
import org.springframework.ws.transport.http.HttpUrlConnectionMessageSender;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.net.HttpURLConnection;

public class SecurityInterceptor implements ClientInterceptor {

    @Autowired
    TokenUtils tokenUtils;

    public SecurityInterceptor(TokenUtils tokenUtils){
        this.tokenUtils = tokenUtils;
    }

    @Override
    public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {

        TransportContext context = TransportContextHolder.getTransportContext();
        HttpUrlConnection connection = (HttpUrlConnection) context.getConnection();

        String encodedAuthorization = tokenUtils.generateToken(Entrepreneur.MICRO_USERNAME);
        try {
            connection.addRequestHeader("Authorization","Bearer " + encodedAuthorization);
        } catch (IOException e) {
          //  log.error(e.getMessage());
        }
        return true;
    }

    @Override
    public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
        return false;
    }

    @Override
    public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
        return false;
    }

    @Override
    public void afterCompletion(MessageContext messageContext, Exception ex) throws WebServiceClientException {

    }
}
