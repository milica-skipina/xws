package orders.ordersmicroservice.repository;

import orders.ordersmicroservice.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    Car findOneById(Long id);

    List<Car> findAllByEntrepreneurUsername(String username);

     Car findOneByAdId(Long id);
}
