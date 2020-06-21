package com.example.adservice.service;

import com.example.adservice.dto.CarDTO;
import com.example.adservice.dto.CarOrderDTO;
import com.example.adservice.model.Advertisement;
import com.example.adservice.model.Car;
import com.example.adservice.model.Image;
import com.example.adservice.model.Review;
import com.example.adservice.repository.CarRepository;
import com.example.adservice.repository.ImageRepository;
import com.example.adservice.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public void addImages(Long id, String [] images){
        Car car = carRepository.findOneById(id);
        for(String s : images){
            Image i = new Image();
            i.setOwner(car);
            i.setImageUrl(s);
            imageRepository.save(i);
        }
        carRepository.save(car);
    }

    public boolean changeMileage(Double temp, Long id){
        Car car = carRepository.findOneById(id);
        Double pom = car.getMileage() + temp;
        car.setMileage(pom);
        carRepository.save(car);
        return true;
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

    public void updateRating(Long carId){
        Car car = carRepository.findOneById(carId);
        List<Review> reviews = reviewRepository.findAllByCarIdAndEvaluationGreaterThan(carId, 0);
        int sum = reviews.stream().filter(r -> r.getEvaluation() > 0).mapToInt(r -> r.getEvaluation()).sum();

        car.setRaiting(sum*1.0/reviews.size());
        carRepository.save(car);
    }

    public Car findOneById(Long id){
        return carRepository.findOneById(id);
    }

    public List<CarDTO> getStatistics(String username, String parameter){
        if(parameter.equals("comments")){
            return getMostCommented(username);
        }
        if(parameter.equals("rating")){
            return getBestRated(username);
        }
        return null;
    }

    public List<CarDTO> getMostCommented(String username){
        List<Car> cars = carRepository.findAllByEntrepreneurUsername(username);
        List<CarDTO> ret = new ArrayList<>();
        CarDTO carDTO;
        Long count;
        for(Car c: cars){
            count = reviewRepository.countAllByCarIdAndTextIsNotNullAndState(c.getId(), "APPROVED");
            carDTO = new CarDTO(c);
            carDTO.setCommentsNumber(count);
            ret.add(carDTO);
        }
        ret.sort(Comparator.comparing(CarDTO::getCommentsNumber).reversed());
        if(ret.size() > 5) {
            return ret.subList(0, 5);
        }
        return ret;
    }

    public List<CarDTO> getBestRated(String username){
        List<Car> cars = carRepository.findAllByEntrepreneurUsername(username);
        List<CarDTO> ret = new ArrayList<>();
        for(Car c:cars){
            ret.add(new CarDTO(c));
        }
        ret.sort(Comparator.comparing(CarDTO::getRaiting).reversed());
        if(ret.size() > 5) {
            return ret.subList(0, 5);
        }
        return ret;
    }

    public CarOrderDTO getOrder(Long id) {
        Car car = carRepository.getOne(id);
        CarOrderDTO ret = new CarOrderDTO(car);
        ret.setEntrepreneurName(((Advertisement) car.getCarAdvertisement().toArray()[0]).getEntrepreneurName());
        return ret;
    }
}
