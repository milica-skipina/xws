package orders.ordersmicroservice.service;

import orders.ordersmicroservice.dto.AdvertisementDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class BasketService {

    public ArrayList<AdvertisementDTO> allItemsInBasket(long id) {
        // TO DO
        return null;
    }

    public boolean addToBasket(long advertisementId) {
        // TO DO
        return true;
    }

    public boolean removeFromBasket(long advertisementId) {
        // TO DO
        return true;
    }
}
