package orders.ordersmicroservice.endpoints;

import net.bytebuddy.asm.Advice;
import orders.ordersmicroservice.dto.RequestWrapDTO;
import orders.ordersmicroservice.model.Car;
import orders.ordersmicroservice.model.Request;
import orders.ordersmicroservice.model.RequestWrapper;
import orders.ordersmicroservice.service.RequestService;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import rs.ac.uns.ftn.xws_tim2.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

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
        Order order = request.getOrder();
        Request newRequest = new Request();
        AddOrderResponse response = new AddOrderResponse();
        boolean isOk = requestService.availableForAgent(order, response);
        response.setOk(isOk);

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "payOrderRequest")
    @ResponsePayload
    public PayOrderResponse payOrder(@RequestPayload PayOrderRequest request) {
        PayOrderResponse response = new PayOrderResponse();
        boolean isOk = requestService.paymentMethod(request.getMicroId(), request.getCustomerUsername());
        response.setOk(isOk);

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "modifyOrderRequest")
    @ResponsePayload
    public ModifyOrderResponse getMicroOrders(@RequestPayload ModifyOrderRequest request) {
        ModifyOrderResponse response = new ModifyOrderResponse();
        ArrayList<Wrapper> ret = requestService.convertForSoap(request.getUsername());
        response.setOk(true);
        response.getRequestWrappers().addAll(ret);
        return response;
    }

}
