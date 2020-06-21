package orders.ordersmicroservice.controller;

import com.google.gson.Gson;
import orders.ordersmicroservice.common.UserIdentifier;
import orders.ordersmicroservice.config.TokenUtils;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping(value = "/request")
public class RequestController {

    private RequestService requestService;

    private UserIdentifier userIdentifier;

    @Autowired
    private TokenUtils tokenUtils;

    private static final Logger logger = LoggerFactory.getLogger(RequestController.class);

    @Autowired
    public RequestController(RequestService requestService, UserIdentifier userIdentifier) {
        this.requestService = requestService;
        this.userIdentifier = userIdentifier;
    }

    @PreAuthorize("hasAuthority('MODIFY_AD')")
    @RequestMapping(method = RequestMethod.GET, value = "/hasAd/{id}") // ad id
    public ResponseEntity<Boolean> adHasRequest(@PathVariable Long id)
    {
        boolean ret = requestService.adRequest(id);
        return  new ResponseEntity<>(ret, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('CREATE_REQUEST')")
    @RequestMapping(method = RequestMethod.POST, value = "/{id}") // car id
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


    @PreAuthorize("hasAuthority('CREATE_REQUEST')")
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<HttpStatus> createRequests(@RequestBody String[] reqs, HttpServletRequest request) {
        String jwt = tokenUtils.getToken(request);
        String[] data = userIdentifier.extractFromJwt(request);
        if (data[0] != null) {      // data[0] je username
            boolean success = requestService.createRequests(reqs, data, jwt);
            if(!success){
                logger.error("|FAILED CREATE REQUEST| user " + data[1]);
                return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
            }
            else{
                logger.info("|SUCCESS CREATE REQUEST| user " + data[1]);
                return new ResponseEntity<>(HttpStatus.CREATED);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('READ_REQUEST')")
    @RequestMapping(method = RequestMethod.GET, produces = "application/json", value= "/{order}")
    public Response findRequests(HttpServletRequest request, @PathVariable boolean order) {
        String[] user = userIdentifier.extractFromJwt(request);
        if (user[0] != null) {
            requestService.after24hOr12h();
            ArrayList<RequestDTO> frontRequests = new ArrayList<RequestDTO>();
            String role = userIdentifier.roleFromJwt(request);
            if (!order) {
                frontRequests = requestService.requestsForApproving(user[0]);
                if (frontRequests == null) {
                    return Response.status(Response.Status.BAD_REQUEST).entity("Validation didn't pass.").build();
                }
                else if (frontRequests.isEmpty()) {
                    return Response.status(Response.Status.NO_CONTENT).entity("No cars requested.").build();     // 204
                } else {
                    String json = new Gson().toJson(frontRequests);
                    return Response.ok(json, MediaType.APPLICATION_JSON).build();
                }
            } else {
                frontRequests = requestService.requestedCars(user[0]);
                if (frontRequests == null) {
                    return Response.status(Response.Status.BAD_REQUEST).entity("Validation didn't pass.").build();
                }
                else if (frontRequests.isEmpty()) {
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
        String[] user = userIdentifier.extractFromJwt(request);
        String token = tokenUtils.getToken(request);
        String username = tokenUtils.getUsernameFromToken(token);
        if (user[0] != null) {
            boolean success = requestService.modifyRequest(requestId, flag);
            if(!success){       // flag true - accept request
                return Response.status(Response.Status.NOT_MODIFIED).entity("Request is not modified.").build();
            }
            else if (success && flag) {
                logger.info("|SUCCESS EDIT REQUEST| user " + username);
                return Response.status(Response.Status.OK).entity("Request is accepted!").build();
            }else {     //uspesno i decline
                logger.info("|SUCCESS EDIT REQUEST| user " + username);
                return Response.status(Response.Status.OK).entity("Request is declined!").build();
            }
        } else {
            logger.error("|FAILED EDIT REQUEST| user " + username);
            return Response.status(Response.Status.FORBIDDEN).entity("Not permitted!").build();
        }
    }

    /**
     * kada prodavac rucno unese zauzece oglasa
     * @param carId
     * @param request
     * @return
     */
    @PreAuthorize("hasAuthority('MODIFY_AD')")
    @RequestMapping(method = RequestMethod.POST, value = "/{carId}/{startDate}/{endDate}") // car id
    public ResponseEntity<Boolean> manualRenting(@PathVariable Long carId, @PathVariable String startDate,
                                                 @PathVariable String endDate, @RequestBody String[] customerData,
                                                 HttpServletRequest request)
    {
        String token = tokenUtils.getToken(request);
        String[] data = userIdentifier.extractFromJwt(request);
        boolean ret = requestService.manualRenting(carId, startDate, endDate, customerData, data, token);       // customer username
        return  new ResponseEntity<>(ret, HttpStatus.OK);
    }


    @GetMapping(value="/car/{startDate}/{endDate}")
    public ResponseEntity<Long[]> getReservedCars(@PathVariable("startDate") String  start,
                                                      @PathVariable("endDate") String end){
        Date startDate = new Date(Long.parseLong(start));
        Date endDate = new Date(Long.parseLong(end));
        List<Long> ids = requestService.findAllByStateAndStartDateAndEndDate("PAID", startDate, endDate);
        Long[] ret = new Long[ids.size()];
        if (!ids.isEmpty()) {
            ret = (Long[]) ids.toArray();
        }
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_CUSTOMER')")
    @RequestMapping(method = RequestMethod.PUT, value = "/pay/{id}") // car id
    public ResponseEntity<Boolean> payForRenting(@PathVariable String id, HttpServletRequest request)
    {
        Long idd = Long.parseLong(id);
        String[] data = userIdentifier.extractFromJwt(request);
        boolean ret = requestService.paymentMethod(idd, data[0]);       // customer username
        return  new ResponseEntity<>(ret, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/available") //
    public ResponseEntity<Boolean> checkingRequestsState(HttpServletRequest request)
    {
        requestService.after24hOr12h();       // customer username
        return  new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('WRITE_REVIEW')")
    @GetMapping(produces="application/json", value="/{id}/writeReview")
    public ResponseEntity<Boolean> canWriteReview(@PathVariable Long id, HttpServletRequest request){
        String token = tokenUtils.getToken(request);
        String username = tokenUtils.getUsernameFromToken(token);
        boolean allowed = requestService.canWriteReview(username, id);
        return new ResponseEntity<>(allowed, HttpStatus.OK);
    }

    @GetMapping(value = "/messageCheck/{customerUsername}/{agentUsername}")
    public ResponseEntity<Boolean> messageCheck(@PathVariable String customerUsername,@PathVariable String agentUsername){
        boolean ret = requestService.messageCheck(customerUsername,agentUsername);
        return new ResponseEntity<>(ret,HttpStatus.OK);
    }

}
