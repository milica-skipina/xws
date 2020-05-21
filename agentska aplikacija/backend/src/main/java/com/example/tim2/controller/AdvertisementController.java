package com.example.tim2.controller;

import com.example.tim2.dto.AdvertisementDTO;
import com.example.tim2.model.*;
import com.example.tim2.service.AdvertisementService;
import com.example.tim2.service.CarService;
import com.example.tim2.datavalidation.RegularExpressions;
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
    private AdvertisementService advertisementService;

    @Autowired
    private CarService carService;

    @RequestMapping(method = RequestMethod.POST, produces = "application/json", consumes = "application/json", value = "/addNew/{id}")
    public ResponseEntity<Car> addAdvertisement(@RequestBody AdvertisementDTO advertisement, @PathVariable Long id) {

        // provjera ulogovanog

        Car car = advertisementService.addNewAd(advertisement, id);
        if(car == null) {
            // nije prosla validacija
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        else{
            return new ResponseEntity<>(car, HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", value = "/addAd/{id}/{id1}")
    public ResponseEntity<Car> addAd(@RequestBody AdvertisementDTO advertisement, @PathVariable Long id, @PathVariable Long id1) {

        // provjera ulogovanog

        boolean ok = advertisementService.addAd(advertisement, id, id1);
        if(!ok) {
            // nije prosla validacija
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        else{
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    @GetMapping(value = "/getAllAds")
    public ResponseEntity<List<AdvertisementDTO>> getAllAds() {

        // String token = tokenUtils.getToken(request);
        //  String email = tokenUtils.getUsernameFromToken(token);
        //  User user = userService.findOneByEmail(email);
        // if (user != null) {
        List<AdvertisementDTO>retValue = advertisementService.getAllAdvertisements();
        if(retValue.size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity<>(retValue, HttpStatus.OK);
        }
    }

    @GetMapping(value = "/getOneAd/{id}")
    public ResponseEntity<AdvertisementDTO> getAd(@PathVariable Long id) {

        // String token = tokenUtils.getToken(request);
        //  String email = tokenUtils.getUsernameFromToken(token);
        //  User user = userService.findOneByEmail(email);
        // if (user != null) {

        RegularExpressions regularExpressions = new RegularExpressions();
        if(regularExpressions.idIdValid(id)){
            AdvertisementDTO retValue = advertisementService.getOneAd(id);
            if(retValue!=null){
                return new ResponseEntity<>(retValue, HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json", consumes = "application/json", value = "/addImages/{id}")
    public ResponseEntity<HttpStatus> addImages(@RequestBody String[] images, @PathVariable Long id) {
    carService.addImages(id,images);
    return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
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
