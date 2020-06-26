package tim2.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tim2.auth.model.EndUser;

import java.util.List;

public interface EndUserRepository extends JpaRepository<EndUser, Long> {

    EndUser findByUserId(Long id);

    EndUser findOneById(Long id);

    List<EndUser> findAll();

    EndUser findByUserUsername(String username);

}
