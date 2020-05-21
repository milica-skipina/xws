package com.example.tim2.repository;

import com.example.tim2.model.Codebook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CodebookRepository extends JpaRepository<Codebook, Long> {

    List<Codebook> findAll();

    Codebook findOneById(Long id);

    Codebook save(Codebook cb);

    Codebook findOneByNameAndCodeTypeAndDeleted(String name, String codeType, Boolean deleted);

    List<Codebook> findAllByDeleted(Boolean deleted);
}
