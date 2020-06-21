package com.example.tim2.soap.clients;

import com.example.tim2.model.Request;
import com.example.tim2.soap.gen.AddOrderRequest;
import com.example.tim2.soap.gen.AddOrderResponse;
import com.example.tim2.soap.gen.Order;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

public class OrderClient extends WebServiceGatewaySupport {

    public AddOrderResponse acceptOrder(Request req) {
        AddOrderRequest request = new AddOrderRequest();
        Order o = req.getGenerated();
        request.setOrder(o);
        request.setUsername(req.getEntrepreneur().getUser().getUsername());
        AddOrderResponse response = (AddOrderResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request);
        return response;
    }
}
