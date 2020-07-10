package com.example.tim2.repository;

import com.example.tim2.model.RequestWrapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestWrapperRepository extends JpaRepository<RequestWrapper, Long> {

    List<RequestWrapper> findAll();

    RequestWrapper findOneById(Long id);

    RequestWrapper findOneByRequestsId(Long id);

    List<RequestWrapper> findAllByAgentUsername(String id);

    List<RequestWrapper> findAllByCustomerUsername(String customerUsername);

    RequestWrapper findOneByMicroId(long microId);
}
