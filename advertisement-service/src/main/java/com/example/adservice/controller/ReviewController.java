package com.example.adservice.controller;

import com.example.adservice.dto.ReviewDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/review")
public class ReviewController {

    @PostMapping(consumes = "application/json")
    public ResponseEntity<HttpStatus> addReview(@RequestBody ReviewDTO reviewDTO) {

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ReviewDTO> getReview(@PathVariable Long id) {

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
