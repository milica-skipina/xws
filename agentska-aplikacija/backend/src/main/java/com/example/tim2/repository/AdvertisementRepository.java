package com.example.tim2.repository;

import com.example.tim2.model.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

    List<Advertisement> findAll();

    Advertisement findOneById(Long id);

    Advertisement findOneByCarAdId(Long id);

    Advertisement save(Advertisement advertisement);

   //@Query("select a from Advertisement a where a.startDate <= :startDate and a.endDate >= :endDate " +
          //  "and a.city like %:city%")

    List<Advertisement> findAllByDeletedAndCityIgnoreCaseAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            Boolean deleted, String city, Date startDate, Date endDate);

    //List<Advertisement> findAllByEntrepreneurIdAndIdEqual(Long entrepreneurId, Long id);

    List<Advertisement> findAllByMicroIdIsNull();
}
