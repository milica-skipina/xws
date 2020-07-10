package com.example.adservice.controller;

import com.example.adservice.config.TokenUtils;
import com.example.adservice.datavalidation.RegularExpressions;
import com.example.adservice.service.CarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/image")
public class ImageController {

    @Autowired
    private CarService carService;

    @Autowired
    private TokenUtils tokenUtils;

    private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    //@PreAuthorize("hasAuthority('WRITE_AD')")
    @RequestMapping(method = RequestMethod.POST, produces = "application/json", consumes = "application/json", value = "/{id}")
    public ResponseEntity<HttpStatus> addImages(@RequestBody String[] images, @PathVariable Long id, HttpServletRequest request) {
        RegularExpressions regularExpressions = new RegularExpressions();
        String token = tokenUtils.getToken(request);
        String username = tokenUtils.getUsernameFromToken(token);
        if(regularExpressions.idIdValid(id)) {
            carService.addImages(id,images);
            logger.info("|ADD IMAGES|user " + username + " car id: " + id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
