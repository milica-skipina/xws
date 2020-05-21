package com.example.adservice.controller;

import com.example.adservice.dto.AdvertisementDTO;
import com.example.adservice.model.Car;
import com.example.adservice.service.AdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.QueryParam;
import java.util.List;

@RestController
@RequestMapping(value = "/advertisement")
public class AdvertisementController {

    @Autowired
    AdvertisementService advertisementService;

    @RequestMapping(method = RequestMethod.POST, produces = "application/json", consumes = "application/json", value = "/{id}")
    public ResponseEntity<Car> addAdvertisement(@RequestBody AdvertisementDTO advertisement, @PathVariable Long id) {

            return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", value = "/{id}/{id1}")
    public ResponseEntity<Car> addAd(@RequestBody AdvertisementDTO advertisement, @PathVariable Long id, @PathVariable Long id1) {

            return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<AdvertisementDTO>> getAllAds() {
            return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<AdvertisementDTO> getAd(@PathVariable Long id) {
            return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus>deleteAdvertisement(@PathVariable Long id){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT ,value = "/{id}",  produces = "application/json", consumes = "application/json")
    public ResponseEntity<HttpStatus>editAd(@RequestBody AdvertisementDTO ad, @PathVariable Long id){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/search", produces = "application/json")
    public ResponseEntity<List<AdvertisementDTO>> searchAds(@QueryParam("startDate") String startDate,
                                                            @QueryParam("endDate") String endDate,
                                                            @QueryParam("city") String city){
        List<AdvertisementDTO> ret = advertisementService.search(startDate, endDate, city);
        if(ret == null){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }

}
