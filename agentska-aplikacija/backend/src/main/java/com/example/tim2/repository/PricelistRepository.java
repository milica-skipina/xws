package com.example.tim2.repository;

import com.example.tim2.model.Pricelist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PricelistRepository  extends JpaRepository<Pricelist, Long> {

    List<Pricelist> findAll();

    Pricelist findOneById(Long id);

    Pricelist save(Pricelist p);
}
