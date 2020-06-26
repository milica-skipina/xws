package com.example.tim2.controller;

import com.example.tim2.dto.EndUserDTO;
import com.example.tim2.dto.ProfileDTO;
import com.example.tim2.security.TokenUtils;
import com.example.tim2.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


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
        logger.info("user " + loggedUsername + " blocked user: " + username  +" IP: " + request.getRemoteAddr() + " HOST: " + request.getRemoteHost() + "PORT: " + request.getRemotePort());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/activate/{id}")
    public ResponseEntity<HttpStatus> activate(@PathVariable Long id) throws MessagingException {
        if(userService.activateUser(id))
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/blockUser/{id}")
    public ResponseEntity<HttpStatus> blockUser(@PathVariable Long id ) {
        if(userService.blockUser(id))
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/unblockUser/{id}")
    public ResponseEntity<HttpStatus> unblockUser(@PathVariable Long id ) {
        if(userService.unblockUser(id))
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id ) {
        if(userService.deleteUser(id))
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping()
    public ResponseEntity<List<EndUserDTO>> getEndUsers( ) {
        return new ResponseEntity<List<EndUserDTO>>(userService.getAllEndUsers(),HttpStatus.OK);
    }

    @PostMapping(value = "/forgotPassword")
    public ResponseEntity<HttpStatus> forgotPassword(@RequestBody String[] email ) {
        if(userService.accountRecovery(email[0]))
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}

