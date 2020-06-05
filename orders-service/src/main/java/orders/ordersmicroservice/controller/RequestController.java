package orders.ordersmicroservice.controller;

import com.google.gson.Gson;
import orders.ordersmicroservice.common.UserIdentifier;
import orders.ordersmicroservice.dto.RequestDTO;
import orders.ordersmicroservice.model.Request;
import orders.ordersmicroservice.service.RequestService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/request")
public class RequestController {

    private RequestService requestService;

    private UserIdentifier userIdentifier;

    @Autowired
    public RequestController(RequestService requestService, UserIdentifier userIdentifier) {
        this.requestService = requestService;
        this.userIdentifier = userIdentifier;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/hasAd/{id}") // ad id
    public ResponseEntity<Boolean> adHasRequest(@PathVariable Long id)
    {
        boolean ret = requestService.adRequest(id);
        return  new ResponseEntity<>(ret, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{id}") // car id
    public ResponseEntity<Boolean> availableForBasket(@PathVariable Long id, HttpServletRequest request)
    {
        String[] data = userIdentifier.extractFromJwt(request);
        boolean ret = requestService.isInBasket(id, data[0]);       // customer username
        return  new ResponseEntity<>(ret, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/test") // ad id
    public ResponseEntity<String> test()
    {
        return  new ResponseEntity<>("ok", HttpStatus.OK);
    }



    //@PreAuthorize("hasAuthority('CREATE_REQUEST')")
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<HttpStatus> createRequests(@RequestBody String[] reqs, HttpServletRequest request) {
        String[] data = userIdentifier.extractFromJwt(request);
        if (data[0] != null) {      // data[0] je username
            boolean success = requestService.createRequests(reqs, data);
            if(!success){
                return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
            }
            else{
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    //@PreAuthorize("hasAuthority('READ_REQUEST')")
    @RequestMapping(method = RequestMethod.GET, produces = "application/json", value= "")
    public Response findRequests(HttpServletRequest request) {
        String[] user = userIdentifier.extractFromJwt(request);
        if (user[0] != null) {
            ArrayList<RequestDTO> frontRequests = new ArrayList<RequestDTO>();
            String role = userIdentifier.roleFromJwt(request);
            if (role.equals("ROLE_SELLER")) {
                frontRequests = requestService.requestsForApproving(user[0]);
                if (frontRequests.isEmpty()) {
                    return Response.status(Response.Status.NO_CONTENT).entity("No cars requested.").build();     // 204
                } else {
                    String json = new Gson().toJson(frontRequests);
                    return Response.ok(json, MediaType.APPLICATION_JSON).build();
                }
            } else {
                frontRequests = requestService.requestedCars(user[0]);
                if (frontRequests.isEmpty()) {
                    return Response.status(Response.Status.NO_CONTENT).entity("Your renting history is empty!").build();     // 204
                } else {
                    String json = new Gson().toJson(frontRequests);
                    return Response.ok(json, MediaType.APPLICATION_JSON).build();
                }
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity("Not permitted!").build();
        }
    }

    //@PreAuthorize("hasAuthority('MODIFY_REQUEST')")
    @RequestMapping(method = RequestMethod.PUT, produces = "application/json", value= "/{requestId}/{flag}")
    public Response modifyRequests(@PathVariable Long requestId, HttpServletRequest request,
                                   @PathVariable boolean flag) {
        String[] user = userIdentifier.extractFromJwt(request);
        if (user[0] != null) {
            boolean success = requestService.modifyRequest(requestId, flag);
            if(!success){       // flag true - accept request
                return Response.status(Response.Status.NOT_MODIFIED).entity("Request is not modified.").build();
            }
            else if (success && flag) {
                return Response.status(Response.Status.OK).entity("Request is accepted!").build();
            }else {     //uspesno i decline
                return Response.status(Response.Status.OK).entity("Request is declined!").build();
            }
        } else {
            return Response.status(Response.Status.FORBIDDEN).entity("Not permitted!").build();
        }
    }

    /**
     * kada prodavac rucno unese zauzece oglasa
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(method = RequestMethod.GET, value = "/{carId}/{startDate}/{endDate}/{customerUsername}") // car id
    public ResponseEntity<Boolean> manualRenting(@PathVariable Long id, @PathVariable String startDate,
                                                 @PathVariable String endDate, @PathVariable String customerUsername,
                                                 HttpServletRequest request)
    {
        String[] data = userIdentifier.extractFromJwt(request);
        boolean ret = requestService.manualRenting(id, startDate, endDate, customerUsername, data);       // customer username
        return  new ResponseEntity<>(ret, HttpStatus.OK);
    }

    @GetMapping(value="/car/{startDate}/{endDate}")
    public ResponseEntity<List<Long>> getReservedCars(@PathVariable("startDate") String  start,
                                                      @PathVariable("endDate") String end){
        Date startDate = new Date(Long.parseLong(start));
        Date endDate = new Date(Long.parseLong(end));
        List<Long> ids = requestService.findAllByStateAndStartDateAndEndDate("PAID", startDate, endDate);
        return new ResponseEntity<>(ids, HttpStatus.OK);
    }

}
