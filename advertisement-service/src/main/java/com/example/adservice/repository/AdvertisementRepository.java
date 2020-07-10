package com.example.adservice.repository;

import com.example.adservice.dto.AdvertisementDTO;
import com.example.adservice.model.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

    List<Advertisement> findAllByState(String state);

    Advertisement findOneById(Long id);

    boolean deleteAdvertisementById(Long id);

    Advertisement save(Advertisement advertisement);

    Advertisement findOneByCarAdIdAndState(Long id, String state);

    List<Advertisement> findAllByDeletedAndStateAndCityIgnoreCaseAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Boolean deleted, String state, String city, Date startDate, Date endDate);

    List<Advertisement> findAllByEntrepreneurUsername(String agentUsername);
}

