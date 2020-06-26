package com.example.tim2.controller;

import com.example.tim2.datavalidation.RegularExpressions;
import com.example.tim2.dto.ReviewDTO;
import com.example.tim2.security.TokenUtils;
import com.example.tim2.service.ReviewService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping(value = "/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private TokenUtils tokenUtils;

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

    @PreAuthorize("hasAuthority('MODIFY_REVIEW')")
    @GetMapping()
    public ResponseEntity<List<ReviewDTO>> getReviews(){
        List<ReviewDTO> reviews = reviewService.getAllByState("PENDING");
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('MODIFY_REVIEW')")
    @PutMapping(value="/{id}")
    public ResponseEntity<ReviewDTO> editReview(@PathVariable Long id, @RequestBody ReviewDTO reviewDTO, HttpServletRequest request){
        String token = tokenUtils.getToken(request);
        String username = tokenUtils.getUsernameFromToken(token);
        ReviewDTO ret = reviewService.editState(id, reviewDTO, username);
        if(ret == null){
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }
}
