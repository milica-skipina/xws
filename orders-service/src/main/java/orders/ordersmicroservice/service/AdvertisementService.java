package orders.ordersmicroservice.service;

import orders.ordersmicroservice.common.RegularExpressions;
import orders.ordersmicroservice.dto.BasketDTO;
import orders.ordersmicroservice.dto.PricelistDTO;
import orders.ordersmicroservice.model.Advertisement;
import orders.ordersmicroservice.model.Pricelist;
import orders.ordersmicroservice.model.WishlistItem;
import orders.ordersmicroservice.repository.AdvertisementRepository;
import orders.ordersmicroservice.repository.PricelistRepository;
import orders.ordersmicroservice.repository.WishlistRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class AdvertisementService {

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    PricelistRepository pricelistRepository;

    public List<BasketDTO> getAllInBasket(String customerUsername) {
        ArrayList<BasketDTO> foundAds = new ArrayList<>();
        BasketDTO adv = new BasketDTO();
        Advertisement ad = new Advertisement();
        ArrayList<WishlistItem> wishes = (ArrayList<WishlistItem>) wishlistRepository.findAllByCustomerUsername(customerUsername);
        for (WishlistItem wish : wishes) {
            if (!wish.isTurnedIntoRequest()) {
                adv = new BasketDTO(wish);
                ad = advertisementRepository.findOneById(adv.getAdvertisementId());
                adv.setPrice(countPricePerAdv(adv.getStartDate(), adv.getEndDate(), ad.getPricelist()));
                foundAds.add(adv);
            }
        }

        return foundAds;
    }

    public double countPricePerAdv(Date startDate, Date endDate, Pricelist pricelist) {
        double pricePerDay = pricelist.getPriceDay();
        double discount20 = pricelist.getDiscount20();
        double discount30 = pricelist.getDiscount30();

        long diffInMillies = Math.abs(startDate.getTime() - endDate.getTime());
        long daysBetween = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

        double totalPrice = daysBetween*pricePerDay;
        if (daysBetween >= 30){
            totalPrice = totalPrice*(1 - discount30/100);
        }
        else if (daysBetween >= 20){
            totalPrice = totalPrice*(1 - discount20/100);
        }
        return totalPrice;
    }

    public boolean validatePricelist(PricelistDTO p){

        if(p.getDiscount20() == null || p.getDiscount20()<0 || p.getDiscount20()>100 ||
                p.getDiscount30() == null || p.getDiscount30()<0 || p.getDiscount30()>100
                || p.getCollisionDW() == null || p.getCollisionDW()<0  ||
                p.getPriceDay()==null || p.getPriceDay() <0 || p.getExceedMileage() == null || p.getExceedMileage() <0){
            return  false;
        }
        RegularExpressions regularExpressions = new RegularExpressions();
        if(!regularExpressions.isValidDiscount(p.getDiscount20()) || !regularExpressions.isValidDiscount(p.getDiscount30()) ){
            return false;
        }
        return  true;
    }


    public PricelistDTO editPricelist(PricelistDTO p, Long id){
        if(validatePricelist(p)) {
            Pricelist current = pricelistRepository.findOneByAdId(id);
            if(current!=null) {
                current.setDiscount30(p.getDiscount30());
                current.setDiscount20(p.getDiscount20());
                current.setPriceDay(p.getPriceDay());
                current.setCollisionDW(p.getCollisionDW());
                current.setExceedMileage(p.getExceedMileage());
                return new PricelistDTO(pricelistRepository.save(current));
            }else{
                Pricelist novi = new Pricelist(p);
                novi.setAdId(id);
                current = pricelistRepository.save(novi);
                return new PricelistDTO(current);
            }
        }
        else{
            return null;
        }
    }
}
