package tim2.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tim2.auth.service.UserService;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping(value = "/dash")
public class AdminController {

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @PreAuthorize("hasAuthority('MODIFY_USER')")
    @RequestMapping(method = RequestMethod.GET, value = "/verify/{id}")
    public ResponseEntity<HttpStatus> rabbit(@PathVariable Long id) throws IOException, TimeoutException {
        if(userService.sendVerificationMail(id)) {
            logger.info("|SUCCESSS VERIFICATION ACCOUNT|");
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
