package com.example.tim2.service;

import com.example.tim2.common.DateConverter;
import com.example.tim2.datavalidation.RegularExpressions;
import com.example.tim2.dto.MiniCarDTO;
import com.example.tim2.dto.RequestDTO;
import com.example.tim2.model.Advertisement;
import com.example.tim2.model.Car;
import com.example.tim2.model.Request;
import com.example.tim2.model.User;
import com.example.tim2.repository.AdvertisementRepository;
import com.example.tim2.repository.CarRepository;
import com.example.tim2.repository.EntrepreneurRepository;
import com.example.tim2.repository.RequestRepository;
import com.example.tim2.soap.clients.OrderClient;
import com.example.tim2.soap.gen.AddOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class RequestService {

    private AdvertisementRepository advertisementRepository;

    private RequestRepository requestRepository;

    private EntrepreneurRepository entrepreneurRepository;

    private CarRepository carRepository;

    private RegularExpressions regularExpressions;

    private AuthService authService;

    private OrderClient orderClient;

    @Autowired
    public RequestService(AdvertisementRepository advertisementRepository, RequestRepository requestRepository,
                          EntrepreneurRepository entrepreneurRepository, CarRepository carRepository,
                          OrderClient orderClient) {
        this.advertisementRepository = advertisementRepository;
        this.requestRepository = requestRepository;
        this.entrepreneurRepository = entrepreneurRepository;
        this.carRepository = carRepository;
        this.regularExpressions = new RegularExpressions();
        this.authService = authService;
        this.orderClient = orderClient;
    }

    public boolean createRequests(String[] reqs, User u) {
        u = u.escapeParameters(u);
        if (!validateRequests(reqs)) {
            return false;
        }
        Hashtable<String, ArrayList<Long>> bundlePerAgent = new Hashtable<>();
        Date startDate = new Date(Long.parseLong(reqs[reqs.length-2]));
        Date endDate = new Date(Long.parseLong(reqs[reqs.length-1]));
        for (int i= 0; i < reqs.length-2 ; i++) {
            String[] array = reqs[i].split("\\+");
            if (array.length == 3) {        // znaci taj oglas ide u bundle
                bundlePerAgent = hashmapFromReq(reqs[i], bundlePerAgent);
            } else {    // pojedinacni zahtev
                Advertisement ad = advertisementRepository.findOneById(Long.parseLong(array[0]));
                Request request = new Request("PENDING", ad.getCarAd().getMileage(), startDate, endDate, u, new Date());
                request.setSender(entrepreneurRepository.findOneByCompanyName(array[1]));
                Set<Car> cars = new HashSet<Car>(1);
                cars.add(ad.getCarAd());
                request.setCars(cars);
                request.escapeParameters(request);
                requestRepository.save(request);
            }
        }
        if (!bundlePerAgent.isEmpty()) {
            createBundle(bundlePerAgent, u, startDate, endDate);
        }
        return true;
    }

    private boolean validateRequests(String[] reqs) {
        for (int i =0; i < reqs.length-2; i++) {
           if (!regularExpressions.charNumPlusUnlimited(reqs[i])) {
               return false;
           }
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
        Request request = new Request("PENDING", -1.0, startDate, endDate, u, new Date());
        Set<Car> cars = new HashSet<Car>(1);
        for (String key : bundlePerAgent.keySet()) {    //agent je key
            for (Long id : bundlePerAgent.get(key)) {   // za sve oglase tog agenta dodati aute u jedan zahtev
                Advertisement ad = advertisementRepository.findOneById(id);
                request.setMileage(ad.getCarAd().getMileage());
                cars.add(ad.getCarAd());
                request.setCars(cars);
                request.setSender(entrepreneurRepository.findOneByCompanyName(key));
                request.escapeParameters(request);
                requestRepository.save(request);
            }
        }
    }

    public List<Long> findAllByStateAndStartDateAndEndDate(String state, Date start, Date end){
        if (!regularExpressions.isValidInput(state)) {
            return null;
        }
        List<Request> requests = requestRepository.findAllByState(state);
        List<Long> ret = new ArrayList<>();
        for(Request r : requests){
            if((r.getStartDate().compareTo(start) <=0 && r.getEndDate().compareTo(start) >=0)
                    || (r.getStartDate().compareTo(end) <=0 && r.getEndDate().compareTo(end) >= 0)){


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
        if (!regularExpressions.idIdValid(id)) {
            return null;
        }
        ArrayList<Request> requests = (ArrayList<Request>) requestRepository.findAllByUserId(id);
        ArrayList<RequestDTO> ret = castRequests(requests);
        return ret;
    }

    /*
        za agenta
     */
    public ArrayList<RequestDTO> requestsForApproving(Long id) {
        if (!regularExpressions.idIdValid(id)) {
            return null;
        }
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
        if (!regularExpressions.idIdValid(requestId)) {
            return false;
        }
        Request r = requestRepository.findOneById(requestId);
        if (r == null) {
            return false;
        }
        try{
           AddOrderResponse response = orderClient.acceptOrder(r);
           System.out.println("MIKRO ID: " + response.getMicroId());
        }catch (Exception e){
            e.printStackTrace();
        }
        if (flag) {
            List<Long> carIds = findAllByStateAndStartDateAndEndDate("RESERVED", r.getStartDate(), r.getEndDate());
            List<Long> paidCarIds = findAllByStateAndStartDateAndEndDate("PAID", r.getStartDate(),r.getEndDate());
            carIds.addAll(paidCarIds);
            for (Long id : carIds) {
                for (Car car : r.getCars()) {
                    if (id == car.getId()) {
                        return false;   //ne moze se rezervisati
                    }
                }
            }
            r.setDateCreated(new Date());       //kasnije se koristi za proveru placanja
            r.setState("RESERVED");
        } else {
            r.setState("CANCELED");
        }
        r.escapeParameters(r);
        requestRepository.save(r);
        return true;
    }

    /**
     * data[0] - shouldRegister data[1] - customer name data[2] - email  data[3] - surname data[4] - username
     * @param id
     * @param startDate
     * @param endDate
     * @param entrepreneurId
     * @param data
     * @return
     */
    public boolean manualRenting(Long id, String startDate, String endDate, Long entrepreneurId, String[] data) {
        User customer = new User();
        if (!regularExpressions.idIdValid(id) && !regularExpressions.idIdValid(entrepreneurId)
                && !regularExpressions.isValidInput(data[4])) {
            return false;
        } else if (data[0].equals("true")) {        // treba registrovati novog
            if (!regularExpressions.isValidInput(data[1]) && !regularExpressions.isValidInput(data[3])
             && !regularExpressions.isValidEmail(data[2]) && (data[4].length() < 6)) {
                return false;
            } else {
                customer = authService.manualRegistration(data);
            }
        } else if (data[0].equals("false")) {
            customer = authService.findOneByUsername(data[4]);
        }
        after24hOr12h();
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
        Set<Car> cars = new HashSet<Car>(1);
        cars.add(car);
        Request req = new Request(cars, "PAID", car.getMileage(), start, end, customer,
                      entrepreneurRepository.findOneByUserId(entrepreneurId), new Date() );
        requestRepository.save(req);
        cancelRequestsByManual(start, end, car);
        return true;
    }

    private void cancelRequestsByManual(Date start, Date end, Car car) {
        ArrayList<Request> pendingRequests = (ArrayList<Request>) requestRepository.findAllByState("PENDING");
        pendingRequests.addAll(requestRepository.findAllByState("RESERVED"));
        for (Request r : pendingRequests) {
            if (r.getCars().contains(car) && start.before(r.getEndDate()) && start.after(r.getStartDate())
                    || (end.before(r.getEndDate()) && end.after(r.getStartDate()))) {
                r.setState("CANCELED");
                r.escapeParameters(r);
                requestRepository.save(r);
            }
        }
    }

    public boolean isInBasket(Long id, String customerUsername) {
        if (!regularExpressions.idIdValid(id) && !regularExpressions.isValidSomeName(customerUsername)) {
            return false;
        }
        ArrayList<Request> requests = (ArrayList<Request>) requestRepository.findAllByUserUsername(customerUsername);
        for (Request r : requests) {
            for (Car c: r.getCars()) {
                if (c.getId() == id) {
                    return true;
                }
            }
        }
        return false;
    }

    public void after24hOr12h() {
        ArrayList<Request> requests = (ArrayList<Request>) requestRepository.findAllByState("PENDING");
        ArrayList<Request> requestsNotPaid = (ArrayList<Request>) requestRepository.findAllByState("RESERVED");
        requests.addAll(requestsNotPaid);
        LocalDateTime currentDate = LocalDateTime.now();
        for (Request r : requests) {
            LocalDateTime requestCreationDate = DateConverter.dateToDateTime(r.getDateCreated().toString());
            long hrs = Math.abs(Duration.between(currentDate, requestCreationDate).toHours());
            if (r.getState().equals("PENDING") && hrs > 24) {
                    r.setState("CANCELED");
                    requestRepository.save(r);
            }
            if (r.getState().equals("RESERVED") && hrs > 12) {
                r.setState("CANCELED");
                requestRepository.save(r);
            }
        }
    }

    public boolean paymentMethod(Long id, String username) {
        /*if (!regularExpressions.idIdValid(id) && !regularExpressions.isValidSomeName(username)) {
            return false;
        }*/
        LocalDateTime currentDate = LocalDateTime.now();
        Request request = requestRepository.findOneById(id);
        LocalDateTime requestCreationDate = DateConverter.dateToDateTime(request.getDateCreated().toString());
        long hrs = Math.abs(Duration.between(currentDate, requestCreationDate).toHours());
        if (request.getUser().getUsername().equals(username) && request.getState().equals("RESERVED")
            && hrs < 12) {
            request.setState("PAID");
            requestRepository.save(request);
            return true;
        }
        return false;
    }
}
