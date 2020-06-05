package com.example.tim2.repository;

import com.example.tim2.model.EndUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EndUserRepository extends JpaRepository<EndUser, Long> {

    EndUser findByUserId(Long id);

    EndUser findOneById(Long id);

    List<EndUser> findAll();
}
