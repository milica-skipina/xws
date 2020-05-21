package com.example.adservice.repository;

import com.example.adservice.model.Pricelist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PricelistRepository  extends JpaRepository<Pricelist, Long> {

    List<Pricelist> findAll();

    Pricelist findOneById(Long id);

    Pricelist save(Pricelist p);

    boolean deletePricelistById(Long id);
}
