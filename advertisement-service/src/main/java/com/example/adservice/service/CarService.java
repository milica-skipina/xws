package com.example.adservice.service;

import com.example.adservice.config.TLSConfiguration;
import com.example.adservice.dto.AdvertisementOrderDTO;
import com.example.adservice.dto.CarDTO;
import com.example.adservice.dto.CarOrderDTO;
import com.example.adservice.dto.CoordsDTO;
import com.example.adservice.model.*;
import com.example.adservice.repository.*;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private MessageProducer messageProducer;

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CoordinatesRepository coordinatesRepository;

    public CoordsDTO getCurrentCoord(Long carId,String token){

            final String url = TLSConfiguration.URL + "orders/car/trackingCheck/"+carId.toString();
            Map<String, Long> params = new HashMap<String, Long>();
            params.put("id",carId);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity header = new HttpEntity(headers);
           // HttpEntity header = createAuthHeader(token, null);
            ResponseEntity<Boolean> result = restTemplate.exchange(url, HttpMethod.GET, header, Boolean.class, params);

           // boolean result = restTemplate.getForObject(url,Boolean.class);

            if( result.getBody()) {
                long id = 1;
                Coords curr = coordinatesRepository.getOne(id);
                if (curr != null) {
                    CoordsDTO dto = new CoordsDTO(curr);
                    return dto;
                }
                CoordsDTO dto = new CoordsDTO();
                dto.setX(45.2464362);
                dto.setY(19.8517172);
                return dto;
            }
            return null;

    }

    public <T> HttpEntity<T> createAuthHeader(String token, T bodyType) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<T> request = new HttpEntity<>(bodyType, headers);
        return request;
    }

    public void addImages(Long id, String [] images){
        Car car = carRepository.findOneById(id);
        for(String s : images){
            Image i = new Image();
            i.setOwner(car);
            i.setImageUrl(s);
            i = imageRepository.save(i);
            car.getImages().add(i);
        }
        Car c = carRepository.save(car);
        Set<Advertisement> ads = c.getCarAdvertisement();
        for(Advertisement a:ads){
            a.setCarAd(c);
            messageProducer.send(new AdvertisementOrderDTO(a));
        }
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

    public void trackingSimulation(Long id) {
        String token = carRepository.findOneById(id).getTrackingToken();
        // ovaj token slati u rabita i prihvatati u tracking servisu
    }
}
