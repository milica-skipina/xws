package com.example.tim2.controller;

import com.example.tim2.dto.RegistrationDTO;
import com.example.tim2.model.Role;
import com.example.tim2.model.EndUser;
import com.example.tim2.model.User;
import com.example.tim2.model.UserTokenState;
import com.example.tim2.security.TokenUtils;
import com.example.tim2.security.auth.JwtAuthenticationRequest;
import com.example.tim2.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", value = "/login")
    public ResponseEntity<UserTokenState> loginUser(@RequestBody JwtAuthenticationRequest authenticationRequest,
                                                    HttpServletResponse response) throws AuthenticationException, IOException {
        User loggedUser = authService.loginUser(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        if (loggedUser != null) {
            final Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()));

            // Ubaci username + password u kontext
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Kreiraj token
            User user = (User) authentication.getPrincipal();
            String jwt = tokenUtils.generateToken(user.getUsername());
            int expiresIn = tokenUtils.getExpiredIn();
            String role = ((Role) user.getRoles().toArray()[0]).getName();

            return ResponseEntity.ok(new UserTokenState(jwt, expiresIn, role ));
        } else {
            String message = "Username and/or password is invalid.";
            return new ResponseEntity<UserTokenState>(new UserTokenState(message, 0, "" ), HttpStatus.NOT_FOUND);
        }

    }

    /**
     * samo enduseri mogu da se registruju, na agentu ne postavljaju oglase
     * @param reg
     * @return
     * @throws Exception
     */
    @RequestMapping(method = RequestMethod.POST, value = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<String> register(@RequestBody RegistrationDTO reg) throws Exception {
        EndUser user = authService.registerUser(reg);
        if (user == null) {
            return new ResponseEntity<String>(HttpStatus.EXPECTATION_FAILED);
        }
        return new ResponseEntity<String>("Successful registration. Please log in to access system.", HttpStatus.OK);
    }
}
