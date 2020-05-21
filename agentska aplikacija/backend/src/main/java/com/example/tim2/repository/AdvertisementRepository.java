package com.example.tim2.repository;

import com.example.tim2.model.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

    List<Advertisement> findAll();

    Advertisement findOneById(Long id);

    Advertisement save(Advertisement advertisement);

   @Query("select a from Advertisement a where a.startDate <= :startDate and a.endDate >= :endDate " +
            "and a.city like %:city%")

    List<Advertisement> findAllByCityIgnoreCaseAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            String city, Date startDate, Date endDate);
}
