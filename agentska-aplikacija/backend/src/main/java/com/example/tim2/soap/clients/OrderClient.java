package com.example.tim2.soap.clients;

import com.example.tim2.model.Request;
import com.example.tim2.soap.gen.*;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;


public class OrderClient extends WebServiceGatewaySupport {

    public AddOrderResponse acceptOrder(Request req) {
        AddOrderRequest request = new AddOrderRequest();
        Order o = req.getGenerated();
        request.setOrder(o);
        request.setUsername(req.getSender().getUser().getUsername());
        AddOrderResponse response = (AddOrderResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request);
        return response;
    }

    public ModifyOrderResponse getMicroOrders(String agentUsername) {
        ModifyOrderRequest request = new ModifyOrderRequest();
        request.setUsername(agentUsername);
        ModifyOrderResponse response = (ModifyOrderResponse) getWebServiceTemplate().marshalSendAndReceive(request);

        return response;
    }

    public PayOrderResponse payOrder(Long id, String customerUsername) {
        PayOrderRequest request = new PayOrderRequest();
        request.setCustomerUsername(customerUsername);
        request.setMicroId(id);
        PayOrderResponse response = (PayOrderResponse) getWebServiceTemplate().marshalSendAndReceive(request);
        return response;
    }
}
