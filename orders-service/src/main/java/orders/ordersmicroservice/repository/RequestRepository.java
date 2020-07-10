package orders.ordersmicroservice.repository;

import orders.ordersmicroservice.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findAllByCarsId(Long id);

    List<Request> findAllByEndDateLessThanEqualOrStartDateGreaterThanEqualAndStateAndCarsId (Date endDate,
                                                                                             Date startDate,
                                                                                             String state,
                                                                                             Long id);

    List<Request>findAllByCustomerUsername(String username);

    List<Request>findAllByAgentUsername(String username);

    Request findOneById(Long id);

    List<Request> findAllByState(String state);

    List<Request> findAllByCustomerUsernameAndEndDateLessThanEqualAndState(String username, Date endDate, String state);

    List<Request> findAllByEndDateLessThanEqualOrStartDateGreaterThanEqualAndAgentUsernameAndState(Date endDate,
                                                                                                      Date startDate,
                                                                                                      String username,
                                                                                                      String state);

    List<Request> findAllByCustomerUsernameAndAgentUsernameAndState(String customerUsername,String agentUsername,String state);

    ArrayList<Request> findAllByCustomerUsernameAndStateAndAgentUsername(String customerUsername, String state,
                                                                         String agentUsername);

    ArrayList<Request> findAllByCustomerUsernameAndAgentUsername(String customerUsername, String agentUsername);

    ArrayList<Request> findAllByStateAndAgentUsername(String pending, String agentUsername);
}
