package com.example.adservice.service;

import com.example.adservice.datavalidation.RegularExpressions;
import com.example.adservice.dto.AdvertisementDTO;
import com.example.adservice.dto.PricelistDTO;
import com.example.adservice.model.Pricelist;
import com.example.adservice.repository.PricelistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PricelistService {

    @Autowired
    private PricelistRepository pricelistRepository;

    @Autowired
    private AdvertisementService advertisementService;

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

    public Pricelist addPricelist(PricelistDTO p, String username){
        Pricelist pricelist = new Pricelist();
        if(validatePricelist(p)){
            pricelist.setPriceDay(p.getPriceDay());
            pricelist.setCollisionDW(p.getCollisionDW());
            pricelist.setDiscount20(p.getDiscount20());
            pricelist.setDiscount30(p.getDiscount30());
            pricelist.setExceedMileage(p.getExceedMileage());
            pricelist.setDeleted(false);
            pricelist.setUsername(username);
            pricelistRepository.save(pricelist);
            return pricelist;
        }
        else{
            return null;
        }
    }

    public boolean deletePricelist(Long id){
        List<AdvertisementDTO>ads = advertisementService.getAllAdvertisements();
        boolean ok = true;
        for(AdvertisementDTO a : ads){
            if(a.getPricelist().getId() == id){
                ok = false;
            }
        }
        if(ok){
           Pricelist temp = pricelistRepository.findOneById(id);
           temp.setDeleted(true);
           pricelistRepository.save(temp);
        }
        return  ok;
    }

    public List<PricelistDTO> getAll(){
        List<PricelistDTO> retValue = new ArrayList<PricelistDTO>();
        List<Pricelist> ret = pricelistRepository.findAll();
        for(Pricelist p : ret){
            if(p.isDeleted() == false) {
                retValue.add(new PricelistDTO(p));
            }
        }
        return retValue;
    }

    public PricelistDTO getOne(Long id){
        Pricelist p = pricelistRepository.findOneById(id);
        return new PricelistDTO(p);
    }

    public PricelistDTO editPricelist(PricelistDTO p, Long id, String username){
        if(validatePricelist(p)) {
            Pricelist current = pricelistRepository.findOneById(id);
            if(current.getUsername().equals(username)){
                current.setDiscount30(p.getDiscount30());
                current.setDiscount20(p.getDiscount20());
                current.setPriceDay(p.getPriceDay());
                current.setCollisionDW(p.getCollisionDW());
                current.setExceedMileage(p.getExceedMileage());
                return new PricelistDTO(pricelistRepository.save(current));
            }
           else {
               return null;
            }
        }
        else{
            return null;
        }
    }
}
