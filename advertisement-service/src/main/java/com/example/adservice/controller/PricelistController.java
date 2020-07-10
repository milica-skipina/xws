package com.example.adservice.controller;

import com.example.adservice.config.TLSConfiguration;
import com.example.adservice.config.TokenUtils;
import com.example.adservice.datavalidation.RegularExpressions;
import com.example.adservice.dto.PricelistDTO;
import com.example.adservice.model.Pricelist;
import com.example.adservice.service.AdvertisementService;
import com.example.adservice.service.PricelistService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/pricelist")
public class PricelistController {


    @Autowired
    private PricelistService pricelistService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    AdvertisementService advertisementService;

    private static final Logger logger = LoggerFactory.getLogger(PricelistController.class);

    //@PreAuthorize("hasAuthority('WRITE_PRICE')")
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<HttpStatus> addP(@RequestBody PricelistDTO p, HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        String username = tokenUtils.getUsernameFromToken(token);
        Pricelist ok = pricelistService.addPricelist(p, username);
        if(ok==null) {
            // nije prosla validacija
            logger.error("user " + username + " tried to add pricelist: ");
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        else{
            logger.info("|ADD PRICELIST|user " + username + " pricelist id: " + ok.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    //@PreAuthorize("hasAuthority('WRITE_PRICE')")
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
        }else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    //@PreAuthorize("hasAuthority('EDIT_PRICE')")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json")
    public ResponseEntity<PricelistDTO> editPricelist(@Valid @RequestBody PricelistDTO p, @PathVariable Long id, HttpServletRequest request) {
        RegularExpressions regularExpressions = new RegularExpressions();
        String token = tokenUtils.getToken(request);
        String username = tokenUtils.getUsernameFromToken(token);
        if(regularExpressions.idIdValid(id)){
            PricelistDTO ok = pricelistService.editPricelist(p, id, username);
            final String url = TLSConfiguration.URL + "orders/advertisement/{id}";
            Map<String, Long> params = new HashMap<String, Long>();
            params.put("id", ok.getId());
            HttpEntity<PricelistDTO> sentTemp = new HttpEntity<PricelistDTO>(p);
            ResponseEntity<Boolean> result = restTemplate.exchange(url, HttpMethod.PUT, sentTemp, Boolean.class, params);
            if(ok == null) {
                // nije prosla validacija
                logger.info("user " + username + " tried to edit pricelist id: " + id);
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            else{
                logger.info("|EDIT PRICELIST|user " + username + " pricelist id: " + id);
                return new ResponseEntity<>(ok,HttpStatus.OK);
            }
        }
        else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    //@PreAuthorize("hasAuthority('READ_PRICE')")
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

    //@PreAuthorize("hasAuthority('READ_PRICE')")
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
