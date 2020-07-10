package orders.ordersmicroservice.service;

import orders.ordersmicroservice.dto.CarOrderDTO;
import orders.ordersmicroservice.model.Car;
import orders.ordersmicroservice.model.Report;
import orders.ordersmicroservice.model.Request;
import orders.ordersmicroservice.repository.CarRepository;
import orders.ordersmicroservice.repository.ReportRepository;
import orders.ordersmicroservice.repository.RequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private RequestRepository requestRepository;

    public boolean checkTracking(Long id){
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        List<Request> requestsReserved = requestRepository.findAllByEndDateLessThanEqualOrStartDateGreaterThanEqualAndStateAndCarsId(now,now,"RESERVED",id);
        List<Request> requestsPaid = requestRepository.findAllByEndDateLessThanEqualOrStartDateGreaterThanEqualAndStateAndCarsId(now,now,"PAID",id);
        if(!requestsReserved.isEmpty() || !requestsPaid.isEmpty()){
            return true;
        }
        return false;
    }

    public List<CarOrderDTO> getStatistics(String  username){
        List<Car> cars = carRepository.findAllByEntrepreneurUsername(username);
        List<CarOrderDTO> ret = new ArrayList<>();
        List<Report> reports = new ArrayList<>();
        Double sum = 0.0;
        CarOrderDTO carDTO;
        for(Car c: cars){
            reports = reportRepository.findAllByCarId(c.getId());
            carDTO = new CarOrderDTO(c);
            sum = reports.stream().filter(r -> r.getPredjeniKilometri() > 0).mapToDouble(r -> r.getPredjeniKilometri()).sum();
            carDTO.setMileageLimit(sum);
            ret.add(carDTO);
        }
        ret.sort(Comparator.comparing(CarOrderDTO::getMileageLimit).reversed());
        if(ret.size() > 5) {
            return ret.subList(0, 5);
        }
        return ret;
    }

}
