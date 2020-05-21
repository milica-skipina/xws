package orders.ordersmicroservice.repository;

import orders.ordersmicroservice.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}
