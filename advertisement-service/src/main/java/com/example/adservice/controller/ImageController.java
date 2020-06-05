package com.example.adservice.controller;

import com.example.adservice.datavalidation.RegularExpressions;
import com.example.adservice.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/image")
public class ImageController {

    @Autowired
    private CarService carService;


    @RequestMapping(method = RequestMethod.POST, produces = "application/json", consumes = "application/json", value = "/{id}")
    public ResponseEntity<HttpStatus> addImages(@RequestBody String[] images, @PathVariable Long id) {
        RegularExpressions regularExpressions = new RegularExpressions();
        if(regularExpressions.idIdValid(id)) {
            carService.addImages(id,images);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
