package com.example.adservice.repository;

import com.example.adservice.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review,Long> {

    List<Review> findAllByCarIdAndEvaluationGreaterThan(Long id, Integer evaluation);

    List<Review> findAllByStateAndCarId(String state, Long id);

    List<Review> findAllByState(String state);

    Review findOneById(Long id);

    List<Review> findAllByCarIdAndTextIsNotNull(Long id);

    Long countAllByCarIdAndTextIsNotNullAndState(Long id, String state);

    Long countAllByUsernameAndCarId(String username, Long id);

}
