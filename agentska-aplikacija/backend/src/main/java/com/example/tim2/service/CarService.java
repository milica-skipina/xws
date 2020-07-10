package com.example.tim2.service;

import com.example.tim2.dto.CarDTO;
import com.example.tim2.model.*;
import com.example.tim2.repository.*;
import com.example.tim2.soap.clients.AdvertisementClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private AdvertisementClient advertisementClient;

    @Autowired
    private EndUserRepository endUserRepository;


    public List<CarDTO> getAllCars(){
        List<Car> cars = carRepository.findAll();
        List<CarDTO>retValue = new ArrayList<>();
        for(Car c : cars){
            retValue.add(new CarDTO(c));
        }
        return retValue;
    }

    public void addImages(Long id, String [] images){
        Car car = carRepository.findOneById(id);
        for(String s : images){
            Image i = new Image();
            i.setOwner(car);
            i.setImageUrl(s);
            imageRepository.save(i);
        }
        try{
            advertisementClient.addImages(car.getMicroId(), images);
        }catch (Exception e){
            e.printStackTrace();
        }
        carRepository.save(car);

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

    public List<CarDTO> getStatistics(String parameter){
        if(parameter.equals("comments")){
            return getMostCommented();
        }
        if(parameter.equals("rating")){
            return getBestRated();
        }
        if(parameter.equals("crossed")){
            return getMostCrossed();
        }
        return null;
    }

    private List<CarDTO> getMostCommented(){
        List<Car> cars = carRepository.findAll();
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

    private List<CarDTO> getBestRated(){
        List<Car> cars = carRepository.findAll();
        List<CarDTO> ret = new ArrayList<>();
        for(Car c:cars){
            ret.add(new CarDTO(c));
        }
        ret.sort(Comparator.comparing(CarDTO::getRating).reversed());
        if(ret.size() > 5) {
            return ret.subList(0, 5);
        }
        return ret;
    }

    private List<CarDTO> getMostCrossed(){
        List<Car> cars = carRepository.findAll();
        List<CarDTO> ret = new ArrayList<>();
        List<Report> reports = new ArrayList<>();
        Double sum = 0.0;
        CarDTO carDTO;
        for(Car c: cars){
            reports = reportRepository.findAllByReportCarId(c.getId());
            carDTO = new CarDTO(c);
            sum = reports.stream().filter(r -> r.getMileage() > 0).mapToDouble(r -> r.getMileage()).sum();
            carDTO.setMileageLimit(sum);
            ret.add(carDTO);
        }
        ret.sort(Comparator.comparing(CarDTO::getMileageLimit).reversed());
        if(ret.size() > 5) {
            return ret.subList(0, 5);
        }
        return ret;
    }

    public Boolean canWriteReview(String username, Long id){
        Long reviews = reviewRepository.countAllByUsernameAndCarId(username, id);
        User user = userRepository.findOneByUsername(username);
        EndUser endUser = endUserRepository.findByUserUsername(username);
        if(!endUser.isCanComment()){
            return false;
        }
        Date endDate = new Date();
        Long requestsNum = 0L;
        List<Request> requests = requestRepository.findAllByUserIdAndEndDateLessThanEqualAndState(user.getId(), endDate, "PAID");
        for(Request r : requests){
            for(Car c : r.getCars()){
                if(c.getId() == id){
                    requestsNum++;
                }
            }
        }
        if(reviews < requestsNum){
            return true;
        }
        else {
            return false;
        }
    }

}
