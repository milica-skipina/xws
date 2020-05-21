package com.example.tim2.controller;

import com.example.tim2.dto.RegistrationDTO;
import com.example.tim2.model.EndUser;
import com.example.tim2.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    /*@RequestMapping(method = RequestMethod.POST, consumes = "application/json", value = "/login")
    public ResponseEntity<User> login(User u)
    {
        User user = authService.loginUser(u);
        if (user != null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }*/

    /**
     * samo enduseri mogu da se registruju, na agentu ne postavljaju oglase
     * @param reg
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST, value = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<EndUser> register(@RequestBody RegistrationDTO reg) throws Exception {
        EndUser user = authService.registerUser(reg);
        if (user == null) {
            return new ResponseEntity<EndUser>(HttpStatus.EXPECTATION_FAILED);
        }
        return new ResponseEntity<EndUser>(user, HttpStatus.OK);
    }
}
