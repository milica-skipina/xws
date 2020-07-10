package com.example.adservice.repository;

import com.example.adservice.model.Coords;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoordinatesRepository extends JpaRepository<Coords, Long> {

    Coords findOneById(Long id);

}
