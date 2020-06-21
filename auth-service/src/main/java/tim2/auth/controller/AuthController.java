package tim2.auth.controller;

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
import tim2.auth.authentication.JwtAuthenticationRequest;
import tim2.auth.common.UserIdentifier;
import tim2.auth.dto.RegistrationDTO;
import tim2.auth.model.Role;
import tim2.auth.model.User;
import tim2.auth.model.UserTokenState;
import tim2.auth.security.TokenUtils;
import tim2.auth.service.AuthService;
import tim2.auth.service.MessageProducer;

import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    private AuthService authService;

    private TokenUtils tokenUtils;

    private AuthenticationManager authenticationManager;

    private UserIdentifier userIdentifier;

    private MessageProducer messageProducer;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(AuthService authService, TokenUtils tokenUtils, AuthenticationManager authenticationManager,
                          UserIdentifier userIdentifier,MessageProducer messageProducer) {
        this.authService = authService;
        this.tokenUtils = tokenUtils;
        this.authenticationManager = authenticationManager;
        this.userIdentifier = userIdentifier;
        this.messageProducer = messageProducer;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", value = "/login")
    public ResponseEntity<UserTokenState> loginUser(@RequestBody JwtAuthenticationRequest authenticationRequest,
                                                    HttpServletResponse response) throws AuthenticationException, IOException, TimeoutException {
        User loggedUser = authService.loginUser(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        if (loggedUser != null) {
            final Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
                            authenticationRequest.getPassword()));

            // Ubaci username + password u kontext
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = (User) authentication.getPrincipal();
            String name = userIdentifier.findByUserId(user.getId());
            String jwt = tokenUtils.generateToken(user.getUsername(), user.getAuthorities(), name);
            int expiresIn = tokenUtils.getExpiredIn();
            String role = ((Role) user.getRoles().toArray()[0]).getName();
            logger.info("SUCCESS | user with username: " + authenticationRequest.getUsername() + " logged in" );
            return ResponseEntity.ok(new UserTokenState(jwt, expiresIn, role, name ));
        } else {
            String message = "Username and/or password is invalid.";
            logger.error(" |FAILED| user with username: " + authenticationRequest.getUsername() + " tried to log in" );
            return new ResponseEntity<UserTokenState>(new UserTokenState(message, 0, "", "" ), HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<HttpStatus> register(@RequestBody RegistrationDTO u) throws Exception {
        User user = authService.registerUser(u);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        logger.info(" |NEW REGISTRATION| " );
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ENDUSER') or hasRole('AGENT') or hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.PUT, value = "/reset/{id}")
    public ResponseEntity<HttpStatus> forgotPassword(@PathParam("id") Long id) throws Exception {
        boolean success = authService.forgotPassword(id);
        if (!success) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ENDUSER') or hasRole('SELLER')")
    @RequestMapping(method = RequestMethod.PUT, value = "/change/{id}")
    public ResponseEntity<HttpStatus> changePassword(@PathParam("id") Long id) throws Exception {
        boolean success = authService.changePassword(id);
        if (!success) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('ROLE_SELLER')")
    @RequestMapping(method = RequestMethod.POST, value = "/manual", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Boolean> manualRegistration(@RequestBody String[] data) {
        boolean success = authService.manualRegistration(data);
        if (!success) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        logger.info(" |NEW MANUAL REGISTRATION| " );
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
