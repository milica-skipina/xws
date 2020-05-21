package com.example.adservice.service;

import com.example.adservice.repository.AdvertisementRepository;
import com.example.adservice.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private AdvertisementRepository advertisementRepository;
}
