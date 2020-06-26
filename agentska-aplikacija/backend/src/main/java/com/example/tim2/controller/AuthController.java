package com.example.tim2.controller;

import com.example.tim2.dto.PasswordChangeDTO;
import com.example.tim2.dto.RegistrationDTO;
import com.example.tim2.model.EndUser;
import com.example.tim2.model.Role;
import com.example.tim2.model.User;
import com.example.tim2.model.UserTokenState;
import com.example.tim2.repository.UserRepository;
import com.example.tim2.security.TokenUtils;
import com.example.tim2.security.auth.JwtAuthenticationRequest;
import com.example.tim2.service.AuthService;
import com.example.tim2.service.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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


    @Autowired
    private RequestService requestService;

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);


    @RequestMapping(method = RequestMethod.POST, value = "/login")
    public ResponseEntity<UserTokenState> loginUser(@RequestBody JwtAuthenticationRequest authenticationRequest,
                                                    HttpServletRequest request) throws AuthenticationException, IOException {
        User loggedUser = authService.loginUser(authenticationRequest.getUsername());

        if (loggedUser != null) {

            try {
                final Authentication authentication = authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                                authenticationRequest.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                User user = (User) authentication.getPrincipal();
                String jwt = tokenUtils.generateToken(user.getUsername());
                int expiresIn = tokenUtils.getExpiredIn();
                String role = ((Role) user.getRoles().toArray()[0]).getName();
                logger.info("SUCCESS | user with username: " + authenticationRequest.getUsername() + " logged in " + "IP: " + request.getRemoteAddr() + " HOST: " + request.getRemoteHost() + " PORT: " + request.getRemotePort());
                return ResponseEntity.ok(new UserTokenState(jwt, expiresIn, role));
            } catch (Exception e) {
                e.printStackTrace();
                String message = "Bad credentials.";
                loggedUser.setNumberFailedLogin(loggedUser.getNumberFailedLogin() + 1);
                userRepository.save(loggedUser);
                logger.error(" |FAILED| user with username: " + authenticationRequest.getUsername() + " tried to log in " + "IP: " + request.getRemoteAddr() + " HOST: " + request.getRemoteHost() + " PORT: " + request.getRemotePort() + " number of failed logins: " + loggedUser.getNumberFailedLogin());

                if (!loggedUser.isDeleted()) {
                    if (!loggedUser.isBlocked()) {
                        try {

                            EndUser endUser = loggedUser.getEnduser();
                            if (endUser != null) {
                                if (!endUser.isActivated()) {
                                    message = "Account is not activated";
                                    logger.error(" |FAILED| user with username: " + authenticationRequest.getUsername() + " tried to log in " + "IP: " + request.getRemoteAddr() + " HOST: " + request.getRemoteHost() + " PORT: " + request.getRemotePort() + " account is not activated");
                                    return new ResponseEntity<UserTokenState>(new UserTokenState(message, 0, ""), HttpStatus.FORBIDDEN);
                                }
                            }


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
                            logger.info("SUCCESS | user with username: " + authenticationRequest.getUsername() + " logged in " + "IP: " + request.getRemoteAddr() + " HOST: " + request.getRemoteHost() + " PORT: " + request.getRemotePort());
                            return ResponseEntity.ok(new UserTokenState(jwt, expiresIn, role));
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            message = "Bad credentials.";
                            loggedUser.setNumberFailedLogin(loggedUser.getNumberFailedLogin() + 1);
                            userRepository.save(loggedUser);
                            logger.error(" |FAILED| user with username: " + authenticationRequest.getUsername() + " tried to log in " + "IP: " + request.getRemoteAddr() + " HOST: " + request.getRemoteHost() + " PORT: " + request.getRemotePort() + " number of failed logins: " + loggedUser.getNumberFailedLogin());
                            return new ResponseEntity<UserTokenState>(new UserTokenState(message, 0, ""), HttpStatus.NOT_FOUND);
                        }
                    } else {
                        message = "Account is blocked.";
                        logger.error(" |FAILED| user with username: " + authenticationRequest.getUsername() + " tried to log in " + "IP: " + request.getRemoteAddr() + " HOST: " + request.getRemoteHost() + " PORT: " + request.getRemotePort() + " account is blocked");
                        return new ResponseEntity<UserTokenState>(new UserTokenState(message, 0, ""), HttpStatus.NOT_FOUND);
                    }
                } else {
                    message = "Account is deleted.";
                    logger.error(" |FAILED| user with username: " + authenticationRequest.getUsername() + " tried to log in " + "IP: " + request.getRemoteAddr() + " HOST: " + request.getRemoteHost() + " PORT: " + request.getRemotePort() + " account is deleted");

                    return new ResponseEntity<UserTokenState>(new UserTokenState(message, 0, ""), HttpStatus.NOT_FOUND);
                }
            }
        } else {
            String message = "Username and/or password is invalid.";
            //logger.error(" |FAILED| user with username: " + authenticationRequest.getUsername() + " tried to log in " + "IP: " + request.getRemoteAddr() + " HOST: " + request.getRemoteHost() + " PORT: " + request.getRemotePort() + " number of failed logins: " + loggedUser.getNumberFailedLogin());
            return new ResponseEntity<UserTokenState>(new UserTokenState(message, 0, ""), HttpStatus.NOT_FOUND);
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

    @RequestMapping(method = RequestMethod.GET, value = "/verify")
    public ResponseEntity<HttpStatus> meilVerify(HttpServletRequest request) throws Exception {
        String token = tokenUtils.getToken(request);
        if (token != null || !token.equals("") ) {
            authService.confirmEmail(token);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>( HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/changePassword")
    public ResponseEntity<HttpStatus> changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO) throws Exception {
        boolean success = authService.changePassword(passwordChangeDTO);
        if (!success) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
