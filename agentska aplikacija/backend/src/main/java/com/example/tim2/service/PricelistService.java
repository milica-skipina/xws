package com.example.tim2.service;

import com.example.tim2.dto.PricelistDTO;
import com.example.tim2.model.Pricelist;
import com.example.tim2.repository.PricelistRepository;
import com.example.tim2.datavalidation.RegularExpressions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PricelistService {

    @Autowired
    PricelistRepository pricelistRepository;

    public boolean validatePricelist(PricelistDTO p){
        if(p.getDiscount20() == null || p.getDiscount20()<0 || p.getDiscount20()>100 ||
                p.getDiscount30() == null || p.getDiscount30()<0 || p.getDiscount30()>100
                || p.getDiscountDC() == null || p.getDiscountDC()<0 || p.getDiscountDC()>100 ||
        p.getPriceDay()==null || p.getPriceDay() <0){
            return  false;
        }
        RegularExpressions regularExpressions = new RegularExpressions();
        if(!regularExpressions.isValidDiscount(p.getDiscount20()) || !regularExpressions.isValidDiscount(p.getDiscount30()) || !regularExpressions.isValidDiscount(p.getDiscountDC())){
            return false;
        }
        return  true;
    }

    public boolean addPricelist(PricelistDTO p){
        Pricelist pricelist = new Pricelist();
        if(validatePricelist(p)){
            pricelist.setPriceDay(p.getPriceDay());
            pricelist.setDiscountDC(p.getDiscountDC());
            pricelist.setDiscount20(p.getDiscount20());
            pricelist.setDiscount30(p.getDiscount30());
            pricelistRepository.save(pricelist);
            return true;
        }
        else{
            return false;
        }
    }

    public List<PricelistDTO> getAll(){
        List<PricelistDTO> retValue = new ArrayList<PricelistDTO>();
        List<Pricelist> ret = pricelistRepository.findAll();
        for(Pricelist p : ret){
            retValue.add(new PricelistDTO(p));
        }
        return retValue;
    }
}
