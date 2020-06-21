package com.example.tim2.repository;

import com.example.tim2.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {

    List<Review> findAllByCarIdAndEvaluationGreaterThan(Long id, Integer evaluation);

    List<Review> findAllByStateAndCarId(String state, Long id);

    List<Review> findAllByState(String state);

    Review findOneById(Long id);

    Long countAllByUsernameAndCarId(String username, Long id);

    Long countAllByCarIdAndTextIsNotNullAndState(Long id, String state);
}
