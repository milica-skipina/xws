package orders.ordersmicroservice.repository;

import orders.ordersmicroservice.model.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

    Advertisement findOneById(Long id);

    Advertisement findOneByCarAdId(Long id);

    Advertisement findOneByAdId(Long id);
}
