package com.example.adservice.service;

import com.example.adservice.dto.CarDTO;
import com.example.adservice.dto.PricelistDTO;
import com.example.adservice.model.Car;
import com.example.adservice.model.Image;
import com.example.adservice.model.Pricelist;
import com.example.adservice.repository.CarRepository;
import com.example.adservice.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ImageRepository imageRepository;


    public void addImages(Long id, String [] images){
        Car car = carRepository.getOne(id);
        for(String s : images){
            Image i = new Image();
            i.setOwner(car);
            i.setImageUrl(s);
            imageRepository.save(i);
        }
        carRepository.save(car);
    }

    public List<CarDTO> getAllCars(){
        List<Car> cars = carRepository.findAll();
        List<CarDTO>retValue = new ArrayList<CarDTO>();
        for(Car c : cars){
            System.out.println(c.getMake().getCode());
            retValue.add(new CarDTO(c));
        }
        return retValue;
    }

    public CarDTO getOne(Long id){
        Car c = carRepository.findOneById(id);
        return new CarDTO(c);
    }

}
