package com.example.tim2.repository;

import com.example.tim2.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
