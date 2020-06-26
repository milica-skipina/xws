package com.example.tim2.soap.clients;

import com.example.tim2.model.Entrepreneur;
import com.example.tim2.repository.AdvertisementRepository;
import com.example.tim2.repository.PricelistRepository;
import com.example.tim2.soap.gen.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.util.GregorianCalendar;
import java.util.List;

public class AdvertisementClient extends WebServiceGatewaySupport {

    private static final Logger log = LoggerFactory.getLogger(AdvertisementClient.class);

    private String microUsername = Entrepreneur.MICRO_USERNAME;

    @Autowired
    private PricelistRepository pricelistRepository;

    @Autowired
    private AdvertisementRepository advertisementRepository;

    public NewPricelistResponse addPricelist(com.example.tim2.model.Pricelist p) {
        Pricelist generated = p.getGenerated();
        NewPricelistRequest request = new NewPricelistRequest();
        request.setPricelist(generated);

        NewPricelistResponse response = (NewPricelistResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request);
        return response;
    }

    public NewAdvertisementResponse addAdvertisement(com.example.tim2.model.Advertisement a, Long id) throws DatatypeConfigurationException {
        NewAdvertisementRequest request = new NewAdvertisementRequest();
        Advertisement generated = a.getGenerated();
        request.setAdvertisement(generated);
        request.getAdvertisement().setPricelist(id);
        request.setUsername(a.getEntrepreneur().getUser().getUsername());
        NewAdvertisementResponse response = (NewAdvertisementResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request);
        System.out.println(response.getMicroId());
        return response;
    }

    public AddImagesResponse addImages(Long id, String[] images){
        AddImagesRequest request = new AddImagesRequest();
        for(String i:images){
            request.getImages().add(i);
        }
        request.setCarId(id);
        request.setUsername(microUsername);
        AddImagesResponse response = (AddImagesResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request);
        return response;
    }

    public EditPricelistResponse editPricelist(com.example.tim2.model.Pricelist p, String username) {
        EditPricelistRequest request = new EditPricelistRequest();
        Pricelist generated = p.getGenerated();
        request.setPricelist(generated);
        request.setUsername(username);
        EditPricelistResponse response = (EditPricelistResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request);
        System.out.println(response.isOk());
        return response;
    }

    public EditAdvertisementResponse editAdvertisement(com.example.tim2.model.Advertisement a) throws DatatypeConfigurationException {
        EditAdvertisementRequest request = new EditAdvertisementRequest();
        Advertisement generated = a.getGenerated();
        request.setCity(generated.getCity());
        request.setPricelist(a.getPricelist().getMicroId());
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(a.getEndDate());
        request.setEndDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
        c.setTime(a.getStartDate());
        request.setStartDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
        request.setMileage(a.getCarAd().getMileage());
        request.setMileageLimit(a.getCarAd().getMileageLimit());
        request.setUsername(a.getEntrepreneur().getUser().getUsername());
        request.setId(a.getMicroId());
        request.setCollisionDW(a.getCarAd().getInsurance());
        request.setKidsSeats(a.getCarAd().getKidsSeats());
        EditAdvertisementResponse response = (EditAdvertisementResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request);
        System.out.println(response.isOk());
        return response;
    }

    public DeletePricelistResponse deletePricelist(com.example.tim2.model.Pricelist p, String username){
        DeletePricelistRequest request = new DeletePricelistRequest();
        request.setUsername(username);
        request.setId(p.getMicroId());
        DeletePricelistResponse response = (DeletePricelistResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request);
        System.out.println(response.isOk());
        return response;
    }

    public DeleteAdvertisementResponse deleteAdvertisement(String username, Long id){
        DeleteAdvertisementRequest request = new DeleteAdvertisementRequest();
        request.setId(id);
        request.setUsername(username);
        DeleteAdvertisementResponse response = (DeleteAdvertisementResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request);
        System.out.println(response.isOk());
        return response;
    }

    public AllPricelistsResponse sendAllPricelists(){
        List<com.example.tim2.model.Pricelist> pricelists = pricelistRepository.findAllByMicroIdIsNull();
        AllPricelistsRequest request = new AllPricelistsRequest();
        request.setUsername(microUsername);
        for(com.example.tim2.model.Pricelist p : pricelists){
            request.getPricelists().add(p.getGenerated());
        }
        AllPricelistsResponse response = (AllPricelistsResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request);
        List<Long> microIds = response.getMicroIds();
        for(int i = 0; i < pricelists.size(); i++){
            ((com.example.tim2.model.Pricelist)pricelists.get(i)).setMicroId(microIds.get(i));
        }
        pricelistRepository.saveAll(pricelists);
        return response;
    }

    public AllAdvertisementsResponse sendAllAdvertisements() throws DatatypeConfigurationException {
        List<com.example.tim2.model.Advertisement> advertisements =advertisementRepository.findAllByMicroIdIsNull();
        AllAdvertisementsRequest request = new AllAdvertisementsRequest();
        request.setUsername(microUsername);
        for(com.example.tim2.model.Advertisement a : advertisements){
            Advertisement generated = a.getGenerated();
            generated.setPricelist(a.getPricelist().getMicroId());
            request.getAdvertisements().add(generated);
        }

        AllAdvertisementsResponse response = (AllAdvertisementsResponse) getWebServiceTemplate()
                .marshalSendAndReceive(request);
        List<Long> microIds = response.getMicroIds();
        for(int i = 0; i < advertisements.size(); i++){
            advertisements.get(i).setMicroId(microIds.get(i));
        }
        advertisementRepository.saveAll(advertisements);
        return response;
    }

}