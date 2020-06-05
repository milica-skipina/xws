package com.example.tim2.service;

import com.example.tim2.common.DateConverter;
import com.example.tim2.dto.MiniCarDTO;
import com.example.tim2.dto.RequestDTO;
import com.example.tim2.model.*;
import com.example.tim2.repository.CarRepository;
import com.example.tim2.repository.EntrepreneurRepository;
import com.example.tim2.repository.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RequestService {

    private AdvertisementService advertisementService;

    private RequestRepository requestRepository;

    private EntrepreneurRepository entrepreneurRepository;

    private CarRepository carRepository;

    @Autowired
    public RequestService(AdvertisementService advertisementService, RequestRepository requestRepository,
                          EntrepreneurRepository entrepreneurRepository, CarRepository carRepository) {
        this.advertisementService = advertisementService;
        this.requestRepository = requestRepository;
        this.entrepreneurRepository = entrepreneurRepository;
        this.carRepository = carRepository;
    }

    public boolean createRequests(String[] reqs, User u) {
        Hashtable<String, ArrayList<Long>> bundlePerAgent = new Hashtable<>();
        Date startDate = new Date(Long.parseLong(reqs[reqs.length-2]));
        Date endDate = new Date(Long.parseLong(reqs[reqs.length-1]));
        for (int i= 0; i < reqs.length-2 ; i++) {
            String[] array = reqs[i].split("\\+");
            if (array.length == 3) {        // znaci taj oglas ide u bundle
                bundlePerAgent = hashmapFromReq(reqs[i], bundlePerAgent);
            } else {    // pojedinacni zahtev
                Advertisement ad = advertisementService.findOneById(Long.parseLong(array[0]));
                Request request = new Request("PENDING", -1.0, startDate, endDate, u);
                request.setSender(entrepreneurRepository.findOneByCompanyName(array[1]));
                Set<Car> cars = new HashSet<Car>(1);
                cars.add(ad.getCarAd());
                request.setCars(cars);
                requestRepository.save(request);
            }
        }
        if (!bundlePerAgent.isEmpty()) {
            createBundle(bundlePerAgent, u, startDate, endDate);
        }
        return true;
    }

    /**
     * kljuc je naziv agenta, values svi oglasi tog agenta
     * @param
     * @return
     */
    private Hashtable<String, ArrayList<Long>> hashmapFromReq(String s, Hashtable<String, ArrayList<Long>> bundlePerAgent) {
            if (!bundlePerAgent.containsKey(s.split("\\+")[1])) {
                ArrayList<Long> advIds = new ArrayList<>();
                advIds.add(Long.parseLong(s.split("\\+")[0]));
                bundlePerAgent.put(s.split("\\+")[1], advIds);
            }else {
                bundlePerAgent.get(s.split("\\+")[1]).add(Long.parseLong(s.split("\\+")[0]));
            }

        return bundlePerAgent;
    }

    private void createBundle(Hashtable<String, ArrayList<Long>> bundlePerAgent, User u,
                              Date startDate, Date endDate) {
        Request request = new Request("PENDING", -1.0, startDate, endDate, u);
        Set<Car> cars = new HashSet<Car>(1);
        for (String key : bundlePerAgent.keySet()) {    //agent je key
            for (Long id : bundlePerAgent.get(key)) {   // za sve oglase tog agenta dodati aute u jedan zahtev
                Advertisement ad = advertisementService.findOneById(id);
                cars.add(ad.getCarAd());
                request.setCars(cars);
                request.setSender(entrepreneurRepository.findOneByCompanyName(key));
                requestRepository.save(request);
            }
        }
    }

    public List<Long> findAllByStateAndStartDateAndEndDate(String state, Date start, Date end){
        List<Request> requests = requestRepository.findAllByState(state);
        List<Long> ret = new ArrayList<>();
        for(Request r : requests){
            if((start.compareTo(r.getStartDate()) >= 0 && start.compareTo(r.getEndDate()) <=0)
                    || (end.compareTo(r.getStartDate()) >= 0 && end.compareTo(r.getEndDate()) <= 0)){
                Set<Car> cars = r.getCars();
                for(Car c : cars){
                    ret.add(c.getId());
                }
            }
        }
        return  ret;
    }
    /*
        za korisnika
     */
    public ArrayList<RequestDTO> requestedCars(Long id) {
        ArrayList<Request> requests = (ArrayList<Request>) requestRepository.findAllByUserId(id);
        ArrayList<RequestDTO> ret = castRequests(requests);
        return ret;
    }

    /*
        za agenta
     */
    public ArrayList<RequestDTO> requestsForApproving(Long id) {
        ArrayList<Request> requests = (ArrayList<Request>) requestRepository.findAllByEntrepreneurId(id);
        ArrayList<RequestDTO> ret = castRequests(requests);
        return ret;
    }

    private ArrayList<RequestDTO> castRequests(ArrayList<Request> requests) {
        ArrayList<RequestDTO> ret = new ArrayList<>(10);
        for (Request r: requests) {
            RequestDTO dto = new RequestDTO(r);
            for (Car c : r.getCars()) {
                dto.getCars().add(new MiniCarDTO(c));
            }
            ret.add(dto);
        }
        return ret;
    }


    public boolean modifyRequest(Long requestId, boolean flag) {
        //  TO DO: VALIDIRAJ ID I FLAG!!!!!!
        Request r = requestRepository.findOneById(requestId);
        if (r == null) {
            return false;
        }
        if (flag) {
            r.setState("RESERVED");
        } else {
            r.setState("CANCELED");
        }
        requestRepository.save(r);
        return true;
    }

    public boolean manualRenting(Long id, String startDate, String endDate) {
        Date start = DateConverter.stringToDate(startDate);
        Date end = DateConverter.stringToDate(endDate);
        ArrayList<Request> reservedRequests = (ArrayList<Request>) requestRepository.findAllByState("PAID");
        Car car = carRepository.findOneById(id);
        for (Request r : reservedRequests) {
            if (r.getCars().contains(car) && (start.before(r.getEndDate()) && start.after(r.getStartDate()) )
                    || (end.before(r.getEndDate()) && end.after(r.getStartDate()) )) {
                return false;
            }
        }
        cancelRequestsByManual(start, end, car);
        return true;
    }

    private void cancelRequestsByManual(Date start, Date end, Car car) {
        ArrayList<Request> pendingRequests = (ArrayList<Request>) requestRepository.findAllByState("PENDING");
        for (Request r : pendingRequests) {
            if (r.getCars().contains(car) && start.before(r.getEndDate()) && start.after(r.getStartDate())
                    || (end.before(r.getEndDate()) && end.after(r.getStartDate()))) {
                r.setState("CANCELED");
                requestRepository.save(r);
            }
        }
    }
}
