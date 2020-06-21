package com.example.tim2.soap.clients;

import com.example.tim2.soap.gen.AddOrderRequest;
import com.example.tim2.soap.gen.AddOrderResponse;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

public class OrderClient extends WebServiceGatewaySupport {

    public AddOrderResponse addOrder() {
        AddOrderRequest request = new AddOrderRequest();

        AddOrderResponse response = (AddOrderResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request);
        return response;
    }
}
