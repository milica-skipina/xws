package com.example.tim2.controller;

import com.example.tim2.datavalidation.RegularExpressions;
import com.example.tim2.dto.AdvertisementDTO;
import com.example.tim2.dto.BasketDTO;
import com.example.tim2.model.Car;
import com.example.tim2.security.TokenUtils;
import com.example.tim2.service.AdvertisementService;
import com.example.tim2.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import java.util.List;

@RestController
@RequestMapping(value = "/advertisement")
public class AdvertisementController {

    @Autowired
    private AdvertisementService advertisementService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private CarService carService;

    @PreAuthorize("hasAuthority('WRITE_AD')")
    @RequestMapping(method = RequestMethod.POST, produces = "application/json", consumes = "application/json", value = "/addNew/{id}")
    public ResponseEntity<Car> addAdvertisement(@RequestBody AdvertisementDTO advertisement, @PathVariable Long id, HttpServletRequest request) {
        RegularExpressions regularExpressions = new RegularExpressions();
        if(regularExpressions.idIdValid(id)){
            // provjera ulogovanog
            Car car = advertisementService.addNewAd(advertisement, id, request);
            if(car == null) {
                // nije prosla validacija
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            else{
                return new ResponseEntity<>(car, HttpStatus.OK);
            }
        }
        else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    @PreAuthorize("hasAuthority('WRITE_AD')")
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", value = "/addAd/{id}/{id1}")
    public ResponseEntity<Car> addAd(@RequestBody AdvertisementDTO advertisement, @PathVariable Long id, @PathVariable Long id1) {
        RegularExpressions regularExpressions = new RegularExpressions();
        if(regularExpressions.idIdValid(id) && regularExpressions.idIdValid(id1)) {
            // provjera ulogovanog
            System.out.println(advertisement.getCity());
            boolean ok = advertisementService.addAd(advertisement, id, id1);
            if(!ok) {
                // nije prosla validacija
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            else{
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        else{
            return  new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    @PreAuthorize("hasAuthority('DELETE_AD')")
    @DeleteMapping(value="/{id}")
    public ResponseEntity<HttpStatus>deleteAdvertisement(@PathVariable Long id){
        RegularExpressions regularExpressions = new RegularExpressions();
        if(regularExpressions.idIdValid(id)) {
            boolean ok = advertisementService.deleteAd(id);
            if(ok){
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else{
            return  new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    @PreAuthorize("hasAuthority('MODIFY_AD')")
    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json", produces="application/json", value="/{adId}/{pId}")
    public ResponseEntity<AdvertisementDTO> editAd(@RequestBody AdvertisementDTO a, @PathVariable Long adId, @PathVariable Long pId, HttpServletRequest request) {
        // provjera ulogovanog
        RegularExpressions regularExpressions = new RegularExpressions();
        if(regularExpressions.idIdValid(adId) && regularExpressions.idIdValid(pId)) {
            AdvertisementDTO ret = advertisementService.editAdvertisement(a,adId, pId, request);
            if(ret == null) {
                // nije prosla validacija
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            else{
                return new ResponseEntity<>(ret,HttpStatus.OK);
            }
        }
        else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    @PreAuthorize("hasAuthority('CAN_ACCESS')")
    @RequestMapping(method = RequestMethod.GET, value = "/canAccess/{id}")
    public ResponseEntity<Car> canAccess(@PathVariable Long id, HttpServletRequest request) {
        RegularExpressions regularExpressions = new RegularExpressions();
        if(regularExpressions.idIdValid(id)) {
            boolean ok = advertisementService.canAccess(id, request);
            if(!ok) {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            else{
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        else{
            return  new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/isAgent")
    public ResponseEntity<Boolean> isAgent(HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        String username = tokenUtils.getUsernameFromToken(token);
        if(username.equals("prodavac")){
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    //@PreAuthorize("hasAuthority('READ_AD')") svi smeju da pretrazuju - znaci i da vide oglase
    @GetMapping()
    public ResponseEntity<List<AdvertisementDTO>> getAllAds() {
        List<AdvertisementDTO>retValue = advertisementService.getAllAdvertisements();
        if(retValue.size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else{
            return new ResponseEntity<>(retValue, HttpStatus.OK);
        }
    }

    /**
     * oglasi stavljeni u korpu
     * @return
     */
    @PreAuthorize("hasAuthority('READ_AD')")
    @PostMapping(value = "/filter", produces="application/json")
    public ResponseEntity<List<BasketDTO>> getAllInBasket(@RequestBody Long[] identifiers) {
        List<BasketDTO>retValue = advertisementService.getAllInBasket(identifiers);
        if(retValue.size() == 0){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else{
            return new ResponseEntity<>(retValue, HttpStatus.OK);
        }
    }

    @PreAuthorize("hasAuthority('READ_AD')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<AdvertisementDTO> getAd(@PathVariable Long id) {

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
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    @PreAuthorize("hasAuthority('WRITE_AD')")
    @RequestMapping(method = RequestMethod.POST, produces = "application/json", consumes = "application/json", value = "/addImages/{id}")
    public ResponseEntity<HttpStatus> addImages(@RequestBody String[] images, @PathVariable Long id) {
        RegularExpressions regularExpressions = new RegularExpressions();
        if(regularExpressions.idIdValid(id)) {
            carService.addImages(id,images);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json", value= "/search")
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
