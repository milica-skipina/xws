package tim2.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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

    // TO DO
    @RequestMapping(method = RequestMethod.GET, value = "/verify/{token}")
    public ResponseEntity<HttpStatus> verify(@PathVariable String token) {
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

}
