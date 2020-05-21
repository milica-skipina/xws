package com.example.tim2.controller;

import com.example.tim2.dto.PricelistDTO;
import com.example.tim2.model.Pricelist;
import com.example.tim2.repository.PricelistRepository;
import com.example.tim2.service.PricelistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/pricelist")
public class PricelistController {

    @Autowired
    private PricelistService pricelistService;

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<HttpStatus> addP(@RequestBody PricelistDTO p) {
        // provjera ulogovanog

        boolean ok = pricelistService.addPricelist(p);
        if(!ok) {
            // nije prosla validacija
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        else{
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @GetMapping(value = "/getAll")
    public ResponseEntity<List<PricelistDTO>> getAllPricelists() {

        // String token = tokenUtils.getToken(request);
        //  String email = tokenUtils.getUsernameFromToken(token);
        //  User user = userService.findOneByEmail(email);
        // if (user != null) {
        List<PricelistDTO>retValue = pricelistService.getAll();
        if(retValue.size()!=0){
            return new ResponseEntity<>(retValue, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
