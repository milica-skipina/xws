package orders.ordersmicroservice.endpoints;

import net.bytebuddy.asm.Advice;
import orders.ordersmicroservice.service.RequestService;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import rs.ac.uns.ftn.xws_tim2.AddOrderRequest;
import rs.ac.uns.ftn.xws_tim2.AddOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;

@Endpoint
public class OrderEndpoint {

    private static final String NAMESPACE_URI = "http://www.ftn.uns.ac.rs/xws_tim2";

    private RequestService requestService;

    @Autowired
    public OrderEndpoint(RequestService requestService){
        this.requestService = requestService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addOrderRequest")
    @ResponsePayload
    public AddOrderResponse addOrder(@RequestPayload AddOrderRequest request) {

        AddOrderResponse response = new AddOrderResponse();

        response.setMicroId(1L);
        return response;
    }

}
