package com.example.adservice.controller;

import com.example.adservice.config.TokenUtils;
import com.example.adservice.datavalidation.RegularExpressions;
import com.example.adservice.dto.CarDTO;
import com.example.adservice.dto.CarOrderDTO;
import com.example.adservice.dto.CoordsDTO;
import com.example.adservice.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/car")
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private TokenUtils tokenUtils;

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

    @GetMapping(value = "/track/{id}", produces = "application/json")
    public ResponseEntity<CoordsDTO> carTrack(@PathVariable Long id, HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        CoordsDTO dto = carService.getCurrentCoord(id,token);
        if(dto != null)
            return new ResponseEntity<>(dto, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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

    //@PreAuthorize("hasAuthority('MODIFY_AD')")
    @PutMapping(value = "/changeMileage/{id}", consumes = "application/json")
    public ResponseEntity<Boolean> changeM(@PathVariable Long id, @RequestBody Double reportMileage){
        RegularExpressions regularExpressions = new RegularExpressions();
        if(regularExpressions.idIdValid(id)) {
            carService.changeMileage(reportMileage,id);
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        }else{
            return new ResponseEntity<Boolean>(false, HttpStatus.NOT_ACCEPTABLE);
        }
    }

   // @PreAuthorize("hasAuthority('READ_STATISTICS')")
    @GetMapping(produces="application/json", value="/statistics/{parameter}")
    public ResponseEntity<List<CarDTO>> getStatistics(@PathVariable String parameter, HttpServletRequest request){
        String token = tokenUtils.getToken(request);
        String username = tokenUtils.getUsernameFromToken(token);
        List<CarDTO> cars = carService.getStatistics(username, parameter);
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

   // @PreAuthorize("hasAuthority('READ_CAR')")
    @GetMapping(value = "/order/{carId}", produces = "application/json")
    public ResponseEntity<CarOrderDTO> getOrder(@PathVariable Long carId) {
        RegularExpressions regularExpressions = new RegularExpressions();
        if(regularExpressions.idIdValid(carId)) {
            CarOrderDTO c = carService.getOrder(carId);
            return new ResponseEntity<>(c, HttpStatus.OK);
        }
        else{
            return  new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

}
