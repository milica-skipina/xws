package com.example.tim2.controller;

import com.example.tim2.common.UserIdentifier;
import com.example.tim2.dto.RequestDTO;
import com.example.tim2.model.Role;
import com.example.tim2.model.User;
import com.example.tim2.repository.EntrepreneurRepository;
import com.example.tim2.service.RequestService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

@RestController
@RequestMapping(value = "/request")
public class RequestController {

    private RequestService requestService;

    private UserIdentifier userIdentifier;

    private EntrepreneurRepository entrepreneurRepository;

    @Autowired
    public RequestController(RequestService requestService, UserIdentifier userIdentifier,
                             EntrepreneurRepository entrepreneurRepository) {
        this.requestService = requestService;
        this.userIdentifier = userIdentifier;
        this.entrepreneurRepository = entrepreneurRepository;
    }

    @PreAuthorize("hasAuthority('CREATE_REQUEST')")
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<HttpStatus> createRequests(@RequestBody String[] reqs, HttpServletRequest request) {
        User user = userIdentifier.verifyUser(request);
        if (user != null) {
            boolean success = requestService.createRequests(reqs, user);
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

    @PreAuthorize("hasAuthority('READ_REQUEST')")
    @RequestMapping(method = RequestMethod.GET, produces = "application/json", value= "")
    public Response findRequests(HttpServletRequest request) {
        User user = userIdentifier.verifyUser(request);
        if (user != null) {
            ArrayList<RequestDTO> frontRequests = new ArrayList<RequestDTO>();
            String role = ((Role) user.getRoles().toArray()[0]).getName();
            if (role.equals("ROLE_SELLER")) {
                frontRequests = requestService.requestsForApproving(user.getId());
                if (frontRequests.isEmpty()) {
                    return Response.status(Response.Status.NO_CONTENT).entity("No cars requested.").build();     // 204
                } else {
                    String json = new Gson().toJson(frontRequests);
                    return Response.ok(json, MediaType.APPLICATION_JSON).build();
                }
            } else {
                frontRequests = requestService.requestedCars(user.getId());
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

    @PreAuthorize("hasAuthority('MODIFY_REQUEST')")
    @RequestMapping(method = RequestMethod.PUT, produces = "application/json", value= "/{requestId}/{flag}")
    public Response modifyRequests(@PathVariable Long requestId, HttpServletRequest request,
                                   @PathVariable boolean flag) {
        User user = userIdentifier.verifyUser(request);
        if (user != null) {
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

    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    @RequestMapping(method = RequestMethod.GET, value = "/{carId}/{startDate}/{endDate}") // car id
    public ResponseEntity<Boolean> manualRenting(@PathVariable Long carId, @PathVariable String startDate,
                                                 @PathVariable String endDate, HttpServletRequest request)
    {
        User user = userIdentifier.verifyUser(request);
        boolean ret = requestService.manualRenting(carId, startDate, endDate);
        return  new ResponseEntity<>(ret, HttpStatus.OK);
    }
}
