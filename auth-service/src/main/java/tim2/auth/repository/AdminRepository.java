package tim2.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tim2.auth.model.Admin;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Admin findByUserId(Long id);

    Admin findOneById(Long id);

    List<Admin> findAll();
}