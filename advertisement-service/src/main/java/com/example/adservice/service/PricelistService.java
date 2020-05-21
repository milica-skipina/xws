package com.example.adservice.service;

import com.example.adservice.repository.PricelistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PricelistService {

    @Autowired
    PricelistRepository pricelistRepository;

}
