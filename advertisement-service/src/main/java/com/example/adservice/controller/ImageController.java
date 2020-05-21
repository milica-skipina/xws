package com.example.adservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/image")
public class ImageController {


    @RequestMapping(method = RequestMethod.POST, produces = "application/json", consumes = "application/json", value = "/addImages/{id}")
    public ResponseEntity<HttpStatus> addImages(@RequestBody String[] images, @PathVariable Long id) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value =  "/{id}")
    public ResponseEntity<HttpStatus>deleteImage(@Valid @PathVariable Long id){
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
