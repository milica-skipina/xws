package tim2.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import tim2.auth.dto.ProfileDTO;
import tim2.auth.dto.UserDTO;
import tim2.auth.model.User;
import tim2.auth.security.TokenUtils;
import tim2.auth.service.UserService;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private TokenUtils tokenUtils;

    @PutMapping(value = "/changeRefusedNumber/{username}", consumes = "application/json")
    public ResponseEntity<Boolean> changeRefusedNumber(@PathVariable String username, @RequestBody Integer number) {
        userService.changeRefusedNumber(username, number);
        return new ResponseEntity<Boolean>(true, HttpStatus.OK);
    }

    @PutMapping(value = "/changeState/{username}", consumes = "application/json")
    public ResponseEntity<Boolean> changeState(@PathVariable String username, @RequestBody String change){
            userService.changeS(username, change);
            return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('CREATE_REQUEST')")
    @RequestMapping(method = RequestMethod.GET, value = "/canReserve/{username}") // ad id
    public ResponseEntity<Boolean> canReserve(@PathVariable String username)
    {
        boolean ret = userService.canR(username);
        return  new ResponseEntity<>(ret, HttpStatus.OK);
    }

    
    // TO DO
    @RequestMapping(method = RequestMethod.GET, value = "/verify")
    public ResponseEntity<HttpStatus> verify(HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        if(userService.verify(token))
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasAuthority('READ_USER')")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<UserDTO>> getAll(){
        return new ResponseEntity<>(userService.getAll(),HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('DELETE_USER')")
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id){
        User user = userService.deleteUser(id);
        if(user!= null) {
            logger.info("|SUCCESSS| DELETE USER| username: " + user.getUsername());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasAuthority('MODIFY_USER')")
    @RequestMapping(method = RequestMethod.GET,value = "/deactivate/{id}")
    public ResponseEntity<HttpStatus> deactivateUser(@PathVariable Long id){
        User user = userService.deactivateAccount(id);
        if(user != null) {
            logger.info("|SUCCESSS| DEACTIVATE USER| username: " + user.getUsername());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasAuthority('MODIFY_USER')")
    @RequestMapping(method = RequestMethod.GET,value = "/activate/{id}")
    public ResponseEntity<HttpStatus> activateUser(@PathVariable Long id){
        User user = userService.activateAccount(id);
        if(user != null) {
            logger.info("|SUCCESSS| ACTIVATE USER| username: " + user.getUsername());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // TO DO MOZDA TREBA IZMENITI AUTHORITY
    @PreAuthorize("hasAuthority('READ_PROFILE')")
    @RequestMapping(method = RequestMethod.GET, value="/current")
    public ResponseEntity<ProfileDTO> getCurrentUser(HttpServletRequest request){
        String token = tokenUtils.getToken(request);
        String username = tokenUtils.getUsernameFromToken(token);
        ProfileDTO profile = userService.getProfile(username);
        if(profile == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('READ_CUSTOMERS')")
    @RequestMapping(method = RequestMethod.GET, value="/customers")
    public ResponseEntity<List<String>> getCustomers(HttpServletRequest request){
        ArrayList<String> customers = userService.getAllCustomers();
        if(customers.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('BLOCK_END_USER')")
    @RequestMapping(method = RequestMethod.PUT, value="/{changedPermission}/{username}")
    public ResponseEntity<HttpStatus> block(@PathVariable String changedPermission, @PathVariable String username,HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        String loggedUsername = tokenUtils.getUsernameFromToken(token);
        userService.blockEndUser(changedPermission, username);
        logger.info("user " + loggedUsername + " blocked user: " + username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
