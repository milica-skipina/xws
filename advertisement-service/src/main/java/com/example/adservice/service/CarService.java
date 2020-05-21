package com.example.adservice.service;

import com.example.adservice.repository.CarRepository;
import com.example.adservice.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ImageRepository imageRepository;
}
