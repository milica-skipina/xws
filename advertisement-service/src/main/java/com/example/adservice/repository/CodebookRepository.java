package com.example.adservice.repository;

import com.example.adservice.model.Codebook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CodebookRepository extends JpaRepository<Codebook, Long> {

    List<Codebook> findAll();

    Codebook findOneById(Long id);

    Codebook save(Codebook cb);

    boolean deleteCodebookById(Long id);

    Codebook findOneByNameAndCodeTypeAndDeleted(String name, String codeType, Boolean deleted);

    Codebook findOneByNameAndCodeTypeAndDeletedAndCode(String name, String codeType, Boolean deleted, String code);

    List<Codebook> findAllByDeleted(Boolean deleted);

}
