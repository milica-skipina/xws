package com.example.adservice.repository;


import com.example.adservice.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    Car save(Car car);

    List<Car> findAll();

    Car findOneById(Long id);

    boolean deleteCarById(Long id);

    List<Car> findAllByEntrepreneurUsername(String username);
}
