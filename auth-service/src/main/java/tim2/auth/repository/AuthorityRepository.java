package tim2.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tim2.auth.model.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
        Authority findByName(String name);
}

