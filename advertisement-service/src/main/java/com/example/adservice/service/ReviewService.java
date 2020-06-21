package com.example.adservice.service;

import com.example.adservice.dto.ReviewDTO;
import com.example.adservice.model.Advertisement;
import com.example.adservice.model.Car;
import com.example.adservice.model.Review;
import com.example.adservice.repository.CarRepository;
import com.example.adservice.repository.ReviewRepository;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CarService carService;

    public ReviewDTO addReview(ReviewDTO reviewDTO, String username){
        Car car = carService.findOneById(reviewDTO.getCarId());
        if(car == null){
            return  null;
        }
        reviewDTO.setText(Encode.forHtml(reviewDTO.getText()));
        Review review = new Review(reviewDTO.getEvaluation(), car, reviewDTO.getText(), username);
        review = reviewRepository.save(review);
        if(review.getEvaluation() != 0) {
            carService.updateRating(car.getId());
        }
        return new ReviewDTO(review);
    }

    public List<ReviewDTO> getAllByStateAndCarId(String state, Long id){
        List<Review> reviews = reviewRepository.findAllByStateAndCarId(state, id);
        List<ReviewDTO> ret = new ArrayList<>();
        for(Review r:reviews){
            ret.add(new ReviewDTO(r));
        }
        return ret;
    }

    public List<ReviewDTO> getAllByState(String state){
        List<Review> reviews = reviewRepository.findAllByState(state);
        List<ReviewDTO> ret = new ArrayList<>();
        for(Review r:reviews){
            ret.add(new ReviewDTO(r));
        }
        return ret;
    }

    public ReviewDTO editState(Long id, ReviewDTO reviewDTO){
        Review review = reviewRepository.findOneById(id);
        review.setState(reviewDTO.getState());
        review = reviewRepository.save(review);
        if(review == null){
            return  null;
        }
        return new ReviewDTO(review);
    }

    public Long countAllByUsernameAndCarId(String username, Long id){
        return reviewRepository.countAllByUsernameAndCarId(username, id);
    }
}
