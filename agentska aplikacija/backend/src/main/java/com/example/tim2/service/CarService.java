package com.example.tim2.service;

import com.example.tim2.dto.CarDTO;
import com.example.tim2.model.Car;
import com.example.tim2.model.Image;
import com.example.tim2.repository.CarRepository;
import com.example.tim2.repository.ImageRepository;
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

    public List<CarDTO> getAllCars(){
        List<Car> cars = carRepository.findAll();
        List<CarDTO>retValue = new ArrayList<CarDTO>();
        for(Car c : cars){
            retValue.add(new CarDTO(c));
        }
        return retValue;
    }

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

}
