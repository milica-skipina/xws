package orders.ordersmicroservice.repository;

import orders.ordersmicroservice.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {
}
