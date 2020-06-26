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
import tim2.auth.dto.PasswordChangeDTO;
import tim2.auth.dto.RegistrationDTO;
import tim2.auth.model.Role;
import tim2.auth.model.User;
import tim2.auth.model.UserTokenState;
import tim2.auth.repository.UserRepository;
import tim2.auth.security.TokenUtils;
import tim2.auth.service.AuthService;
import tim2.auth.service.MessageProducer;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private UserRepository userRepository;

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
    public ResponseEntity<UserTokenState> loginUser(@RequestBody JwtAuthenticationRequest authenticationRequest, HttpServletRequest request,
                                                    HttpServletResponse response) throws AuthenticationException, IOException, TimeoutException {
        User loggedUser = authService.loginUser(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        if (loggedUser != null) {
            if(!loggedUser.isDeleted()) {
                if(!loggedUser.isBlocked()) {
                    if(loggedUser.isActivated()) {
                        try {
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
                            logger.info("SUCCESS | user with username: " + authenticationRequest.getUsername() + " logged in " + "IP: " + request.getRemoteAddr() + " HOST: " + request.getRemoteHost() + " PORT: " + request.getRemotePort());
                            return ResponseEntity.ok(new UserTokenState(jwt, expiresIn, role, name));
                        } catch (Exception e) {
                            e.printStackTrace();
                            String message = "Bad credentials.";
                            loggedUser.setNumberFailedLogin(loggedUser.getNumberFailedLogin() + 1);
                            userRepository.save(loggedUser);
                            logger.error(" |FAILED| user with username: " + authenticationRequest.getUsername() + " tried to log in " + "IP: " + request.getRemoteAddr() + " HOST: " + request.getRemoteHost() + " PORT: " + request.getRemotePort() + " number of failed logins: " + loggedUser.getNumberFailedLogin());
                            return new ResponseEntity<UserTokenState>(new UserTokenState(message, 0, "", ""), HttpStatus.NOT_FOUND);
                        }
                    }else{
                        String message = "Account is not activated.";
                        logger.error(" |FAILED| user with username: " + authenticationRequest.getUsername() + " tried to log in " + "IP: " + request.getRemoteAddr() + " HOST: " + request.getRemoteHost() + " PORT: " + request.getRemotePort() + " Account not activated");
                        return new ResponseEntity<UserTokenState>(new UserTokenState(message, 0, "", ""), HttpStatus.FORBIDDEN);

                    }
                }else{
                    String message = "User is blocked by administrator.";
                    logger.error(" |FAILED| user with username: " + authenticationRequest.getUsername() + " tried to log in " + "IP: " + request.getRemoteAddr() + " HOST: " + request.getRemoteHost() + " PORT: " + request.getRemotePort() + " User is blocked" );
                    return new ResponseEntity<UserTokenState>(new UserTokenState(message, 0, "", ""), HttpStatus.FORBIDDEN);
                }
            }else{
                String message = "User account is deleted by administrator.";
                logger.error(" |FAILED| user with username: " + authenticationRequest.getUsername() + " tried to log in " + "IP: " + request.getRemoteAddr() + " HOST: " + request.getRemoteHost() + " PORT: " + request.getRemotePort() + " User account is deleted");
                return new ResponseEntity<UserTokenState>(new UserTokenState(message, 0, "", ""), HttpStatus.FORBIDDEN);
            }
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

    @PostMapping(value = "/forgotPassword")
    public ResponseEntity<HttpStatus> forgotPassword(@RequestBody String[] email ) {
        if(authService.accountRecovery(email[0]))
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('ROLE_CUSTOMER') or hasRole('ROLE_SELLER') or hasRole('ROLE_ADMIN')")
    @RequestMapping(method = RequestMethod.PUT, value = "/changePassword")
    public ResponseEntity<HttpStatus> changePassword(@RequestBody PasswordChangeDTO passwordChangeDTO) throws Exception {
        boolean success = authService.changePassword(passwordChangeDTO);
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

    @RequestMapping(method = RequestMethod.GET, value = "/verifyAgent", produces = "application/json")
    public ResponseEntity<String> verifyAgent(HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        String username = tokenUtils.getUsernameFromToken(token);

        User user = userRepository.findOneByUsername(username);
        if (user != null) {
            String name = userIdentifier.findByUserId(user.getId());
            String jwt = tokenUtils.generateToken(user.getUsername(), user.getAuthorities(), name);
            return new ResponseEntity<>(jwt, HttpStatus.OK);
        }
        //logger.info(" |NEW MANUAL REGISTRATION| " );
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
