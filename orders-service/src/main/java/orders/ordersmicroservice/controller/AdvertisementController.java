package orders.ordersmicroservice.controller;

import orders.ordersmicroservice.common.RegularExpressions;
import orders.ordersmicroservice.config.TokenUtils;
import orders.ordersmicroservice.dto.BasketDTO;
import orders.ordersmicroservice.dto.PricelistDTO;
import orders.ordersmicroservice.service.AdvertisementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/advertisement")
public class AdvertisementController {

    @Autowired
    private AdvertisementService advertisementService;

    @Autowired
    private TokenUtils tokenUtils;

    @PostMapping(value = "/filter", produces="application/json")
    public ResponseEntity<List<BasketDTO>> getAllInBasket(HttpServletRequest request) {
        String token = tokenUtils.getToken(request);
        String username = tokenUtils.getUsernameFromToken(token);
        List<BasketDTO>retValue = advertisementService.getAllInBasket(username);        // customer username
        if(retValue.size() == 0){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        else{
            return new ResponseEntity<>(retValue, HttpStatus.OK);
        }
    }

   // @PreAuthorize("hasAuthority('EDIT_PRICE')")
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json", consumes = "application/json")
    public ResponseEntity<Boolean> editPricelist(@RequestBody PricelistDTO p, @PathVariable Long id, HttpServletRequest request) {
        RegularExpressions regularExpressions = new RegularExpressions();
        if (regularExpressions.idIdValid(id)) {
            PricelistDTO ok = advertisementService.editPricelist(p, id);
            if (ok != null) {
                return new ResponseEntity<Boolean>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(false, HttpStatus.NOT_ACCEPTABLE);
            }
        }else{
            return new ResponseEntity<>(false, HttpStatus.NOT_ACCEPTABLE);
        }
    }

}
