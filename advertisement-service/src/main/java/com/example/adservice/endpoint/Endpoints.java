package com.example.adservice.endpoint;
import com.example.adservice.dto.AdvertisementDTO;
import com.example.adservice.dto.PricelistDTO;
import com.example.adservice.model.Car;
import com.example.adservice.model.Codebook;
import com.example.adservice.model.Pricelist;
import com.example.adservice.repository.AdvertisementRepository;

import com.example.adservice.repository.CodebookRepository;
import com.example.adservice.repository.PricelistRepository;
import com.example.adservice.service.AdvertisementService;
import com.example.adservice.service.CarService;
import com.example.adservice.service.CodebookService;
import com.example.adservice.service.PricelistService;
import com.netflix.discovery.converters.Auto;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.beans.factory.annotation.Autowired;
import rs.ac.uns.ftn.xws_tim2.*;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

@Endpoint
public class Endpoints {

    private static final String NAMESPACE_URI = "http://www.ftn.uns.ac.rs/xws_tim2" ;

    @Autowired
    private AdvertisementService advertisementService;

    @Autowired
    private CodebookRepository codebookRepository;

    @Autowired
    private PricelistRepository pricelistRepository;

    @Autowired
    private PricelistService pricelistService;

    @Autowired
    private CarService carService;

    @Autowired
    private CodebookService codebookService;

    @Autowired
    public Endpoints(CodebookRepository codebookRepository, PricelistRepository pricelistRepositoryice) {
        this.codebookRepository = codebookRepository;
        this.pricelistRepository = pricelistRepository;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "newPricelistRequest")
    @ResponsePayload
    public NewPricelistResponse getPricelist(@RequestPayload NewPricelistRequest request) {
        Pricelist pom = new Pricelist(request.getPricelist());
        Pricelist newPricelist = pricelistService.addPricelist(new PricelistDTO(pom), request.getUsername());
        NewPricelistResponse response = new NewPricelistResponse();
        response.setMicroId(newPricelist.getId());
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "newAdvertisementRequest")
    @ResponsePayload
    public NewAdvertisementResponse addAdvertisement(@RequestPayload NewAdvertisementRequest request) {

        Advertisement ad = request.getAdvertisement();
        ad = advertisementService.transform(ad);
        AdvertisementDTO newAd = new AdvertisementDTO(ad);
        String username = request.getUsername();
        Car car = advertisementService.addNewAd(newAd, request.getAdvertisement().getPricelist(), username, "", "ROLE_SELLER" );
        NewAdvertisementResponse response = new NewAdvertisementResponse();
        response.setMicroId(car.getId());

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "addImagesRequest")
    @ResponsePayload
    public AddImagesResponse addImages(@RequestPayload AddImagesRequest request) {

        String[] images = new String[request.getImages().size()];
        request.getImages().toArray(images);
        carService.addImages(request.getCarId(), images);
        AddImagesResponse response = new AddImagesResponse();
        response.setOk(true);

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "editPricelistRequest")
    @ResponsePayload
    public EditPricelistResponse getEditedPricelist(@RequestPayload EditPricelistRequest request) {
        Pricelist temp = new Pricelist(request.getPricelist());
        pricelistService.editPricelist(new PricelistDTO(temp),request.getPricelist().getId(), request.getUsername());
        EditPricelistResponse response = new EditPricelistResponse();
        response.setOk(true);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "editAdvertisementRequest")
    @ResponsePayload
    public EditAdvertisementResponse getEditedAdvertisement(@RequestPayload EditAdvertisementRequest request) throws DatatypeConfigurationException {
        AdvertisementDTO temp = new AdvertisementDTO();
        temp.setCity(request.getCity());
        temp.setId(request.getId());
        temp.getCarAd().setMileage(request.getMileage());
        temp.getCarAd().setMileageLimit(request.getMileageLimit());
        temp.getPricelist().setId(request.getPricelist());
        temp.setEndDate(request.getEndDate().toGregorianCalendar().getTime());
        temp.setStartDate(request.getStartDate().toGregorianCalendar().getTime());
        temp.setUsername(request.getUsername());
        temp.getCarAd().setInsurance(request.isCollisionDW());
        temp.getCarAd().setKidsSeats(request.getKidsSeats());
        advertisementService.editAdvertisement(temp, request.getId(), request.getPricelist(),request.getUsername());
        EditAdvertisementResponse response = new EditAdvertisementResponse();
        response.setOk(true);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deletePricelistRequest")
    @ResponsePayload
    public DeletePricelistResponse getDeletedPricelist(@RequestPayload DeletePricelistRequest request) {
        pricelistService.deletePricelist(request.getId());
        DeletePricelistResponse response = new DeletePricelistResponse();
        response.setOk(true);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteAdvertisementRequest")
    @ResponsePayload
    public DeleteAdvertisementResponse getDeletedAdvertisement(@RequestPayload DeleteAdvertisementRequest request) {
        advertisementService.deleteAd(request.getId());
        DeleteAdvertisementResponse response = new DeleteAdvertisementResponse();
        response.setOk(true);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "allPricelistsRequest")
    @ResponsePayload
    public AllPricelistsResponse addPricelists(@RequestPayload AllPricelistsRequest request) {
        List<rs.ac.uns.ftn.xws_tim2.Pricelist> pricelists = request.getPricelists();
        AllPricelistsResponse response = new AllPricelistsResponse();
        for(rs.ac.uns.ftn.xws_tim2.Pricelist p : pricelists) {
            Pricelist pom = new Pricelist(p);
            Pricelist newPricelist = pricelistService.addPricelist(new PricelistDTO(pom), request.getUsername());
            response.getMicroIds().add(newPricelist.getId());
        }
        response.setOk(true);
        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "allAdvertisementsRequest")
    @ResponsePayload
    public AllAdvertisementsResponse addAdvertisements(@RequestPayload AllAdvertisementsRequest request) {
        List<rs.ac.uns.ftn.xws_tim2.Advertisement> advertisements = request.getAdvertisements();
        AllAdvertisementsResponse response = new AllAdvertisementsResponse();
        for(rs.ac.uns.ftn.xws_tim2.Advertisement a : advertisements) {
            a = advertisementService.transform(a);
            AdvertisementDTO newAd = new AdvertisementDTO(a);
            String username = request.getUsername();
            Car car = advertisementService.addNewAd(newAd, a.getPricelist(), username, "", "ROLE_SELLER" );
            response.getMicroIds().add(car.getId());
        }
        response.setOk(true);
        return response;
    }

}


