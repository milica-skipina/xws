package tim2.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tim2.auth.model.Role;

public interface AuthorityRepository extends JpaRepository<Role, Long> {
        Role findByName(String name);
}

