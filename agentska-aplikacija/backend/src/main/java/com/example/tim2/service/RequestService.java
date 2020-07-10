package com.example.tim2.service;

import com.example.tim2.common.DateConverter;
import com.example.tim2.datavalidation.RegularExpressions;
import com.example.tim2.dto.BasketDTO;
import com.example.tim2.dto.MiniCarDTO;
import com.example.tim2.dto.RequestDTO;
import com.example.tim2.dto.RequestWrapDTO;
import com.example.tim2.model.*;
import com.example.tim2.model.Advertisement;
import com.example.tim2.model.Car;
import com.example.tim2.repository.*;
import com.example.tim2.soap.clients.OrderClient;
import com.example.tim2.soap.gen.*;
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

    private EndUserRepository endUserRepository;

    private UserRepository userRepository;

    private AdvertisementService advertisementService;

    private WishlistRepository wishlistRepository;

    private RequestWrapperRepository requestWrapperRepository;

    @Autowired
    public RequestService(AdvertisementRepository advertisementRepository, RequestRepository requestRepository,
                          EntrepreneurRepository entrepreneurRepository, CarRepository carRepository, OrderClient orderClient,
                          EndUserRepository endUserRepository, AuthService authService, AdvertisementService advertisementService,
                          WishlistRepository wishlistRepository, RequestWrapperRepository requestWrapperRepository,
                          UserRepository userRepository) {
        this.advertisementRepository = advertisementRepository;
        this.requestRepository = requestRepository;
        this.entrepreneurRepository = entrepreneurRepository;
        this.carRepository = carRepository;
        this.regularExpressions = new RegularExpressions();
        this.authService = authService;
        this.orderClient = orderClient;
        this.endUserRepository = endUserRepository;
        this.advertisementService = advertisementService;
        this.wishlistRepository = wishlistRepository;
        this.requestWrapperRepository = requestWrapperRepository;
        this.userRepository = userRepository;
    }

    public boolean createRequests(ArrayList<BasketDTO> reqs, User u) {
        u = u.escapeParameters(u);
        Hashtable<String, ArrayList<Request>> bundlePerAgent = new Hashtable<>();
        Set<Request> requests = new HashSet<Request>(1);     // za wrapper
        for (BasketDTO req : reqs) {
                WishlistItem wish = wishlistRepository.findOneById(req.getWishlistId());
                wish.setTurnedIntoRequest(true);
                wishlistRepository.save(wish);
                Advertisement ad = advertisementRepository.findOneById(req.getAdvertisementId());
                Request request = new Request("PENDING", ad.getCarAd().getMileage(), req.getStartDate(),
                        req.getEndDate(), u, new Date());
                request.setSender(entrepreneurRepository.findOneByCompanyName(req.getEntrepreneur()));
                Set<Car> cars = new HashSet<Car>(1);
                cars.add(ad.getCarAd());
                request.setCars(cars);
                request.escapeParameters(request);
                requestRepository.save(request);
            if (req.isBundle()) {        // znaci taj oglas ide u bundle
                bundlePerAgent = hashmapFromReq(request, bundlePerAgent);
            } else {
                requests.add(request);
                RequestWrapper wrapper = new RequestWrapper();
                wrapper.setState("PENDING");
                wrapper.setCustomerUsername(request.getUser().getUsername());
                wrapper.setAgentUsername(request.getSender().getUser().getUsername());
                wrapper.setRequests(requests);
                requestWrapperRepository.save(wrapper);
                request.setRequestWrapper(wrapper);
                requestRepository.save(request);
                requests.clear();

            }
        }
        if (!bundlePerAgent.isEmpty()) {
            createBundle(bundlePerAgent, u);
        }
        return true;
    }

    /**
     * kljuc je naziv agenta, values svi zahtevi za bundle za tog agenta (ti zahtevi idu u jedan wreper)
     * @param
     * @return
     */
    private Hashtable<String, ArrayList<Request>> hashmapFromReq(Request req, Hashtable<String, ArrayList<Request>> bundlePerAgent) {
            if (!bundlePerAgent.containsKey(req.getSender().getCompanyName())) {
                ArrayList<Request> requestIds = new ArrayList<>();
                requestIds.add(req);
                bundlePerAgent.put(req.getSender().getCompanyName(), requestIds);
            }else {
                bundlePerAgent.get(req.getSender().getCompanyName()).add(req);
            }

        return bundlePerAgent;
    }

    //@Transactional
    public void createBundle(Hashtable<String, ArrayList<Request>> bundlePerAgent, User u) {
        Set<Request> requests = new HashSet<Request>(1);
        for (String key : bundlePerAgent.keySet()) {
            requests.addAll(bundlePerAgent.get(key));
            RequestWrapper wrapper = new RequestWrapper();
            wrapper.setState("PENDING");
            wrapper.setCustomerUsername(((Request)bundlePerAgent.get(key).toArray()[0]).getUser().getUsername());
            wrapper.setAgentUsername(((Request)bundlePerAgent.get(key).toArray()[0]).getSender().getUser().getUsername());
            wrapper.setRequests(requests);
            requestWrapperRepository.save(wrapper);
            Iterator quickFix = requests.iterator();
            while (quickFix.hasNext()) {
                ((Request)quickFix.next()).setRequestWrapper(wrapper);
            }
            List<Request> temp = new ArrayList<>(bundlePerAgent.get(key));
            requestRepository.saveAll(temp);
            requests.clear();
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
    public ArrayList<RequestWrapDTO> requestedCars(String customerUsername) {
        if (!regularExpressions.isValidSomeName(customerUsername)) {
            return null;
        }
        ArrayList<RequestWrapper> requests = (ArrayList<RequestWrapper>) requestWrapperRepository.
                findAllByCustomerUsername(customerUsername);
        ArrayList<RequestWrapDTO> ret = castRequests(requests);
        return ret;
    }

    /*
        za agenta
     */
    public ArrayList<RequestWrapDTO> requestsForApproving(String agentUsername) {
        if (!regularExpressions.isValidSomeName(agentUsername)) {
            return null;
        }
        getMicOrders(agentUsername);
        ArrayList<RequestWrapper> requests = (ArrayList<RequestWrapper>) requestWrapperRepository.
                findAllByAgentUsername(agentUsername);
        ArrayList<RequestWrapDTO> ret = castRequests(requests);
        return ret;
    }

    private void getMicOrders(String agentUsername){
        try{
            ModifyOrderResponse response = orderClient.getMicroOrders(agentUsername);
            for (Wrapper w : response.getRequestWrappers()) {
                if (endUserRepository.findByUserUsername(w.getCustomerUsername()) == null) {        // registruj kupca
                    String[] custDetails = new String[]{"true", "name", "email@gmail.com", "surname", w.getCustomerUsername()};
                    User endUser = authService.manualRegistration(custDetails);
                }

                RequestWrapper isChanged = requestWrapperRepository.findOneByMicroId(w.getMicroId());
                if (isChanged == null) {
                    RequestWrapper microWrap = new RequestWrapper(w);
                    HashSet<Request> nvsd = new HashSet<>();
                    for (Order ox : w.getRequests()) {
                        Request tempSoap = orderToRequest(ox);
                        tempSoap.setRequestWrapper(microWrap);
                        nvsd.add(tempSoap);
                    }
                    microWrap.setRequests(nvsd);
                    requestWrapperRepository.save(microWrap);
                } else if (!isChanged.getState().equals(w.getState())) {        //placen je na mikro
                    isChanged.setState(w.getState());
                    requestWrapperRepository.save(isChanged);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private ArrayList<RequestWrapDTO> castRequests(ArrayList<RequestWrapper> requests) {
        ArrayList<RequestWrapDTO> temp = new ArrayList<>(10);
        for (RequestWrapper r: requests) {
            HashSet<RequestDTO> ret = new HashSet<>(10);
            for (Request req : r.getRequests() ) {
                RequestDTO dto = new RequestDTO(req);
                EndUser customer = endUserRepository.findByUserUsername(req.getUser().getUsername());
                dto.setUserName(customer.getName() + " " + customer.getSurname());
                for (Car c : req.getCars()) {
                    dto.getCars().add(new MiniCarDTO(c));
                }
                ret.add(dto);
            }
            RequestWrapDTO wrapDTO = new RequestWrapDTO(r);
            wrapDTO.setRequests(ret);
            temp.add(wrapDTO);
        }
        return temp;
    }


    public boolean modifyRequest(Long requestWrapperId, boolean flag, String username, String role) {
        if (!regularExpressions.idIdValid(requestWrapperId)) {
            return false;
        }
        RequestWrapper requestWrapper = requestWrapperRepository.findOneById(requestWrapperId);
        if (flag){
            requestWrapper.setState("RESERVED");

        }else {
            requestWrapper.setState("CANCELED");
        }
        requestWrapperRepository.save(requestWrapper);

        List<Request> temp = new ArrayList<>(requestWrapper.getRequests());
        Iterator<Request> wrapperRequests = temp.iterator();
        while(wrapperRequests.hasNext()) {
            Request r = wrapperRequests.next();
            AddOrderResponse response = new AddOrderResponse();
            try{
                response = orderClient.acceptOrder(r);
                System.out.println("MIKRO ID: " + response.getMicroId());
                r.setMicroId(response.getMicroId());
            }catch (Exception e){
                e.printStackTrace();
            }
            if (flag && response.isOk()) {
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
                r.setMicroId(response.getMicroId());
            } else if (!response.isOk()) {
            return false;
        } else {
                if(username.equals("kupac")) {
                    EndUser endUser = endUserRepository.findByUserUsername(username);
                    endUser.setNumberCanceledRequest(endUser.getNumberCanceledRequest() + 1);
                    endUserRepository.save(endUser);
                }
                r.setState("CANCELED");
            }
            r.escapeParameters(r);
        }

        temp = new ArrayList<>(requestWrapper.getRequests());
        requestRepository.saveAll(temp);

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
        AddOrderResponse response = new AddOrderResponse();
        if (!regularExpressions.idIdValid(id) && !regularExpressions.idIdValid(entrepreneurId)
                && !regularExpressions.isValidInput(data[4])) {
            return false;
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

        if (data[0].equals("true")) {        // treba registrovati novog
            if (!regularExpressions.isValidInput(data[1]) && !regularExpressions.isValidInput(data[3])
             && !regularExpressions.isValidEmail(data[2]) && (data[4].length() < 6)) {
                return false;
            } else {
                customer = authService.manualRegistration(data);
                req.setUser(customer);
            }
        } else if (data[0].equals("false")) {
            customer = authService.findOneByUsername(data[4]);
            req.setUser(customer);
            try{
                response = orderClient.acceptOrder(req);
                System.out.println("MIKRO ID: " + response.getMicroId());
            }catch (Exception e){
                e.printStackTrace();
            }
            if (!response.isOk()) {
                return false;
            }
        }

        HashSet<Request> tempRequests = new HashSet<>(1);
        tempRequests.add(req);
        RequestWrapper newWrap = new RequestWrapper(tempRequests);
        requestRepository.save(req);
        requestWrapperRepository.save(newWrap);
        cancelRequestsByManual(start, end, car);
        return true;
    }

    private void cancelRequestsByManual(Date start, Date end, Car car) {
        ArrayList<Request> pendingRequests = (ArrayList<Request>) requestRepository.findAllByState("PENDING");
        for (Request r : pendingRequests) {
            if (r.getCars().contains(car) && start.before(r.getEndDate()) && start.after(r.getStartDate())
                    || (end.before(r.getEndDate()) && end.after(r.getStartDate()))) {
                r.setState("CANCELED");
                r.getRequestWrapper().setState("CANCELED");
                r.escapeParameters(r);
                requestRepository.save(r);
                requestWrapperRepository.save(r.getRequestWrapper());
            }
        }
    }

    private boolean isInBasket(Long id, String customerUsername, Date start, Date end) {
        if (!regularExpressions.idIdValid(id) && !regularExpressions.isValidSomeName(customerUsername)) {
            return true;
        }
        EndUser endUser = endUserRepository.findByUserUsername(customerUsername);
        if(!endUser.isCanReserve()){
            return true;
        }
        Advertisement checkAdv = advertisementRepository.findOneByCarAdId(id);
        ArrayList<WishlistItem> itemsInBasket = wishlistRepository.findAllByCustomerUsernameAndAdvertisementId
                (customerUsername, checkAdv.getId());

        for (WishlistItem r : itemsInBasket) {
            if (isDateBetween(r.getStartDate(), r.getEndDate(), start) || isDateBetween(r.getStartDate(), r.getEndDate(), end)){
                return true;
            }
        }

        return false;
    }

    private boolean isDateBetween(Date start, Date end, Date check) {
        return check.after(start) && check.before(end);
    }

    public boolean addToWishlist(Long id, String customerUsername, String startDate, String endDate) {
        Date start = new Date(Long.parseLong(startDate));
        Date end = new Date(Long.parseLong(endDate));
        if (!isInBasket(id, customerUsername, start, end)) {
            Advertisement adv = advertisementRepository.findOneByCarAdId(id);
            WishlistItem wishlistItem = new WishlistItem(start, end, adv,
                    advertisementService.countPricePerAdv(start, end, adv.getPricelist()), false, customerUsername);
            wishlistRepository.save(wishlistItem);
            return true;
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
        LocalDateTime currentDate = LocalDateTime.now();
        Request request = requestRepository.findOneById(id);
        LocalDateTime requestCreationDate = DateConverter.dateToDateTime(request.getDateCreated().toString());
        long hrs = Math.abs(Duration.between(currentDate, requestCreationDate).toHours());
        if (request.getUser().getUsername().equals(username) && request.getState().equals("RESERVED")
            && hrs < 12) {
            request.setState("PAID");
            requestRepository.save(request);

            RequestWrapper wrapper = request.getRequestWrapper();
            boolean completedPayment = true;
            for (Request r : wrapper.getRequests()) {       // ovo u slucaju da je zahtev iz bundle-a
                if (!r.getState().equals("PAID")) {
                    completedPayment = false;
                }
            }

            if (completedPayment) {
                wrapper.setState("PAID");
                requestWrapperRepository.save(wrapper);
            }

            try{
                PayOrderResponse response = orderClient.payOrder(request.getMicroId(), request.getUser().getUsername());
            }catch (Exception e){
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public Request orderToRequest(Order o) {
        Request r = new Request();
        r.setSender(entrepreneurRepository.findByUserUsername(o.getAgentUsername()));
        r.setMicroId(o.getMicroId());
        r.setState(o.getState());
        r.setDateCreated(o.getDateCreated().toGregorianCalendar().getTime());
        r.setUser(userRepository.findOneByUsername(o.getCustomerUsername()));
        r.setMileage(o.getMileage());
        r.setEndDate(o.getEndDate().toGregorianCalendar().getTime());
        r.setStartDate(o.getStartDate().toGregorianCalendar().getTime());
        Set<Car> cars = new HashSet<>();
        for (Long id : o.getCars()) {
            cars.add(carRepository.findOneByMicroId(id));
        }
        r.setCars(cars);

        return r;
    }
}
