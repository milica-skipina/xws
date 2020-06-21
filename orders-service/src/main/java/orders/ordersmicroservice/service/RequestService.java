package orders.ordersmicroservice.service;

import orders.ordersmicroservice.common.DateConverter;
import orders.ordersmicroservice.common.RegularExpressions;
import orders.ordersmicroservice.config.TLSConfiguration;
import orders.ordersmicroservice.dto.BasketDTO;
import orders.ordersmicroservice.dto.CarOrderDTO;
import orders.ordersmicroservice.dto.MiniCarDTO;
import orders.ordersmicroservice.dto.RequestDTO;
import orders.ordersmicroservice.model.Advertisement;
import orders.ordersmicroservice.model.Car;
import orders.ordersmicroservice.model.Request;
import orders.ordersmicroservice.repository.CarRepository;
import orders.ordersmicroservice.repository.RequestRepository;
import orders.ordersmicroservice.template.RestTemplateExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rs.ac.uns.ftn.xws_tim2.Order;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class RequestService {

    private RestTemplate restTemplate;

    private RegularExpressions regularExpressions;

    private RestTemplateExample restTemplateExample;

    private RequestRepository requestRepository;

    private CarRepository carRepository;

    @Autowired
    public RequestService(RequestRepository requestRepository, RestTemplateExample restTemplate, CarRepository carRepository) {
        this.requestRepository = requestRepository;
        this.restTemplateExample = restTemplate;
        this.regularExpressions = new RegularExpressions();
        this.carRepository = carRepository;
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

    public boolean createRequests(String[] reqs, String[] data, String jwt) {
        if (!validateRequests(reqs)) {
            return false;
        }
        Hashtable<String, ArrayList<BasketDTO>> bundlePerAgent = new Hashtable<>();
        ArrayList<Long> advertisementIds = parseInput(reqs, 0);
        Hashtable<Long, BasketDTO> advertisements = restTemplateExample
                                                    .postSender(TLSConfiguration.URL +
                                                     "advertisement/advertisement/filter", advertisementIds, jwt);
        Date startDate = new Date(Long.parseLong(reqs[reqs.length-2]));
        Date endDate = new Date(Long.parseLong(reqs[reqs.length-1]));
        for (int i= 0; i < reqs.length-2 ; i++) {
            String[] array = reqs[i].split("\\+");
            if (array.length == 3) {        // znaci taj oglas ide u bundle
                bundlePerAgent = hashmapFromReq(reqs[i], bundlePerAgent, advertisements);
            } else {    // pojedinacni zahtev
                BasketDTO adv = advertisements.get(Long.parseLong(array[0]));
                Request request = new Request("PENDING", adv.getCar().getMileage(), startDate, endDate,
                                  data[0], adv.getEntrepreneur(), new Date());
                Set<Car> cars = new HashSet<Car>(1);
                cars.add(new Car(adv.getCar()));
                request.setCars(cars);
                request.setAgentUsername(adv.getCar().getEntrepreneurUsername());
                request.setAgentNamee(adv.getEntrepreneur());       // name
                request.setCustomerName(data[1]);
                request.escapeParameters(request);
                requestRepository.save(request);
            }
        }
        if (!bundlePerAgent.isEmpty()) {
            createBundle(bundlePerAgent, data, startDate,  endDate);
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

    private <E> ArrayList<E> parseInput(String[] array, int x) {
        ArrayList<E> ret = new ArrayList<>();
        for (int i= 0; i < array.length-2 ; i++) {
            String[] rez = array[i].split("\\+");
            ret.add((E) rez[x]);
        }

        return ret;
    }
    /**
     * kljuc je naziv agenta, values svi oglasi tog agenta
     * @param
     * @return
     */
    private Hashtable<String, ArrayList<BasketDTO>> hashmapFromReq(String s, Hashtable<String, ArrayList<BasketDTO>> bundlePerAgent,
                                                              Hashtable<Long, BasketDTO> advertisements) {
        if (!bundlePerAgent.containsKey(s.split("\\+")[1])) {
            ArrayList<BasketDTO> advIds = new ArrayList<>();
            advIds.add(advertisements.get(Long.parseLong(s.split("\\+")[0])));
            bundlePerAgent.put(s.split("\\+")[1], advIds);
        }else {
            bundlePerAgent.get(s.split("\\+")[1]).add(advertisements.get(Long.parseLong(s.split("\\+")[0])));
        }

        return bundlePerAgent;
    }

    private void createBundle(Hashtable<String, ArrayList<BasketDTO>> bundlePerAgent, String[] customerData,
                              Date startDate, Date endDate) {
        Set<Car> cars = new HashSet<Car>(1);
        for (String key : bundlePerAgent.keySet()) {    //agent je key
            Request request = new Request("PENDING", -1, startDate, endDate, customerData[0], key, new Date());
            for (BasketDTO adv : bundlePerAgent.get(key)) {   // za sve oglase tog agenta dodati aute u jedan zahtev
                request.setAgentUsername(adv.getCar().getEntrepreneurUsername());
                request.setAgentNamee(adv.getEntrepreneur());       // name
                request.setCustomerName(customerData[1]);
                request.setAgentNamee(key);
                request.setMileage(adv.getCar().getMileage());
                cars.add(new Car(adv.getCar()));
                request.setCars(cars);
                request.escapeParameters(request);
                requestRepository.save(request);
            }
        }
    }

    /*
        za korisnika
     */
    public ArrayList<RequestDTO> requestedCars(String username) {
        if (!regularExpressions.isValidCharNum(username)) {
            return null;
        }
        ArrayList<Request> requests = (ArrayList<Request>) requestRepository.findAllByCustomerUsername(username);
        ArrayList<RequestDTO> ret = castRequests(requests);
        return ret;
    }

    /*
        za agenta
     */
    public ArrayList<RequestDTO> requestsForApproving(String username) {
        if (!regularExpressions.isValidCharNum(username)) {
            return null;
        }
        ArrayList<Request> requests = (ArrayList<Request>) requestRepository.findAllByAgentUsername(username);
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

    public boolean isInBasket(Long id, String customerUsername) {
        if (!regularExpressions.idIdValid(id) && !regularExpressions.isValidCharNum(customerUsername)) {
            return false;
        }
        ArrayList<Request> requests = (ArrayList<Request>) requestRepository.findAllByCustomerUsername(customerUsername);
        for (Request r : requests) {
            if (!r.getState().equals("CANCELED")) {
                for (Car c: r.getCars()) {
                    if (c.getId() == id) {
                        return true;
                    }
                }
            }

        }
        return false;
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
        CarOrderDTO car = restTemplateExample.getCar(TLSConfiguration.URL + "advertisement/car/order/" + id, jwt);
        Request manualRequest = new Request("PAID", car.getMileage(), start, end, customerData[4], agentData[0], new Date());
        manualRequest.setAgentNamee(agentData[1]);
        manualRequest.setCustomerName(customerData[1] + " " + customerData[3]);
        Set<Car> cars = new HashSet<Car>(1);
        cars.add(new Car(car));
        manualRequest.setCars(cars);
        manualRequest.escapeParameters(manualRequest);
        requestRepository.save(manualRequest);
        cancelRequestsByManual(start, end);
        return true;
    }

    private void cancelRequestsByManual(Date start, Date end) {
        ArrayList<Request> pendingRequests = (ArrayList<Request>) requestRepository.findAllByState("PENDING");
        pendingRequests.addAll(requestRepository.findAllByState("RESERVED"));
        for (Request r : pendingRequests) {
            if (start.compareTo(r.getEndDate()) < 0 && start.compareTo(r.getStartDate()) > 0
                    || (end.compareTo(r.getEndDate()) < 0 && end.compareTo(r.getStartDate()) > 0)) {
                r.setState("CANCELED");
                requestRepository.save(r);
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
            return true;
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
        newRequest.setState(o.getState());
        newRequest.setMileage(o.getMileage());
        newRequest.setStartDate(o.getStartDate().toGregorianCalendar().getTime());
        newRequest.setEndDate(o.getEndDate().toGregorianCalendar().getTime());
        newRequest.setCustomerUsername(o.getCustomerUsername());
        newRequest.setAgentUsername(o.getAgentUsername());
        newRequest.setDateCreated(o.getDateCreated().toGregorianCalendar().getTime());
        Set<Car> cars = new HashSet<>();
        for (Long id : o.getCars()) {
            cars.add(carRepository.findOneById(id));
        }
        save(newRequest);
        return newRequest;
    }
}
