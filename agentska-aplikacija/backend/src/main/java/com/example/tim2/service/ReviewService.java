package com.example.tim2.service;

import com.example.tim2.dto.ReviewDTO;
import com.example.tim2.model.Car;
import com.example.tim2.model.EndUser;
import com.example.tim2.model.Review;
import com.example.tim2.repository.EndUserRepository;
import com.example.tim2.repository.ReviewRepository;
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

    @Autowired
    private EndUserRepository endUserRepository;

    public ReviewDTO addReview(ReviewDTO reviewDTO, String username){
        Car car = carService.findOneById(reviewDTO.getCarId());
        if(car == null){
            return  null;
        }
        EndUser endUser = endUserRepository.findByUserUsername(username);
        if(!endUser.isCanComment()){
            return null;
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

    public ReviewDTO editState(Long id, ReviewDTO reviewDTO, String username){
        Review review = reviewRepository.findOneById(id);
        EndUser endUser = endUserRepository.findByUserUsername(review.getUsername());
        review.setState(reviewDTO.getState());
        review = reviewRepository.save(review);
        if(review.getState().equals("REJECTED")){
            endUser.setNumberRefusedComments(endUser.getNumberRefusedComments()+1);
            endUserRepository.save(endUser);
        }
        if(review == null){
            return  null;
        }
        return new ReviewDTO(review);
    }
}