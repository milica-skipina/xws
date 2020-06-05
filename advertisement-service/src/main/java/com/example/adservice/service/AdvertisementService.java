package com.example.adservice.service;

import com.example.adservice.common.DateConverter;
import com.example.adservice.config.TokenUtils;
import com.example.adservice.datavalidation.RegularExpressions;
import com.example.adservice.dto.AdvertisementDTO;
import com.example.adservice.dto.BasketDTO;
import com.example.adservice.dto.CarDTO;
import com.example.adservice.dto.SearchDTO;
import com.example.adservice.model.*;
import com.example.adservice.repository.*;
import com.netflix.discovery.converters.Auto;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.web.client.RestTemplate;

@Service
public class AdvertisementService {

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PricelistRepository pricelistRepository;

    @Autowired
    private CodebookService codebookService;

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private RestTemplate restTemplate;

    public List<AdvertisementDTO> search(String start, String end, String city){
        city = Encode.forHtml(city);
        Date startDate = new Date(Long.parseLong(start));
        Date endDate = new Date(Long.parseLong(end));
        List<Advertisement> ads = advertisementRepository.findAllByDeletedAndCityIgnoreCaseAndStartDateLessThanEqualAndEndDateGreaterThanEqual(false, city, startDate, endDate);
        List<AdvertisementDTO> ret = new ArrayList<>();
        String url = "https://localhost:8082/orders/request/car/" + start + "/" + end;
        List<Long> ids = restTemplate.getForObject(url, ArrayList.class);
        for (Advertisement a: ads) {
            if(!ids.contains(a.getCarAd().getId())) {
                AdvertisementDTO aDTO = new AdvertisementDTO(a);
                aDTO.getCarAd().setPrice(countPricePerAdv(startDate, endDate, a.getPricelist()));
                ret.add(aDTO);
            }
        }
        return ret;
    }

    public List<AdvertisementDTO> advancedSearch(SearchDTO search){
        List<AdvertisementDTO> ads = search(search.getStartDate(), search.getEndDate(), search.getCity());
        List<AdvertisementDTO> ret = new ArrayList<>();
        boolean ok;
        for (AdvertisementDTO a: ads){
            ok = search.getBrand().contains(a.getCarAd().getMake())
                    && search.getModel().contains((a.getCarAd().getModel()))
                    && search.getGearbox().contains(a.getCarAd().getGearbox())
                    && search.getFuel().contains(a.getCarAd().getFuel())
                    && search.getCarClass().contains(a.getCarAd().getCarClass())
                    && a.getPricelist().getPriceDay() >= search.getMinPrice()
                    && a.getPricelist().getPriceDay() <= search.getMaxPrice()
                    && a.getCarAd().getMileage() <= search.getMileage()
                    && a.getCarAd().getKidsSeats() >= search.getKidsSeats();
            if(ok) {
                if(search.getMileageLimit() == -1.0){
                    ok = a.getCarAd().getMileageLimit() == -1;
                }
                else{
                    ok = a.getCarAd().getMileageLimit() <= search.getMileageLimit();
                }
                if (search.getDamageWaiver()){
                    ok = ok && a.getCarAd().getInsurance();
                }
            }
            if (ok){
                ret.add(a);
            }
        }
        return ret;
    }


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
                || car.getMileageLimit()< -1 || car.getMileageLimit()==null){
            ok = false;
        }
        RegularExpressions regularExpressions = new RegularExpressions();
        if(!regularExpressions.isValidMileage(car.getMileage())){
            return false;
        }
        if(!regularExpressions.idValidKidsSeats(car.getKidsSeats())){
            return false;
        }
        return ok;
    }

    public AdvertisementDTO forHtmlAd(AdvertisementDTO a){
        a.setCity(Encode.forHtml(a.getCity()));
        return  a;
    }

    public List<AdvertisementDTO> getAllAdvertisements(){
        List<AdvertisementDTO>retValue = new ArrayList<AdvertisementDTO>();
        List<Advertisement>ads = advertisementRepository.findAll();
        for(Advertisement a : ads){
            if(!a.isDeleted()){
                retValue.add(new AdvertisementDTO(a));
            }
        }
        return retValue;
    }

    public Long getCarByAdId(Long id){
        Advertisement ad = advertisementRepository.getOne(id);
        Long ret = ad.getCarAd().getId();
        return  ret;
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
    public boolean deleteAd(Long id) {
        Advertisement advertisement = advertisementRepository.findOneById(id);
        advertisement.setDeleted(true);
        advertisementRepository.save(advertisement);
        return true;
    }


    public boolean validateAdDates(Date start, Date end, Car car, Advertisement ad){
        Set<Advertisement> ads = car.getCarAdvertisement();
        boolean retValue = true;
        for(Advertisement a: ads){
            if(ad!=null) {
                if (a.getId() != ad.getId()) {
                    if ((start.compareTo(a.getStartDate()) >= 0) && (start.compareTo(a.getEndDate()) <= 0)) {
                        retValue = false;
                    } else if ((end.compareTo(a.getStartDate()) >= 0) && (end.compareTo(a.getEndDate()) <= 0)) {
                        retValue = false;
                    }
                }
            }else{
                if ((start.compareTo(a.getStartDate()) >= 0) && (start.compareTo(a.getEndDate()) <= 0)) {
                    retValue = false;
                } else if ((end.compareTo(a.getStartDate()) >= 0) && (end.compareTo(a.getEndDate()) <= 0)) {
                    retValue = false;
                }
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


    public AdvertisementDTO editAdvertisement(AdvertisementDTO a, Long aId, Long pId, String useername, String name){
        RegularExpressions regularExpressions = new RegularExpressions();
        Advertisement ad = new Advertisement();
        if(regularExpressions.idIdValid(aId)){
            ad = advertisementRepository.findOneById(aId);
        }
        else{
            return null;
        }
        if(ad.getEntrepreneurName().equals(name) && ad.getEntrepreneurUsername().equals(useername)) {
            Car car = carRepository.findOneById(ad.getCarAd().getId());
            if (validateAdDates(a.getStartDate(), a.getEndDate(), car, ad)) {
                ad.setStartDate(a.getStartDate());
                ad.setEndDate(a.getEndDate());
            } else {
                return null;
            }
            if (regularExpressions.isValidSomeName(a.getCity())) {
                ad.setCity(a.getCity());
            } else {
                return null;
            }
            Pricelist pricelist = new Pricelist();
            if (regularExpressions.idIdValid(pId)) {
                pricelist = pricelistRepository.findOneById(pId);
                ad.setPricelist(pricelist);
            } else {
                return null;
            }
            ad.getCarAd().setMileageLimit(a.getCarAd().getMileageLimit());
            ad.getCarAd().setMileage(a.getCarAd().getMileage());
            ad.getCarAd().setKidsSeats(a.getCarAd().getKidsSeats());
            ad.getCarAd().setInsurance(a.getCarAd().getInsurance());

            return new AdvertisementDTO(advertisementRepository.save(ad));
        } else{
            return null;
        }
    }

    public boolean canAccess(Long id, HttpServletRequest request){
        String token = tokenUtils.getToken(request);
        String username = tokenUtils.getUsernameFromToken(token);
        String name = tokenUtils.getNameFromToken(token);
        Advertisement ad = advertisementRepository.findOneById(id);
        if(ad.getEntrepreneurUsername().equals(username) && ad.getEntrepreneurName().equals(name)){
            return true;
        } else{
            return false;
        }
    }

    public boolean addAd(AdvertisementDTO advertisement, Long id, Long idp, String username, String name){
        Car car = carRepository.findOneById(id);
        Pricelist p = pricelistRepository.findOneById(idp);
        Advertisement newAd = new Advertisement();
        newAd.setCaAdr(car);
        newAd.setStartDate(advertisement.getStartDate());
        newAd.setEndDate(advertisement.getEndDate());
        newAd.setPricelist(p);
        newAd.setCity(advertisement.getCity());
        newAd.setDeleted(false);
        newAd.setEntrepreneurName(name);
        newAd.setEntrepreneurUsername(username);
        // dodati setovanje id ulogovanog koji kreira oglas, tnx
        if(adValidation(newAd) && validateAdDates(newAd.getStartDate(), newAd.getEndDate(),car,null)){
            AdvertisementDTO temp = forHtmlAd(advertisement);
            newAd.setCity(temp.getCity());
            advertisementRepository.save(newAd);
            return true;
        }
        else{
            return false;
        }
    }

    /**
     *
     * @param advertisement
     * @param id - id pricelista
     * @return
     */
    public Car addNewAd(AdvertisementDTO advertisement, Long id, String username, String name, String role){
        List<Advertisement> ads = advertisementRepository.findAll();
        int x = 0;
        if (role.equals("ROLE_CUSTOMER")){
            for(Advertisement a : ads){
                if(a.getEntrepreneurUsername().equals(username) && a.getEntrepreneurName().equals(name) && (!a.isDeleted())){
                    x++;
                }
            }
        }
        if(x == 3){
            return null;
        }
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
        car.setEntrepreneurUsername(username);
        car.setMileage(advertisement.getCarAd().getMileage());
        car.setMileageLimit(advertisement.getCarAd().getMileageLimit());
        car.setState(Encode.forHtml(car.getState()));
        car.setRaiting(advertisement.getCarAd().getRaiting());
        car.setImages(advertisement.getCarAd().getImages());
        Advertisement newAd = new Advertisement();
        newAd.setCaAdr(car);
        newAd.setStartDate(advertisement.getStartDate());
        newAd.setEndDate(advertisement.getEndDate());
        newAd.setEntrepreneurUsername(username);
        newAd.setCity(advertisement.getCity());
        newAd.setDeleted(false);
        newAd.setEntrepreneurName(name);
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

    public boolean canAdd(String name, String username, String role){
        int x = 0;
        if(role.equals("ROLE_CUSTOMER")){
            List<Advertisement> ads = advertisementRepository.findAll();
            for(Advertisement a : ads) {
                if (a.getEntrepreneurName().equals(name) && a.getEntrepreneurUsername().equals(username) && (!a.isDeleted())) {
                    x++;
                }
            }
        }else{
            return true;
        }
        if(x == 3){
            return false;
        }
        else{
            return true;
        }
    }

    public List<BasketDTO> getAllInBasket(Long[] identifiers) {
        ArrayList<BasketDTO> foundAds = new ArrayList<>();
        BasketDTO adv = new BasketDTO();
        Advertisement ad = new Advertisement();
        for (int i=0; i < identifiers.length ; i++) {
            ad = advertisementRepository.findOneById(identifiers[i]);
            adv = new BasketDTO(ad);
            adv.setPrice(countPricePerAdv(adv.getStartDate(), adv.getEndDate(), ad.getPricelist()));
            foundAds.add(adv);
        }
        return foundAds;
    }

    private double countPricePerAdv(Date startDate, Date endDate, Pricelist pricelist) {
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

    public Advertisement findOneById(Long id) {
        return advertisementRepository.findOneById(id);
    }

    public List<AdvertisementDTO> allAdsForAgent(String agentUsername) {
        ArrayList<Advertisement> ads = (ArrayList<Advertisement>) advertisementRepository.findAllByEntrepreneurUsername(agentUsername);
        List<AdvertisementDTO> retVal = new ArrayList<AdvertisementDTO>() ;
        for(Advertisement a : ads){
            if(!a.isDeleted()){
                retVal.add(new AdvertisementDTO(a));
            }
        }
        return retVal;
    }
}
