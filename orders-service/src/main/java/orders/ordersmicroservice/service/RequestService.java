package orders.ordersmicroservice.service;

import orders.ordersmicroservice.common.DateConverter;
import orders.ordersmicroservice.dto.BasketDTO;
import orders.ordersmicroservice.dto.CarOrderDTO;
import orders.ordersmicroservice.dto.MiniCarDTO;
import orders.ordersmicroservice.dto.RequestDTO;
import orders.ordersmicroservice.model.Advertisement;
import orders.ordersmicroservice.model.Car;
import orders.ordersmicroservice.model.Request;
import orders.ordersmicroservice.repository.RequestRepository;
import orders.ordersmicroservice.template.RestTemplateExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

@Service
public class RequestService {

    private RestTemplate restTemplate;

    public boolean adRequest(Long id){
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

    private RestTemplateExample restTemplateExample;

    private RequestRepository requestRepository;

    @Autowired
    public RequestService(RequestRepository requestRepository, RestTemplateExample restTemplate) {
        this.requestRepository = requestRepository;
        this.restTemplateExample = restTemplate;
    }

    public boolean createRequests(String[] reqs, String[] data) {
        Hashtable<String, ArrayList<BasketDTO>> bundlePerAgent = new Hashtable<>();
        ArrayList<Long> advertisementIds = parseInput(reqs, 0);
        Hashtable<Long, BasketDTO> advertisements = restTemplateExample
                                                    .postSender("https://localhost:8083/advertisement/filter", advertisementIds);
        Date startDate = new Date(Long.parseLong(reqs[reqs.length-2]));
        Date endDate = new Date(Long.parseLong(reqs[reqs.length-1]));
        for (int i= 0; i < reqs.length-2 ; i++) {
            String[] array = reqs[i].split("\\+");
            if (array.length == 3) {        // znaci taj oglas ide u bundle
                bundlePerAgent = hashmapFromReq(reqs[i], bundlePerAgent, advertisements);
            } else {    // pojedinacni zahtev
                BasketDTO adv = advertisements.get(Long.parseLong(array[0]));
                Request request = new Request("PENDING", adv.getCar().getMileage(), startDate, endDate,
                                  data[0], adv.getEntrepreneur());
                Set<Car> cars = new HashSet<Car>(1);
                cars.add(new Car(adv.getCar()));
                request.setCars(cars);
                request.setAgentUsername(adv.getCar().getEntrepreneurUsername());
                request.setAgentNamee(adv.getEntrepreneur());       // name
                request.setCustomerName(data[1]);
                requestRepository.save(request);
            }
        }
        if (!bundlePerAgent.isEmpty()) {
            createBundle(bundlePerAgent, data, startDate,  endDate);
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
            Request request = new Request("PENDING", -1, startDate, endDate, customerData[0], key);
            for (BasketDTO adv : bundlePerAgent.get(key)) {   // za sve oglase tog agenta dodati aute u jedan zahtev
                request.setAgentUsername(adv.getCar().getEntrepreneurUsername());
                request.setAgentNamee(adv.getEntrepreneur());       // name
                request.setCustomerName(customerData[1]);
                request.setAgentNamee(key);
                request.setMileage(adv.getCar().getMileage());
                cars.add(new Car(adv.getCar()));
                request.setCars(cars);
                requestRepository.save(request);
            }
        }
    }

    /*
        za korisnika
     */
    public ArrayList<RequestDTO> requestedCars(String username) {
        ArrayList<Request> requests = (ArrayList<Request>) requestRepository.findAllByCustomerUsername(username);
        ArrayList<RequestDTO> ret = castRequests(requests);
        return ret;
    }

    /*
        za agenta
     */
    public ArrayList<RequestDTO> requestsForApproving(String username) {
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
        //  TO DO: VALIDIRAJ ID I FLAG!!!!!!
        Request r = requestRepository.findOneById(requestId);
        if (r == null) {
            return false;
        }
        if (flag && r.getState().equals("PENDING")) {   // u slucaju da je manuelno zauzet u medjuvremenu
            r.setState("RESERVED");
        } else {
            r.setState("CANCELED");
        }
        requestRepository.save(r);
        return true;
    }

    public boolean isInBasket(Long id, String customerUsername) {
        ArrayList<Request> requests = (ArrayList<Request>) requestRepository.findAllByCustomerUsername(customerUsername);
        for (Request r : requests) {
            for (Car c: r.getCars()) {
                if (c.getId() == id) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean manualRenting(Long id, String startDate, String endDate, String customerUsername, String[] data) {
        Date start = new Date(Long.parseLong(startDate));
        Date end = new Date(Long.parseLong(endDate));
        ArrayList<Request> reservedRequests = (ArrayList<Request>) requestRepository.findAllByState("RESERVED");
        for (Request r : reservedRequests) {
            if ( (start.compareTo(r.getEndDate()) < 0 && start.compareTo(r.getStartDate()) > 0 )
                    || (end.compareTo(r.getEndDate()) < 0 && end.compareTo(r.getStartDate()) > 0 )) {
                return false;
            }
        }
        CarOrderDTO car = restTemplateExample.getCar("https://localhost:8083/advertisement/",id);
        Request manualRequest = new Request("RESERVED", -1, start, end, customerUsername, data[0]);
        manualRequest.setMileage(car.getMileage());
        manualRequest.setAgentNamee(data[1]);
        manualRequest.setCustomerName(customerUsername);
        Set<Car> cars = new HashSet<Car>(1);
        cars.add(new Car(car));
        manualRequest.setCars(cars);
        requestRepository.save(manualRequest);


        return true;
    }

    private void cancelRequestsByManual(Date start, Date end) {
        ArrayList<Request> pendingRequests = (ArrayList<Request>) requestRepository.findAllByState("PENDING");
        for (Request r : pendingRequests) {
            if (start.compareTo(r.getEndDate()) < 0 && start.compareTo(r.getStartDate()) > 0
                    || (end.compareTo(r.getEndDate()) < 0 && end.compareTo(r.getStartDate()) > 0)) {
                r.setState("CANCELED");
                requestRepository.save(r);
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


}
