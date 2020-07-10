package com.example.adservice.controller;

import com.example.adservice.config.TLSConfiguration;
import com.example.adservice.config.TokenUtils;
import com.example.adservice.datavalidation.RegularExpressions;
import com.example.adservice.dto.ReviewDTO;
import com.example.adservice.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private RestTemplate restTemplate;

    @PreAuthorize("hasAuthority('CREATE_REVIEW')")
    @PostMapping(consumes = "application/json")
    public ResponseEntity<ReviewDTO> addReview(@RequestBody ReviewDTO reviewDTO, HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        String username = tokenUtils.getUsernameFromToken(token);
        ReviewDTO ret = reviewService.addReview(reviewDTO, username);
        if(ret == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ReviewDTO> getReview(@PathVariable Long id) {
        RegularExpressions regularExpressions = new RegularExpressions();
        if(regularExpressions.idIdValid(id)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping(value="/carId/{id}")
    public ResponseEntity<List<ReviewDTO>> getReviewsByCarId(@PathVariable Long id){
        List<ReviewDTO> reviews = reviewService.getAllByStateAndCarId("APPROVED", id);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<ReviewDTO>> getReviews(HttpServletRequest request){
        List<ReviewDTO> reviews = reviewService.getAllByState("PENDING");
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('MODIFY_REVIEW')")
    @PutMapping(value="/{id}")
    public ResponseEntity<ReviewDTO> editReview(@PathVariable Long id, @RequestBody ReviewDTO reviewDTO, HttpServletRequest request){
        String token = tokenUtils.getToken(request);
        String username = tokenUtils.getUsernameFromToken(token);
        ReviewDTO ret = reviewService.editState(id, reviewDTO);
        final String url = TLSConfiguration.URL + "authpoint/user/changeState/{username}";
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        HttpEntity<String> sentTemp = new HttpEntity<String>(reviewDTO.getState());
        HttpEntity<Boolean> result = restTemplate.exchange(url, HttpMethod.PUT, sentTemp, Boolean.class, params);
        if(ret == null){
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }

    @GetMapping(value="/username/{username}/carId/{id}")
    public ResponseEntity<Long> getNumReviews(HttpServletRequest request, @PathVariable String username,
                                                      @PathVariable Long id){
        Long reviews = reviewService.countAllByUsernameAndCarId(username, id);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }
}
