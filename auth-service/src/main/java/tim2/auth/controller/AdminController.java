package tim2.auth.controller;

import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tim2.auth.service.AdminService;
import tim2.auth.service.MessageProducer;
import tim2.auth.service.UserService;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping(value = "/dash")
public class AdminController {


    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET, value = "/verify/{id}")
   // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> rabbit(@PathParam("id") Long id) throws IOException, TimeoutException {
        System.out.println(id);
            long asd = 4;
        if(userService.sendVerificationMail(asd))
            return new ResponseEntity<>(HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
