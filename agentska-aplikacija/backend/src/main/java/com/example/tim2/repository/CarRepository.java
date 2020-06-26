package com.example.tim2.repository;

import com.example.tim2.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    Car save(Car car);

    List<Car> findAll();

    Car findOneById(Long id);


}
