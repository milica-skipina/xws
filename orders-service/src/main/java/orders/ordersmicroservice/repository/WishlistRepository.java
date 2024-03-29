package orders.ordersmicroservice.repository;

import orders.ordersmicroservice.model.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface WishlistRepository extends JpaRepository<WishlistItem, Long> {

    List<WishlistItem> findAll();

    WishlistItem findOneById(Long id);

    List<WishlistItem> findAllByAdvertisementId(Long id);

    List<WishlistItem> findAllByCustomerUsername(String customerUsername);

    ArrayList<WishlistItem> findAllByCustomerUsernameAndAdvertisementId(String customerUsername, Long id);
}

