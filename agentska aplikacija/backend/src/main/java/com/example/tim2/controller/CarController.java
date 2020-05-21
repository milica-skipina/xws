package com.example.tim2.controller;

import com.example.tim2.dto.CarDTO;
import com.example.tim2.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/car")
public class CarController {

    @Autowired
    private CarService carService;

    @GetMapping(value = "/getAllCars")
    public ResponseEntity<List<CarDTO>> getAllCaCerts() {

        // String token = tokenUtils.getToken(request);
        //  String email = tokenUtils.getUsernameFromToken(token);
        //  User user = userService.findOneByEmail(email);
        // if (user != null) {
        List<CarDTO>retValue = carService.getAllCars();
        if(retValue.size()!=0){
            return new ResponseEntity<>(retValue, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
