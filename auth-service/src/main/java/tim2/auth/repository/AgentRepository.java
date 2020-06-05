package tim2.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tim2.auth.model.Agent;

import java.util.List;

public interface AgentRepository extends JpaRepository<Agent, Long> {

    Agent findByUserId(Long id);

    Agent findOneById(Long id);

    List<Agent> findAll();
}
