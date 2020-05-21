package orders.ordersmicroservice.repository;

import orders.ordersmicroservice.model.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {
}
