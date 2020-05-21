package orders.ordersmicroservice.service;

import orders.ordersmicroservice.dto.RequestDTO;
import orders.ordersmicroservice.model.Request;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class RentingService {

    public boolean createRequest(Request request, Long id){
        // to be implemented
        return true;
    }

    public boolean acceptRequest(Request request, Long id){
        // to be implemented
        return true;
    }

    public boolean declineRequest(Request request, Long id){
        // to be implemented
        return true;
    }

    public ArrayList<RequestDTO> getAcceptedRequests(Long id) {
        // to be implemented
        return null;
    }
}
