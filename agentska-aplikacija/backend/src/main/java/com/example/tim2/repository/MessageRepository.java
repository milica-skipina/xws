package com.example.tim2.repository;

import com.example.tim2.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    Message findOneById(Long id);
    List<Message> findAllByEndUserId(Long id);
    List<Message> findAllByEntrepreneurId(Long id);
}
