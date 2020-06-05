package com.example.adservice.controller;

import com.example.adservice.datavalidation.RegularExpressions;
import com.example.adservice.dto.PricelistDTO;
import com.example.adservice.model.Pricelist;
import com.example.adservice.service.PricelistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/pricelist")
public class PricelistController {


    @Autowired
    private PricelistService pricelistService;

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<HttpStatus> addP(@RequestBody PricelistDTO p) {
        boolean ok = pricelistService.addPricelist(p);
        if(!ok) {
            // nije prosla validacija
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        else{
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<HttpStatus>deleteP(@PathVariable Long id){
        RegularExpressions regularExpressions = new RegularExpressions();
        if(regularExpressions.idIdValid(id)){
            boolean ok = pricelistService.deletePricelist(id);
            if(ok){
                return new ResponseEntity<HttpStatus>(HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<PricelistDTO> editPricelist(@Valid @RequestBody PricelistDTO p, @PathVariable Long id) {
        RegularExpressions regularExpressions = new RegularExpressions();
        if(regularExpressions.idIdValid(id)){
            PricelistDTO ok = pricelistService.editPricelist(p, id);
            if(ok == null) {
                // nije prosla validacija
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            else{
                return new ResponseEntity<>(ok,HttpStatus.OK);
            }
        }
        else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<PricelistDTO> getOnePricelist(@PathVariable Long id) {
        RegularExpressions regularExpressions = new RegularExpressions();
        if(regularExpressions.idIdValid(id)) {
           PricelistDTO p = pricelistService.getOne(id);
            return new ResponseEntity<>(p, HttpStatus.OK);
        }
        else{
            return  new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping
    public ResponseEntity<List<PricelistDTO>> getAllPricelists() {
        List<PricelistDTO>retValue = pricelistService.getAll();
        if(retValue.size()!=0){
            return new ResponseEntity<>(retValue, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
