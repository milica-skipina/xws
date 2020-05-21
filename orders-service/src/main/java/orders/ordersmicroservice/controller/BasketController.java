package orders.ordersmicroservice.controller;

import orders.ordersmicroservice.dto.AdvertisementDTO;
import orders.ordersmicroservice.service.BasketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/basket")
public class BasketController {

    @Autowired
    private BasketService basketService;

    @GetMapping("/")
    public ResponseEntity<List<AdvertisementDTO>> getAllItems(Long userId) {
        ArrayList<AdvertisementDTO> basket = basketService.allItemsInBasket(userId);

        if (basket.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }else {
            return new ResponseEntity<>(basket, HttpStatus.OK);
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<HttpStatus> addToBasket(@PathVariable Long advertisementId) {
        boolean success = basketService.addToBasket(advertisementId);
        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> removeFromBasket(@PathVariable Long advertisementId) {
        boolean success = basketService.removeFromBasket(advertisementId);

        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        }else {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
    }


}
