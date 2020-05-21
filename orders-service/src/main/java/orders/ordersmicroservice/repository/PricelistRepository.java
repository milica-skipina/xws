package orders.ordersmicroservice.repository;

import orders.ordersmicroservice.model.Pricelist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PricelistRepository extends JpaRepository<Pricelist, Long> {
}
