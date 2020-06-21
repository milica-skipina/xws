package orders.ordersmicroservice.repository;

import orders.ordersmicroservice.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request>findAllByCarsId(Long id);

    List<Request>findAllByCustomerUsername(String username);

    List<Request>findAllByAgentUsername(String username);

    Request findOneById(Long id);

    List<Request> findAllByState(String state);

    List<Request> findAllByCustomerUsernameAndEndDateLessThanEqualAndState(String username, Date endDate, String state);

    List<Request> findAllByCustomerUsernameAndAgentUsernameAndState(String customerUsername,String agentUsername,String state);
}
