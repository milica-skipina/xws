package com.example.tim2.controller;

import com.example.tim2.datavalidation.RegularExpressions;
import com.example.tim2.dto.AdvertisementDTO;
import com.example.tim2.dto.BasketDTO;
import com.example.tim2.model.Advertisement;
import com.example.tim2.model.Car;
import com.example.tim2.security.TokenUtils;
import com.example.tim2.service.AdvertisementService;
import com.example.tim2.service.CarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import javax.xml.datatype.DatatypeConfigurationException;
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

    private static final Logger logger = LoggerFactory.getLogger(AdvertisementController.class);

    @PreAuthorize("hasAuthority('WRITE_AD')")
    @RequestMapping(method = RequestMethod.POST, produces = "application/json", consumes = "application/json", value = "/addNew/{id}")
    public ResponseEntity<Car> addAdvertisement(@RequestBody AdvertisementDTO advertisement, @PathVariable Long id, HttpServletRequest request) throws DatatypeConfigurationException {
        RegularExpressions regularExpressions = new RegularExpressions();
        if(regularExpressions.idIdValid(id)){
            String token = tokenUtils.getToken(request);
            String username = tokenUtils.getUsernameFromToken(token);
            // provjera ulogovanog
            Car car = advertisementService.addNewAd(advertisement, id, username);
            if(car == null) {
                // nije prosla validacija
                logger.error("user " + username + " tried to add ad");
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            else{
                Advertisement ad = advertisementService.getByCar(car.getId());
                logger.info("|ADD AD| user: " + username + " ad id: " + ad.getId());
                return new ResponseEntity<>(car, HttpStatus.OK);
            }
        }
        else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    @PreAuthorize("hasAuthority('WRITE_AD')")
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", value = "/addAd/{id}/{id1}")
    public ResponseEntity<Car> addAd(@RequestBody AdvertisementDTO advertisement, @PathVariable Long id, @PathVariable Long id1, HttpServletRequest request) {
        RegularExpressions regularExpressions = new RegularExpressions();
        if(regularExpressions.idIdValid(id) && regularExpressions.idIdValid(id1)) {
            // provjera ulogovanog
            String token = tokenUtils.getToken(request);
            String username = tokenUtils.getUsernameFromToken(token);
            Advertisement ok = advertisementService.addAd(advertisement, id, id1);
            if(ok!=null) {
                // nije prosla validacija
                logger.error("user " + username + " tried to add ad");
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            else{
                logger.info("|ADD AD| user: " + username + ", ad id: " + advertisement.getId());
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        else{
            return  new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('DELETE_AD')")
    @DeleteMapping(value="/{id}")
    public ResponseEntity<HttpStatus>deleteAdvertisement(@PathVariable Long id, HttpServletRequest request){
        RegularExpressions regularExpressions = new RegularExpressions();
        if(regularExpressions.idIdValid(id)) {
            String token = tokenUtils.getToken(request);
            String username = tokenUtils.getUsernameFromToken(token);
            boolean ok = advertisementService.deleteAd(id);
            if(ok){
                logger.info("|DELETE AD|user: " + username + " ad id: " + id);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            else{
                logger.error("user " + username + " tried to deleted ad id: " + id);
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        }
        else{
            return  new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PreAuthorize("hasAuthority('MODIFY_AD')")
    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json", produces="application/json", value="/{adId}/{pId}")
    public ResponseEntity<AdvertisementDTO> editAd(@RequestBody AdvertisementDTO a, @PathVariable Long adId, @PathVariable Long pId, HttpServletRequest request) throws DatatypeConfigurationException {
        // provjera ulogovanog
        RegularExpressions regularExpressions = new RegularExpressions();
        if(regularExpressions.idIdValid(adId) && regularExpressions.idIdValid(pId)) {
            AdvertisementDTO ret = advertisementService.editAdvertisement(a,adId, pId, request);
            String token = tokenUtils.getToken(request);
            String username = tokenUtils.getUsernameFromToken(token);
            if(ret == null) {
                // nije prosla validacija
                logger.error("user " + username + " tried to edit ad id: " + adId);
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            else{
                logger.info("|EDIT AD| user " + username + " ad id: " + adId);
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
        if(username!=null){
            if(username.equals("prodavac")){
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else{
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping()
    public ResponseEntity<List<AdvertisementDTO>> getAllAds() {
        List<AdvertisementDTO>retValue = advertisementService.getAllAdvertisements();
        if(retValue.size() == 0){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
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
    public ResponseEntity<List<BasketDTO>> getAllInBasket(HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        String username = tokenUtils.getUsernameFromToken(token);
        List<BasketDTO>retValue = advertisementService.getAllInBasket(username);        // customer username
        if(retValue.size() == 0){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else{
            return new ResponseEntity<>(retValue, HttpStatus.OK);
        }
    }

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
    public ResponseEntity<HttpStatus> addImages(@RequestBody String[] images, @PathVariable Long id, HttpServletRequest request) {
        RegularExpressions regularExpressions = new RegularExpressions();
        if(regularExpressions.idIdValid(id)) {
            String token = tokenUtils.getToken(request);
            String username = tokenUtils.getUsernameFromToken(token);
            carService.addImages(id,images);
            logger.info("|ADD IMAGES|user " + username + " car id: " + id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json", value= "/search")
    public ResponseEntity<List<AdvertisementDTO>> searchAds(@QueryParam("startDate") String startDate,
                                                            @QueryParam("endDate") String endDate,
                                                            @QueryParam("city") String city, HttpServletRequest request){
        String token = tokenUtils.getToken(request);
        String username = tokenUtils.getUsernameFromToken(token);
        List<AdvertisementDTO> ret = advertisementService.search(startDate, endDate, city);
        if(ret == null){
            if(username.equals("")){
                logger.info("unauthorized user " + " searched ads with invalid data");
            }
            else{
                logger.info("user " + username + " searched ads with invalid data");
            }
            logger.info("user " + username + " searched ads with invalid data");
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        logger.info("user " + username + " searched ads");
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }

    @GetMapping(value = "/sync", produces = "application/json")
    public ResponseEntity<String> syncDatabase(){
        boolean ok = advertisementService.sync();
        if(ok){
            return new ResponseEntity<>("Databases are synchronized", HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>("An error occurred while syncing. For more details, contact administrator. ", HttpStatus.EXPECTATION_FAILED);
        }
    }
}
