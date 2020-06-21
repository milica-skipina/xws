package com.example.tim2.controller;

import com.example.tim2.datavalidation.RegularExpressions;
import com.example.tim2.dto.PricelistDTO;
import com.example.tim2.security.TokenUtils;
import com.example.tim2.service.PricelistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/pricelist")
public class PricelistController {

    @Autowired
    private PricelistService pricelistService;

    @Autowired
    private TokenUtils tokenUtils;

    private static final Logger logger = LoggerFactory.getLogger(PricelistController.class);

    @PreAuthorize("hasAuthority('WRITE_PRICE')")
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<PricelistDTO> addP(@RequestBody PricelistDTO p, HttpServletRequest request) {
        // provjera ulogovanog
        String token = tokenUtils.getToken(request);
        String username = tokenUtils.getUsernameFromToken(token);
        PricelistDTO ok = pricelistService.addPricelist(p,username);
        System.out.println(p.getDiscount20() + "    " + p.getDiscount30());
        if(ok == null) {
            // nije prosla validacija
            logger.error("user " + username + " tried to add pricelist: ");
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        else{
            logger.info("|ADD PRICELIST|user " + username + " pricelist id: " + ok.getId());
            return new ResponseEntity<>(ok, HttpStatus.OK);
        }
    }

    @PreAuthorize("hasAuthority('WRITE_PRICE')")
    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json", produces="application/json", value="/{id}")
    public ResponseEntity<PricelistDTO> editPr(@RequestBody PricelistDTO p, @PathVariable Long id, HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        String username = tokenUtils.getUsernameFromToken(token);
        RegularExpressions regularExpressions = new RegularExpressions();
        if(regularExpressions.idIdValid(id)){
            PricelistDTO ok = pricelistService.editPricelist(p, id,username);
            if(ok == null) {
                // nije prosla validacija
                logger.info("user " + username + " tried to edit pricelist id: " + id);
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            else{
                logger.info("|EDIT PRICELIST|user " + username + " pricelist id: " + id);
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
    public ResponseEntity<HttpStatus>deleteP(@PathVariable Long id, HttpServletRequest request){
        RegularExpressions regularExpressions = new RegularExpressions();
        String token = tokenUtils.getToken(request);
        String username = tokenUtils.getUsernameFromToken(token);
        if(regularExpressions.idIdValid(id)){
            boolean ok = pricelistService.deletePricelist(id);
            if(ok){
                logger.info("|DELETE PRICELIST|user " + username +" pricelist id: " + id);
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
