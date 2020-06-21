package com.example.tim2.controller;

import com.example.tim2.dto.ProfileDTO;
import com.example.tim2.security.TokenUtils;
import com.example.tim2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
