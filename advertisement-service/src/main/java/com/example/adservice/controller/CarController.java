package com.example.adservice.controller;

import com.example.adservice.datavalidation.RegularExpressions;
import com.example.adservice.dto.CarDTO;
import com.example.adservice.model.Car;
import com.example.adservice.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/car")
public class CarController {

    @Autowired
    private CarService carService;

    @GetMapping
    public ResponseEntity<List<CarDTO>> getAllCars() {
        List<CarDTO>retValue = carService.getAllCars();
        if(retValue.size()!=0){
            return new ResponseEntity<>(retValue, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<CarDTO> getOneCar(@PathVariable Long id) {
        RegularExpressions regularExpressions = new RegularExpressions();
        if(regularExpressions.idIdValid(id)) {
            CarDTO c = carService.getOne(id);
            return new ResponseEntity<>(c, HttpStatus.OK);
        }
        else{
            return  new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

}
