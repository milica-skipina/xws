package tim2.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tim2.auth.service.AdminService;

import javax.websocket.server.PathParam;

@RestController
@RequestMapping(value = "/dash")
public class AdminController {

    @Autowired
    private AdminService adminService;
    /**
     * odobrava tj. registruje agente na aplikaciju
     * @param id - id agenta kojeg je potrebno registrovati
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", value = "/approve/{id}")
    public ResponseEntity<HttpStatus> approveRequest(@PathParam("id") Long id)
    {
        boolean approved = adminService.acceptRequest(id);
        if (approved) {
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    /**
     * banovanje usera iz nekog razloga, tipa logovao se vise od 3 puta
     * @param id
     * @return
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", value = "/block/{id}")
    public ResponseEntity<HttpStatus> blockUser(@PathParam("id") Long id)
    {
        boolean approved = adminService.blockUser(id);
        if (approved) {
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }
}
