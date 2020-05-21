package tim2.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tim2.auth.model.User;
import tim2.auth.service.AuthService;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", value = "/login")
    public ResponseEntity<User> login(User u)
    {
        User user = authService.loginUser(u);
        if (user != null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<User> register(User u) throws Exception {
        User user = authService.registerUser(u);
        if (user == null) {
            return new ResponseEntity<User>(HttpStatus.EXPECTATION_FAILED);
        }
        return new ResponseEntity<User>(u, HttpStatus.OK);
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

    @PreAuthorize("hasRole('ENDUSER') or hasRole('AGENT')")
    @RequestMapping(method = RequestMethod.PUT, value = "/change/{id}")
    public ResponseEntity<HttpStatus> changePassword(@PathParam("id") Long id) throws Exception {
        boolean success = authService.changePassword(id);
        if (!success) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
