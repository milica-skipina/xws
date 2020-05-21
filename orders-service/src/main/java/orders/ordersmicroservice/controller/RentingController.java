package orders.ordersmicroservice.controller;

import orders.ordersmicroservice.dto.AdvertisementDTO;
import orders.ordersmicroservice.dto.RequestDTO;
import orders.ordersmicroservice.model.Request;
import orders.ordersmicroservice.service.RentingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/rent")
public class RentingController {

    @Autowired
    private RentingService rentingService;

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", value = "/{id}") // customer id
    public ResponseEntity<HttpStatus> createRequest(Request request, @PathVariable Long id)
    {
        boolean success = rentingService.createRequest(request, id);
        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json", value = "/accept/{id}")
    public ResponseEntity<HttpStatus> acceptRequest(Request request, @PathVariable Long id)
    {
        boolean success = rentingService.acceptRequest(request, id);
        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json", value = "/decline/{id}")
    public ResponseEntity<HttpStatus> declineRequest(Request request, @PathVariable Long id)
    {
        boolean success = rentingService.declineRequest(request, id);
        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json", value = "/{id}") // customer id
    public ResponseEntity<List<RequestDTO>> rentingHistory(@PathVariable Long id)
    {
        ArrayList<RequestDTO> history = rentingService.getAcceptedRequests(id);
        if (!history.isEmpty()) {
            return new ResponseEntity<>(history, HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }



}
