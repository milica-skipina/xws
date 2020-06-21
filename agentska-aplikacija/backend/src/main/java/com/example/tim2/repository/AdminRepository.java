package com.example.tim2.repository;

import com.example.tim2.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Admin findOneByUserId(Long id);
}
