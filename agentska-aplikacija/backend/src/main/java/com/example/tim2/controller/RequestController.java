package com.example.tim2.controller;

import com.example.tim2.common.UserIdentifier;
import com.example.tim2.dto.BasketDTO;
import com.example.tim2.dto.RequestWrapDTO;
import com.example.tim2.model.Role;
import com.example.tim2.model.User;
import com.example.tim2.repository.EntrepreneurRepository;
import com.example.tim2.security.TokenUtils;
import com.example.tim2.service.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/request")
public class RequestController {

    private RequestService requestService;

    private UserIdentifier userIdentifier;

    private EntrepreneurRepository entrepreneurRepository;

    @Autowired
    private TokenUtils tokenUtils;

    private static final Logger logger = LoggerFactory.getLogger(RequestController.class);

    @Autowired
    public RequestController(RequestService requestService, UserIdentifier userIdentifier,
                             EntrepreneurRepository entrepreneurRepository) {
        this.requestService = requestService;
        this.userIdentifier = userIdentifier;
        this.entrepreneurRepository = entrepreneurRepository;
    }

    @PreAuthorize("hasAuthority('CREATE_REQUEST')")
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<HttpStatus> createRequests(@RequestBody ArrayList<BasketDTO> reqs, HttpServletRequest request) {
        User user = userIdentifier.verifyUser(request);
        String token = tokenUtils.getToken(request);
        String username = tokenUtils.getUsernameFromToken(token);
        if (user != null) {
            boolean success = requestService.createRequests(reqs, user);
            if(!success){
                return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
            }
            else{
                logger.info("CREATE | user with username: " + username + " created request");
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        } else {
            logger.info("FAILED CREATE REQUEST | user with username: " + username);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('READ_REQUEST')")
    @RequestMapping(method = RequestMethod.GET, produces = "application/json", value= "")
    public ResponseEntity<List<RequestWrapDTO>> findRequests(HttpServletRequest request) {
        User user = userIdentifier.verifyUser(request);
        requestService.after24hOr12h();
        if (user != null) {
            ArrayList<RequestWrapDTO> frontRequests ;
            String role = ((Role) user.getRoles().toArray()[0]).getName();
            if (role.equals("ROLE_SELLER")) {
                frontRequests = requestService.requestsForApproving(user.getUsername());
                if (frontRequests.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);     // 204
                } else {
                    return new ResponseEntity<>(frontRequests, HttpStatus.OK);
                }
            } else {
                frontRequests = requestService.requestedCars(user.getUsername());
                if (frontRequests.isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.NO_CONTENT);     // 204
                } else {
                    return new ResponseEntity<>(frontRequests, HttpStatus.OK);
                }
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('MODIFY_REQUEST')")
    @RequestMapping(method = RequestMethod.PUT, produces = "application/json", value= "/{requestId}/{flag}")
    public Response modifyRequests(@PathVariable Long requestId, HttpServletRequest request,
                                   @PathVariable boolean flag) {
        User user = userIdentifier.verifyUser(request);
        String token = tokenUtils.getToken(request);
        String username = tokenUtils.getUsernameFromToken(token);
        String role = tokenUtils.getRoleFromToken(token);
        if (user != null) {
            System.out.println(role);
            boolean success = requestService.modifyRequest(requestId, flag, username, role);
            if(!success && flag){       // flag true - accept request
                return Response.status(Response.Status.NOT_MODIFIED).entity("Car has already been booked for these days.").build();
            } else if (!success && !flag){       // flag false - cancel request
                return Response.status(Response.Status.NOT_MODIFIED).entity("Request is not canceled.").build();
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

    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    @RequestMapping(method = RequestMethod.POST, value = "/{carId}/{startDate}/{endDate}")
    public ResponseEntity<Boolean> manualRenting(@PathVariable Long carId, @PathVariable String startDate,
                                                 @PathVariable String endDate, HttpServletRequest request,
                                                 @RequestBody String[] data)
    {
        User user = userIdentifier.verifyUser(request);     // ulogovani prodavac
        boolean ret = requestService.manualRenting(carId, startDate, endDate, user.getId(), data);
        return  new ResponseEntity<>(ret, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('CREATE_REQUEST')")
    @RequestMapping(method = RequestMethod.GET, value = "/{id}/{start}/{end}") // car id
    public ResponseEntity<Boolean> availableForBasket(@PathVariable Long id, HttpServletRequest request,
                                                      @PathVariable String start, @PathVariable String end)
    {
        User user = userIdentifier.verifyUser(request);
        boolean ret = requestService.addToWishlist(id, user.getUsername(), start, end);       // customer username
        return  new ResponseEntity<>(ret, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @RequestMapping(method = RequestMethod.PUT, value = "/pay/{id}") // car id
    public ResponseEntity<Boolean> payForRenting(@PathVariable String id, HttpServletRequest request)
    {
        Long idd = Long.parseLong(id);
        User user = userIdentifier.verifyUser(request);
        boolean ret = requestService.paymentMethod(idd, user.getUsername());       // customer username
        return  new ResponseEntity<>(ret, HttpStatus.OK);
    }
}
