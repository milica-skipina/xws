package com.example.adservice.controller;

import com.example.adservice.common.UserIdentifier;
import com.example.adservice.config.TokenUtils;
import com.example.adservice.datavalidation.RegularExpressions;
import com.example.adservice.dto.AdvertisementDTO;
import com.example.adservice.dto.BasketDTO;
import com.example.adservice.dto.SearchDTO;
import com.example.adservice.model.Car;
import com.example.adservice.service.AdvertisementService;
import com.netflix.ribbon.proxy.annotation.Http;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpEntity;


@RestController
@RequestMapping(value = "/advertisement")
public class AdvertisementController {

    @Autowired
    AdvertisementService advertisementService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private UserIdentifier userIdentifier;

    @RequestMapping(method = RequestMethod.POST, produces = "application/json", consumes = "application/json", value = "/{id}")
    public ResponseEntity<Car> addAdvertisement(@RequestBody AdvertisementDTO advertisement,
                                                @PathVariable Long id,
                                                HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        String username = tokenUtils.getUsernameFromToken(token);
        String name = tokenUtils.getNameFromToken(token);
        String role = tokenUtils.getRoleFromToken(token);
        RegularExpressions regularExpressions = new RegularExpressions();
        if(regularExpressions.idIdValid(id)){
            Car car = advertisementService.addNewAd(advertisement, id, username, name, role);
            if(car == null) {
                // nije prosla validacija
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            else{
                return new ResponseEntity<>(car, HttpStatus.OK);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

    }

    /**
     *
     * @param advertisement
     * @param id - id auta
     * @param id1 - id pricelista
     * @return
     */
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", value = "/{id}/{id1}")
    public ResponseEntity<Car> addAd(@RequestBody AdvertisementDTO advertisement, @PathVariable Long id,
                                     @PathVariable Long id1, HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        String username = tokenUtils.getUsernameFromToken(token);
        String name = tokenUtils.getNameFromToken(token);
        RegularExpressions regularExpressions = new RegularExpressions();
        if(regularExpressions.idIdValid(id) && regularExpressions.idIdValid(id1)) {
            boolean ok = advertisementService.addAd(advertisement, id, id1, username, name);
            if (!ok) {
                // nije prosla validacija
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            } else {
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }


    @GetMapping()
    public ResponseEntity<List<AdvertisementDTO>> getAllAds(HttpServletRequest request) {
        String role = userIdentifier.verifyUserRole(request);
        List<AdvertisementDTO>retValue = null;
        if (role == null ||  !role.equals("ROLE_SELLER")) {
            retValue = advertisementService.getAllAdvertisements();
        } else {
            String username = userIdentifier.verifyUser(request);
            retValue = advertisementService.allAdsForAgent(username);

        }

        if(retValue.size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
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
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @GetMapping(value = "/cadEndUserAdd")
    public ResponseEntity<Boolean> canEndUserAdd(HttpServletRequest request) {
        RegularExpressions regularExpressions = new RegularExpressions();
        String token = tokenUtils.getToken(request);
        String username = tokenUtils.getUsernameFromToken(token);
        String name = tokenUtils.getNameFromToken(token);
        String role = tokenUtils.getRoleFromToken(token);
        boolean retValue = advertisementService.canAdd(name,username,role);
        if(retValue){
            return new ResponseEntity<>(retValue, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus>deleteAdvertisement(@PathVariable Long id){
        RegularExpressions regularExpressions = new RegularExpressions();
        if(regularExpressions.idIdValid(id)){
            // vraca da li ima zahtjeva ili ne
            final String url = "https://localhost:8072/request/hasAd/{id}";
            Long carId = advertisementService.getCarByAdId(id);
            Map<String, Long> params = new HashMap<String, Long>();
            params.put("id", carId);
            boolean result = restTemplate.getForObject(url, Boolean.class, params);
            if(result){
                boolean ok = advertisementService.deleteAd(id);
                if(ok){
                    return new ResponseEntity<>(HttpStatus.OK);
                }else{
                    return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
                }
            }else{
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }
        else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(method = RequestMethod.PUT, consumes = "application/json", produces="application/json", value="/{adId}/{pId}")
    public ResponseEntity<AdvertisementDTO> editAd(@RequestBody AdvertisementDTO a, @PathVariable Long adId, @PathVariable Long pId,  HttpServletRequest request) {
        RegularExpressions regularExpressions = new RegularExpressions();
        if (regularExpressions.idIdValid(adId) && regularExpressions.idIdValid(pId)) {
            // vraca da li ima zahtjeva ili ne
            final String url = "https://localhost:8072/request/hasAd/{id}";
            Long carId = advertisementService.getCarByAdId(adId);
            Map<String, Long> params = new HashMap<String, Long>();
            params.put("id", carId);
            boolean result = restTemplate.getForObject(url, Boolean.class, params);
            if (result) {
                String token = tokenUtils.getToken(request);
                String username = tokenUtils.getUsernameFromToken(token);
                String name = tokenUtils.getNameFromToken(token);
                AdvertisementDTO ret = advertisementService.editAdvertisement(a, adId, pId, username, name);
                if (ret == null) {
                    // nije prosla validacija
                    return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
                } else {
                    return new ResponseEntity<>(ret, HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/canAccess/{id}")
    public ResponseEntity<Car> canAccess(@PathVariable Long id, HttpServletRequest request) {
        // sve to preko HttpServletRequesta, tu imamo username, a oglas ima username
        RegularExpressions regularExpressions = new RegularExpressions();
        if(regularExpressions.idIdValid(id)) {
            boolean ok = advertisementService.canAccess(id, request);
            if (!ok) {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            } else {
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
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

    //@PreAuthorize("hasAuthority('READ_AD')") , treba i za order
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


    @PostMapping(consumes = "application/json", produces="application/json", value="/search")
    public ResponseEntity<List<AdvertisementDTO>> addAd(@RequestBody SearchDTO search) {
        List<AdvertisementDTO> retValue = advertisementService.advancedSearch(search);
        return new ResponseEntity<>(retValue, HttpStatus.OK);
    }

}
