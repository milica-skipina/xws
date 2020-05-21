package orders.ordersmicroservice.repository;

import orders.ordersmicroservice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
