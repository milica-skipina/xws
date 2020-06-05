package com.example.tim2.controller;

import com.example.tim2.datavalidation.RegularExpressions;
import com.example.tim2.dto.PricelistDTO;
import com.example.tim2.service.PricelistService;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/pricelist")
public class PricelistController {

    @Autowired
    private PricelistService pricelistService;

    @PreAuthorize("hasAuthority('WRITE_PRICE')")
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<PricelistDTO> addP(@RequestBody PricelistDTO p) {
        // provjera ulogovanog

        PricelistDTO ok = pricelistService.addPricelist(p);
        System.out.println(p.getDiscount20() + "    " + p.getDiscount30());
        if(ok == null) {
            // nije prosla validacija
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        else{
            return new ResponseEntity<>(ok, HttpStatus.OK);
        }
    }

    @PreAuthorize("hasAuthority('WRITE_PRICE')")
    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json", produces="application/json", value="/{id}")
    public ResponseEntity<PricelistDTO> editPr(@RequestBody PricelistDTO p, @PathVariable Long id) {

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
        }else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    @PreAuthorize("hasAuthority('READ_PRICE')")
    @GetMapping()
    public ResponseEntity<List<PricelistDTO>> getAllPricelists() {

        List<PricelistDTO>retValue = pricelistService.getAll();
        if(retValue.size()!=0){
            return new ResponseEntity<>(retValue, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasAuthority('WRITE_PRICE')")
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
        }
        else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }
}
