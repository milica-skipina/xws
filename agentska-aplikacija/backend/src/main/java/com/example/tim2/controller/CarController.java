package com.example.tim2.controller;

import com.example.tim2.dto.CarDTO;
import com.example.tim2.security.TokenUtils;
import com.example.tim2.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/car")
public class CarController {

    @Autowired
    private CarService carService;

    @Autowired
    private TokenUtils tokenUtils;

    @GetMapping()
    public ResponseEntity<List<CarDTO>> getCars() {

        List<CarDTO>retValue = carService.getAllCars();
        if(retValue.size()!=0){
            return new ResponseEntity<>(retValue, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAuthority('READ_STATISTICS')")
    @GetMapping(produces="application/json", value="/statistics/{parameter}")
    public ResponseEntity<List<CarDTO>> getStatistics(@PathVariable String parameter){
        List<CarDTO> cars = carService.getStatistics(parameter);
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }

    // TO DO - PROVERITI KO IMA PRAVO
    @PreAuthorize("hasAuthority('CREATE_REVIEW')")
    @GetMapping(produces="application/json", value="/{id}/writeReview")
    public ResponseEntity<Boolean> canWriteReview(@PathVariable Long id, HttpServletRequest request){
        String token = tokenUtils.getToken(request);
        String username = tokenUtils.getUsernameFromToken(token);
        boolean allowed = carService.canWriteReview(username, id);
        return new ResponseEntity<>(allowed, HttpStatus.OK);
    }

}
