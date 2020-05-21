package com.example.adservice.controller;

import com.example.adservice.dto.CarDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/car")
public class CarController {


    @GetMapping
    public ResponseEntity<List<CarDTO>> getAllCars() {

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CarDTO> getOneCar() {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT ,value = "/{id}",  produces = "application/json", consumes = "application/json")
    public ResponseEntity<HttpStatus>editCar(@RequestBody CarDTO car, @PathVariable Long id){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.DELETE ,value = "/{id}")
    public ResponseEntity<HttpStatus>deleteCar(@Valid @PathVariable Long id){
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
