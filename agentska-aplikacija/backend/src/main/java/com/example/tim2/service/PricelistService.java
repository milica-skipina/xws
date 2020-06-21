package com.example.tim2.service;

import com.example.tim2.dto.AdvertisementDTO;
import com.example.tim2.dto.PricelistDTO;
import com.example.tim2.model.Pricelist;
import com.example.tim2.repository.PricelistRepository;
import com.example.tim2.datavalidation.RegularExpressions;
import com.example.tim2.soap.clients.AdvertisementClient;
import com.example.tim2.soap.gen.DeletePricelistResponse;
import com.example.tim2.soap.gen.EditPricelistResponse;
import com.example.tim2.soap.gen.NewPricelistResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PricelistService {

    @Autowired
    PricelistRepository pricelistRepository;

    @Autowired
    AdvertisementService advertisementService;

    @Autowired
    AdvertisementClient advertisementClient;

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

    public PricelistDTO addPricelist(PricelistDTO p, String username){
        Pricelist pricelist = new Pricelist();
        if(validatePricelist(p)){
            pricelist.setPriceDay(p.getPriceDay());
            pricelist.setCollisionDW(p.getCollisionDW());
            pricelist.setDiscount20(p.getDiscount20());
            pricelist.setDiscount30(p.getDiscount30());
            pricelist.setExceedMileage(p.getExceedMileage());
            pricelist.setDeleted(false);
            pricelist.setUsername(username);
            try{
                NewPricelistResponse response = advertisementClient.addPricelist(pricelist);
                pricelist.setMicroId(response.getMicroId());
            }catch (Exception e){
                e.printStackTrace();
            }

            return new PricelistDTO(pricelistRepository.save(pricelist));
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
           Pricelist pricelist = pricelistRepository.findOneById(id);
           pricelist.setDeleted(true);
           try{
               DeletePricelistResponse response = advertisementClient.deletePricelist(pricelist, pricelist.getUsername());
           }catch (Exception e){
               e.printStackTrace();
           }
           pricelistRepository.save(pricelist);
           ok = true;
           return  true;
       }
       return  ok;
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
                try{
                    EditPricelistResponse response = advertisementClient.editPricelist(current, current.getUsername());
                }catch (Exception e){
                    e.printStackTrace();
                }
                return new PricelistDTO(pricelistRepository.save(current));
            }
            else{
                return null;
            }
        }
        else{
            return null;
        }
    }

    public List<PricelistDTO> getAll(){
        List<PricelistDTO> retValue = new ArrayList<PricelistDTO>();
        List<Pricelist> ret = pricelistRepository.findAll();
        for(Pricelist p : ret){
            if(!p.isDeleted()) {
                retValue.add(new PricelistDTO(p));
            }
        }
        return retValue;
    }

    public PricelistDTO getOne(Long id){
        Pricelist p = pricelistRepository.findOneById(id);
        return new PricelistDTO(p);
    }
}
