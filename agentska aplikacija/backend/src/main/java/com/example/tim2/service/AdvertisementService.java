package com.example.tim2.service;

import com.example.tim2.dto.AdvertisementDTO;
import com.example.tim2.dto.CarDTO;
import com.example.tim2.model.*;
import com.example.tim2.repository.*;
import com.example.tim2.datavalidation.RegularExpressions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class AdvertisementService {

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Autowired
    private EntrepreneurRepository entrepreneurRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PricelistRepository pricelistRepository;

    @Autowired
    private CodebookService codebookService;

    public boolean carValidation(CarDTO car){
        boolean ok = true;
        if(car.getModel().equals("") || car.getModel()==null
        || car.getMake().equals("") || car.getMake()==null
        ||car.getCarClass().equals("") || car.getCarClass()==null
        || car.getFuel().equals("") || car.getFuel()==null
        || car.getGearbox().equals("") || car.getGearbox()==null
        || car.getInsurance().equals("") || car.getInsurance()==null
        || car.getState().equals("") || car.getState()==null
        || car.getKidsSeats()<0 || car.getKidsSeats() == null || car.getMileage()<0 || car.getMileage()==null
        || car.getMileageLimit()<0 || car.getMileageLimit()==null){
            ok = false;
        }
        RegularExpressions regularExpressions = new RegularExpressions();
        if(!regularExpressions.isValidMileage(car.getMileage()) || !regularExpressions.isValidMileage(car.getMileageLimit())){
            return false;
        }
        if(!regularExpressions.idValidKidsSeats(car.getKidsSeats())){
            return false;
        }
        return ok;
    }

    public List<AdvertisementDTO> getAllAdvertisements(){
        List<AdvertisementDTO>retValue = new ArrayList<AdvertisementDTO>();
        List<Advertisement>ads = advertisementRepository.findAll();
        for(Advertisement a : ads){
            retValue.add(new AdvertisementDTO(a));
        }
        return retValue;
    }

    public boolean adValidation(Advertisement ad){
        boolean ok = true;
        if(ad.getEndDate().compareTo(ad.getStartDate()) <=0 || ad.getCity() == null || ad.getCity().equals("")){
            ok = false;
        }
        RegularExpressions regularExpressions = new RegularExpressions();
        if(!regularExpressions.isValidCity(ad.getCity())){
            return false;
        }
        return ok;
    }

    public boolean advertisementDate(AdvertisementDTO ad){
        boolean retValue = true;

        return retValue;
    }

    public boolean validateAdDates(Date start, Date end,Car car){
        Set<Advertisement> ads = car.getCarAdvertisement();
        boolean retValue = true;
        for(Advertisement a: ads){
            if((start.compareTo(a.getStartDate()) >=0) && (start.compareTo(a.getEndDate())<=0)){
                retValue = false;
            }else if((end.compareTo(a.getStartDate()) >=0) && (end.compareTo(a.getEndDate()) <=0)){
                retValue = false;
            }
        }
        return retValue;
    }

    public AdvertisementDTO getOneAd(Long id){
        if(id!=null){
            Advertisement ad = advertisementRepository.findOneById(id);
            return new AdvertisementDTO(ad);
        }
        else{
            return null;
        }

    }

    public boolean addAd(AdvertisementDTO advertisement, Long id, Long idp){
        Car car = carRepository.findOneById(id);
        Pricelist p = pricelistRepository.findOneById(idp);
        Advertisement newAd = new Advertisement();
        newAd.setCaAdr(car);
        newAd.setStartDate(advertisement.getStartDate());
        newAd.setEndDate(advertisement.getEndDate());
        newAd.setPricelist(p);
        if(adValidation(newAd) && validateAdDates(newAd.getStartDate(), newAd.getEndDate(),car)){
            newAd.setEntrepreneur(entrepreneurRepository.findOneByBin("123458363"));
            advertisementRepository.save(newAd);
            return true;
        }
        else{
            return false;
        }
    }

    public Car addNewAd(AdvertisementDTO advertisement, Long id){
        Car car = new Car();
        Codebook model = codebookService.findOneByNameAndCodeType(advertisement.getCarAd().getModel(), "model");
        car.setModel(model);
        Codebook brand = codebookService.findOneByNameAndCodeType(advertisement.getCarAd().getMake(), "brand");
        car.setMake(brand);
        Codebook carClass = codebookService.findOneByNameAndCodeType(advertisement.getCarAd().getCarClass(), "class");
        car.setCarClass(carClass);
        car.setFollowing(advertisement.getCarAd().isFollowing());
        Codebook fuel = codebookService.findOneByNameAndCodeType(advertisement.getCarAd().getFuel(), "fuel");
        car.setFuel(fuel);
        Codebook gearbox = codebookService.findOneByNameAndCodeType(advertisement.getCarAd().getGearbox(), "gearbox");
        car.setGearbox(gearbox);
        car.setImages(advertisement.getCarAd().getImages());
        car.setInsurance(advertisement.getCarAd().getInsurance());
        car.setKidsSeats(advertisement.getCarAd().getKidsSeats());
        car.setState(advertisement.getCarAd().getState());
        car.setEntrepreneur(entrepreneurRepository.findOneByBin("123458363"));
        car.setMileage(advertisement.getCarAd().getMileage());
        car.setMileageLimit(advertisement.getCarAd().getMileageLimit());
        car.setRaiting(advertisement.getCarAd().getRaiting());
        car.setImages(advertisement.getCarAd().getImages());
        Advertisement newAd = new Advertisement();
        newAd.setCaAdr(car);
        newAd.setStartDate(advertisement.getStartDate());
        newAd.setEndDate(advertisement.getEndDate());
        newAd.setEntrepreneur(entrepreneurRepository.findOneByBin("123458363"));
        newAd.setCity(advertisement.getCity());
        Pricelist p = pricelistRepository.findOneById(id);
        newAd.setPricelist(p);
        if(carValidation(advertisement.getCarAd()) && adValidation(newAd) && id!=null){
            carRepository.save(car);
            for(Image i : advertisement.getCarAd().getImages()){
                imageRepository.save(i);
            }
            advertisementRepository.save(newAd);
            return car;
        }
        else{
            return null;
        }
    }

    public List<AdvertisementDTO> search(String start, String end, String city){
        Date startDate = new Date(Long.parseLong(start));
        Date endDate = new Date(Long.parseLong(end));
        List<Advertisement> ads = advertisementRepository.findAllByCityIgnoreCaseAndStartDateLessThanEqualAndEndDateGreaterThanEqual(city, startDate, endDate);
        List<AdvertisementDTO> ret = new ArrayList<>();
        for (Advertisement a: ads) {
            ret.add(new AdvertisementDTO(a));
        }
        return ret;
    }

}
