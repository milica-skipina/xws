package com.example.tim2.repository;

import com.example.tim2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAll();

    User findOneByUsername(String username);

    User findOneById(Long id);


}
