package orders.ordersmicroservice.service;

import orders.ordersmicroservice.common.DateConverter;
import orders.ordersmicroservice.common.RegularExpressions;
import orders.ordersmicroservice.config.TLSConfiguration;
import orders.ordersmicroservice.dto.*;
import orders.ordersmicroservice.model.*;
import orders.ordersmicroservice.repository.*;
import orders.ordersmicroservice.template.RestTemplateExample;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.xws_tim2.AddOrderResponse;
import rs.ac.uns.ftn.xws_tim2.Order;
import rs.ac.uns.ftn.xws_tim2.Wrapper;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class RequestService {

    private RestTemplate restTemplate;

    private RegularExpressions regularExpressions;

    private RestTemplateExample restTemplateExample;

    private RequestRepository requestRepository;

    private CarRepository carRepository;

    private AdvertisementService advertisementService;

    private WishlistRepository wishlistRepository;

    private RequestWrapperRepository requestWrapperRepository;

    private AdvertisementRepository advertisementRepository;

    @Autowired
    public RequestService(RequestRepository requestRepository, RestTemplateExample restTemplate,
                          CarRepository carRepository, AdvertisementService advertisementService,
                          WishlistRepository wishlistRepository, RequestWrapperRepository requestWrapperRepository,
                          AdvertisementRepository advertisementRepository) {
        this.requestRepository = requestRepository;
        this.restTemplateExample = restTemplate;
        this.regularExpressions = new RegularExpressions();
        this.carRepository = carRepository;
        this.advertisementService = advertisementService;
        this.wishlistRepository = wishlistRepository;
        this.requestWrapperRepository = requestWrapperRepository;
        this.advertisementRepository = advertisementRepository;
    }

    public boolean adRequest(Long id){
        if (!regularExpressions.idIdValid(id)) {
            return false;
        }
        List<Request>all = requestRepository.findAllByCarsId(id);
        int x = 0;
        for(Request r : all){
            if(r.getState().equals("PENDING")){
                x = 1;
            }
        }
        if(x == 1){
            return false;
        }else{
            return true;
        }
    }

    public boolean createRequests(ArrayList<BasketDTO> reqs, String[] data, String jwt) {
        Hashtable<String, ArrayList<Request>> bundlePerAgent = new Hashtable<>();
        Set<Request> requests = new HashSet<Request>(1);     // za wrapper
        for (BasketDTO req : reqs) {
            WishlistItem wish = wishlistRepository.findOneById(req.getWishlistId());
            wish.setTurnedIntoRequest(true);
            wishlistRepository.save(wish);
            Advertisement ad = advertisementRepository.findOneById(req.getAdvertisementId());
            Request request = new Request("PENDING", ad.getCarAd().getMileage(), req.getStartDate(),
                    req.getEndDate(), data[0], data[0], new Date());
            request.setAgentUsername(ad.getCarAd().getEntrepreneurUsername());
            request.setAgentNamee(ad.getEntrepreneurName());       // name
            request.setCustomerName(data[1]);
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
                wrapper.setCustomerUsername(request.getCustomerUsername());
                wrapper.setAgentUsername(request.getAgentUsername());
                wrapper.setRequests(requests);
                requestWrapperRepository.save(wrapper);
                request.setRequestWrapper(wrapper);
                requestRepository.save(request);
                requests.clear();

            }
        }
        if (!bundlePerAgent.isEmpty()) {
            createBundle(bundlePerAgent);
        }
        return true;
    }

    /**
     * kljuc je naziv agenta, values svi oglasi tog agenta
     * @param
     * @return
     */
    private Hashtable<String, ArrayList<Request>> hashmapFromReq(Request req, Hashtable<String, ArrayList<Request>> bundlePerAgent) {
        if (!bundlePerAgent.containsKey(req.getAgentNamee())) {
            ArrayList<Request> requestIds = new ArrayList<>();
            requestIds.add(req);
            bundlePerAgent.put(req.getAgentNamee(), requestIds);
        }else {
            bundlePerAgent.get(req.getAgentNamee()).add(req);
        }

        return bundlePerAgent;
    }

    private void createBundle(Hashtable<String, ArrayList<Request>> bundlePerAgent) {
        Set<Request> requests = new HashSet<Request>(1);
        for (String key : bundlePerAgent.keySet()) {
            requests.addAll(bundlePerAgent.get(key));
            RequestWrapper wrapper = new RequestWrapper();
            wrapper.setState("PENDING");
            wrapper.setCustomerUsername(((Request)bundlePerAgent.get(key).toArray()[0]).getCustomerUsername());
            wrapper.setAgentUsername(((Request)bundlePerAgent.get(key).toArray()[0]).getAgentUsername());
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

    /*
        za korisnika
     */
    public ArrayList<RequestWrapDTO> requestedCars(String username) {
        if (!regularExpressions.isValidCharNum(username)) {
            return null;
        }
        ArrayList<RequestWrapper> requests = (ArrayList<RequestWrapper>) requestWrapperRepository.
                findAllByCustomerUsername(username);
        ArrayList<RequestWrapDTO> ret = castRequests(requests);
        return ret;
    }

    /*
        za agenta
     */
    public ArrayList<RequestWrapDTO> requestsForApproving(String username) {
        if (!regularExpressions.isValidCharNum(username)) {
            return null;
        }
        ArrayList<RequestWrapper> requests = (ArrayList<RequestWrapper>) requestWrapperRepository.
                findAllByAgentUsername(username);
        ArrayList<RequestWrapDTO> ret = castRequests(requests);
        return ret;
    }

    public ArrayList<Wrapper> convertForSoap(String username) {
        ArrayList<Wrapper> wraps = new ArrayList<>();
        ArrayList<RequestWrapper> requests = (ArrayList<RequestWrapper>) requestWrapperRepository.
                findAllByAgentUsername(username);
        for (RequestWrapper w : requests) {
            Wrapper newW = w.getGenerated();
            wraps.add(newW);
        }
        return wraps;
    }

    private ArrayList<RequestWrapDTO> castRequests(ArrayList<RequestWrapper> requests) {
        ArrayList<RequestWrapDTO> temp = new ArrayList<>(10);
        for (RequestWrapper r: requests) {
            HashSet<RequestDTO> ret = new HashSet<>(10);
            for (Request req : r.getRequests() ) {
                RequestDTO dto = new RequestDTO(req);
                dto.setUserId(req.getCustomerName());
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


    public boolean modifyRequest(Long requestWrapperId, boolean flag, String role, String username, String jwt) {
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
            if (flag) {
                List<Long> carIds = findAllByStateAndStartDateAndEndDate("RESERVED", r.getStartDate(), r.getEndDate());
                List<Long> paidCarIds = findAllByStateAndStartDateAndEndDate("PAID", r.getStartDate(), r.getEndDate());
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
                if (role.equals("ROLE_CONSUMER")) {
                    restTemplateExample.changeNumber(username, jwt);
                }
                r.setState("CANCELED");
            }
            r.escapeParameters(r);
            //requestRepository.save(r);
        }

        temp = new ArrayList<>(requestWrapper.getRequests());
        requestRepository.saveAll(temp);

        return true;
    }

    private boolean isInBasket(Long id, String customerUsername, Date start, Date end) {
        if (!regularExpressions.idIdValid(id) && !regularExpressions.isValidCharNum(customerUsername)) {
            return true;
        }

        // TO DO: DODATI REST TEMPLEJT
        /*EndUser endUser = endUserRepository.findByUserUsername(customerUsername);
        if(!endUser.isCanReserve()){
            return true;
        }*/

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
        return (check.after(start) || check.equals(start)) && (check.before(end) || check.equals(end));
    }

    /**
     * data je izvadjeno iz jwta, tu ako ga ne isparsira nece dalje proci
     * @param id
     * @param startDate
     * @param endDate
     * @param customerData - podaci o customeru
     * @param agentData - username i name agenta
     * @return
     */
    public boolean manualRenting(Long id, String startDate, String endDate, String[] customerData, String[] agentData, String jwt) {
        if (!regularExpressions.idIdValid(id) && !regularExpressions.isValidInput(customerData[4])) {
            return false;
        } else if (customerData[0].equals("true")) {        // treba registrovati novog
            if (!regularExpressions.isValidInput(customerData[1]) && !regularExpressions.isValidInput(customerData[3])
                    && !regularExpressions.isValidEmail(customerData[2]) && (customerData[4].length() < 6)) {
                return false;
            } else {
                restTemplateExample.postCreateUser(TLSConfiguration.URL + "authpoint/auth/manual", customerData, jwt);
            }
        }
        after24hOr12h();
        Date start = DateConverter.stringToDate(startDate);
        Date end = DateConverter.stringToDate(endDate);
        ArrayList<Request> reservedRequests = (ArrayList<Request>) requestRepository.findAllByState("PAID");
        for (Request r : reservedRequests) {
            if ( (start.compareTo(r.getEndDate()) < 0 && start.compareTo(r.getStartDate()) > 0 )
                    || (end.compareTo(r.getEndDate()) < 0 && end.compareTo(r.getStartDate()) > 0 )) {
                return false;
            }
        }

        Set<Car> cars = new HashSet<Car>(1);
        Car sagaCar = carRepository.findOneByAdId(id);
        if (sagaCar != null) {
            cars.add(sagaCar);
        } else {
            CarOrderDTO car = restTemplateExample.getCar(TLSConfiguration.URL + "advertisement/car/order/" + id, jwt);
            cars.add(new Car(car));
        }
        Request manualRequest = new Request("PAID", ((Car)cars.toArray()[0]).getMileage(), start, end,
                customerData[4], agentData[0], new Date());
        manualRequest.setAgentNamee(agentData[1]);
        manualRequest.setCustomerName(customerData[1] + " " + customerData[3]);
        manualRequest.setCars(cars);
        HashSet<Request> tempRequests = new HashSet<>(1);
        tempRequests.add(manualRequest);
        RequestWrapper newWrap = new RequestWrapper(tempRequests);
        manualRequest.escapeParameters(manualRequest);
        requestRepository.save(manualRequest);
        requestWrapperRepository.save(newWrap);
        cancelRequestsByManual(start, end);
        return true;
    }

    private void cancelRequestsByManual(Date start, Date end) {
        ArrayList<Request> pendingRequests = (ArrayList<Request>) requestRepository.findAllByState("PENDING");
        for (Request r : pendingRequests) {
            if (start.compareTo(r.getEndDate()) < 0 && start.compareTo(r.getStartDate()) > 0
                    || (end.compareTo(r.getEndDate()) < 0 && end.compareTo(r.getStartDate()) > 0)) {
                r.setState("CANCELED");
                r.getRequestWrapper().setState("CANCELED");
                r.escapeParameters(r);
                requestRepository.save(r);
                requestWrapperRepository.save(r.getRequestWrapper());
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

    public void after24hOr12h() {
        ArrayList<Request> requests = (ArrayList<Request>) requestRepository.findAllByState("PENDING");
        ArrayList<Request> requestsNotPaid = (ArrayList<Request>) requestRepository.findAllByState("RESERVED");
        requests.addAll(requestsNotPaid);
        Date currentDate = new Date();
        for (Request r : requests) {
            Date requestCreationDate = r.getDateCreated();
            long diff = Math.abs(currentDate.getTime() - requestCreationDate.getTime());
            long hrs = TimeUnit.MILLISECONDS.toHours(diff);
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
        if (!regularExpressions.idIdValid(id) && !regularExpressions.isValidCharNum(username)) {
            return false;
        }
        Date currentDate = new Date();
        Request request = requestRepository.findOneById(id);
        Date requestCreationDate = request.getDateCreated();
        long diff = Math.abs(currentDate.getTime() - requestCreationDate.getTime());
        long hrs = TimeUnit.MILLISECONDS.toHours(diff);
        if (request.getCustomerUsername().equals(username) && request.getState().equals("RESERVED")
                && hrs <= 12) {
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

            return  true;
        }

        return false;
    }

    public Boolean canWriteReview(String username, Long id){
        String url = TLSConfiguration.URL + "advertisement/review/username/" + username + "/carId" + id;
        Long reviews = restTemplate.getForObject(url, Long.class);
        Date endDate = new Date();
        Long requestsNum = 0L;
        List<Request> requests = requestRepository.findAllByCustomerUsernameAndEndDateLessThanEqualAndState(username, endDate, "PAID");
        for(Request r : requests){
            for(Car c : r.getCars()){
                if(c.getId() == id){
                    requestsNum++;
                }
            }
        }
        if(reviews < requestsNum){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean messageCheck(String customerUsername,String agentUsername){
        List<Request> requests = requestRepository.findAllByCustomerUsernameAndAgentUsernameAndState(customerUsername,agentUsername,"RESERVED");
        if(requests.isEmpty())
            return false;
        else
            return true;
    }

    public List<Request> findAllByEndDateLessThanEqualOrStartDateGreaterThanEqualAndAgentUsernameAndState(Date endDate,
                                                                                                          Date startDate,
                                                                                                          String username,
                                                                                                          String state) {
        return requestRepository.findAllByEndDateLessThanEqualOrStartDateGreaterThanEqualAndAgentUsernameAndState
                (endDate, startDate,  username, state);
    }


    public Request save(Request newRequest) {
        return requestRepository.save(newRequest);
    }

    public Request requestWrapper(Order o) {

        Request newRequest = new Request();
        newRequest.setState("RESERVED");    // jer ce vamo biti uspesan zahtev na agentu
        newRequest.setMileage(o.getMileage());
        newRequest.setStartDate(o.getStartDate().toGregorianCalendar().getTime());
        newRequest.setEndDate(o.getEndDate().toGregorianCalendar().getTime());
        newRequest.setCustomerUsername(o.getCustomerUsername());
        newRequest.setAgentUsername(o.getAgentUsername());
        newRequest.setDateCreated(o.getDateCreated().toGregorianCalendar().getTime());
        Set<Car> cars = new HashSet<>();
        for (Long id : o.getCars()) {
            cars.add(carRepository.findOneByAdId(id));
        }
        newRequest.setCars(cars);
        save(newRequest);
        RequestWrapper newWrapper = new RequestWrapper(newRequest);
        newWrapper.setRequests(new HashSet<Request>());
        newWrapper.getRequests().add(newRequest);
        requestWrapperRepository.save(newWrapper);
        return newRequest;
    }

    public <T> HttpEntity<T> createAuthHeader(String token, T bodyType) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<T> request = new HttpEntity<>(bodyType, headers);
        return request;
    }

    public boolean addToWishlist(Long carMicroId, String customerUsername, String startDate, String endDate) {
        Date start = new Date(Long.parseLong(startDate));
        Date end = new Date(Long.parseLong(endDate));
        Car car = carRepository.findOneByAdId(carMicroId);
        if (!isInBasket(car.getId(), customerUsername, start, end)) {
            Advertisement adv = advertisementRepository.findOneByCarAdId(car.getId());
            WishlistItem wishlistItem = new WishlistItem(start, end, adv,
                    advertisementService.countPricePerAdv(start, end, adv.getPricelist()), false, customerUsername);
            wishlistItem.setAgentName(adv.getEntrepreneurName());
            wishlistRepository.save(wishlistItem);
            return true;
        }
        return false;
    }

    /**
     * sluzi za proveru kada dodje zahtev sa soapa, ruznije nije moglo
     * @param order
     * @return
     */
    public boolean availableForAgent(Order order, AddOrderResponse response) {
        boolean ret = true;
        ArrayList<Request> requestsForCustomer = requestRepository.findAllByCustomerUsernameAndAgentUsername(
                order.getCustomerUsername(), order.getAgentUsername());
        ArrayList<Request> pendingAgentsRequests = requestRepository.findAllByStateAndAgentUsername("PENDING",
                order.getAgentUsername());
        int contraCounter = 0;      // Da li se desilo da se ni jedan od zahteva tog kupca ne poklapa sa ovim iz agenta?
        for (Request r : requestsForCustomer) {
            if (((Car)r.getCars().toArray()[0]).getAdId() == order.getCars().toArray()[0] &&
               (isDateBetween(r.getStartDate(), r.getEndDate(), order.getStartDate().toGregorianCalendar().getTime()) ||
                 isDateBetween(r.getStartDate(), r.getEndDate(), order.getEndDate().toGregorianCalendar().getTime())) ) {
                if (r.getState().equals("RESERVED") || r.getState().equals("PAID")) {
                    ret = false;
                    response.setMicroId(-1L);       // ne moze se rezervisati
                    break;
                } else if (r.getState().equals("PENDING")) {
                    List<Request> toChange = pendingAgentsRequests.stream()
                                                            .filter(el -> el.getId() == order.getMicroId())
                                                            .collect(Collectors.toList() );
                    if (toChange.isEmpty()) {       // isti takav zahtev ne postoji na mikro, sve ostale ciji se datumi poklapaju treba ponistiti
                        Long microID = createNewRequestFromSoap(order, (Car)r.getCars().toArray()[0]);
                        response.setMicroId(microID);
                        toChange = pendingAgentsRequests.stream()
                                .filter(el -> el.getId() == r.getId())
                                .collect(Collectors.toList() );
                        for (Request rx : toChange) {
                            rx.setState("CANCELED");
                            RequestWrapper wrap = rx.getRequestWrapper();
                            wrap.setState("CANCELED");
                            requestWrapperRepository.save(wrap);
                        }
                        requestRepository.saveAll(new ArrayList<>(toChange));
                    } else {
                        ((Request)toChange.toArray()[0]).setState("RESERVED");      // znaci kupac je zahtevao auto koristeci mikro
                         requestRepository.save(((Request)toChange.toArray()[0])); // i taj zahtev je otisao na agent pa se tamo odobrava
                        RequestWrapper wrapper = ((Request)toChange.toArray()[0]).getRequestWrapper();
                        wrapper.setState("RESERVED");
                        requestWrapperRepository.save(wrapper);
                    }
                }
            } else {    //
                contraCounter++;
            }
        }

        if (contraCounter == requestsForCustomer.size()) {   // ni jedan oglas se nije poklopio
            Long microId = createNewRequestFromSoap(order, carRepository.findOneByAdId((Long)order.getCars().toArray()[0]));
            response.setMicroId(microId);
        }

        return ret;
    }

    private Long createNewRequestFromSoap(Order order, Car car) {
        Request addNew = new Request(order);
        addNew.setState("RESERVED");
        RequestWrapper wrapper = new RequestWrapper(addNew);
        addNew.setRequestWrapper(wrapper);
        HashSet<Car> carsForNew = new HashSet<>();
        carsForNew.add(car);
        addNew.setCars(carsForNew);
        addNew = requestRepository.save(addNew);
        requestWrapperRepository.save(wrapper);

        return addNew.getId();
    }
}
