package com.example.tim2.repository;

import com.example.tim2.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAll();

    Request findOneById(Long id);

    Request save(Request request);

    List<Request> findAllByState(String state);

    List<Request> findAllByUserId(Long id);

    List<Request> findAllByEntrepreneurId(Long id);

    List<Request> findAllByUserUsername(String customerUsername);

    List<Request> findAllByStateAndStartDateLessThanEqualAndEndDateGreaterThanEqual(String state, Date startDate, Date endDate);

    List<Request> findAllByUserIdAndEndDateLessThanEqualAndState(Long id, Date endDate, String state);

    List<Request> findAllByUserUsernameAndEntrepreneurUserUsernameAndState(String endUsername,String enterUsername,String state);

    List<Request> findAllByStartDateGreaterThanEqualOrEndDateLessThanEqualAndUserUsername(String customerUsername, Date start, Date end);
}
