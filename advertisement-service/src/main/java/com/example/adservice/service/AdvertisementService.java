package com.example.adservice.service;

import com.example.adservice.dto.AdvertisementDTO;
import com.example.adservice.model.Advertisement;
import com.example.adservice.repository.AdvertisementRepository;
import com.example.adservice.repository.CarRepository;
import com.example.adservice.repository.ImageRepository;
import com.example.adservice.repository.PricelistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class AdvertisementService {

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PricelistRepository pricelistRepository;

    public List<AdvertisementDTO> search(String start, String end, String city){
        Date startDate = new Date(Long.parseLong(start));
        Date endDate = new Date(Long.parseLong(end));
        List<Advertisement> ads = advertisementRepository.findAllByCityIgnoreCaseAndStartDateLessThanEqualAndEndDateGreaterThanEqual(city, startDate, endDate);
        List<AdvertisementDTO> ret = new ArrayList<>();
        for (Advertisement a: ads) {
            ret.add(new AdvertisementDTO(a));
        }
        return ret;
    }
}
