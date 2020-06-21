package orders.ordersmicroservice.endpoints;

import net.bytebuddy.asm.Advice;
import orders.ordersmicroservice.model.Car;
import orders.ordersmicroservice.model.Request;
import orders.ordersmicroservice.service.RequestService;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import rs.ac.uns.ftn.xws_tim2.AddOrderRequest;
import rs.ac.uns.ftn.xws_tim2.AddOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import rs.ac.uns.ftn.xws_tim2.Order;

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
        List<Request> checkReq = requestService.findAllByEndDateLessThanEqualOrStartDateGreaterThanEqualAndAgentUsernameAndState(
                order.getEndDate().toGregorianCalendar().getTime(), order.getStartDate().toGregorianCalendar().getTime(),
                order.getCustomerUsername(), "PAID");
        if (checkReq.isEmpty()) {       // nema tog zahteva, pa ga treba dodati
            newRequest = requestService.requestWrapper(order);
            response.setMicroId(newRequest.getId());
            requestService.save(newRequest);
        } else {
            for (Request r : checkReq ) {
                for (Car car : r.getCars() ) {
                    for (Long idX : order.getCars() ) {
                        if (car.getId() == idX ) {
                            // u response dodati flag false
                            System.out.println("Pronadjen auto " + idX);
                             break;
                        }
                    }

                }
            }
        }

        response.setMicroId(14L);   //znak da nije kreiran novi zahtev, therefore traba staviti false flag

        return response;
    }

}
