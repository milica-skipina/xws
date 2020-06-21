package orders.ordersmicroservice.service;

import orders.ordersmicroservice.dto.CarOrderDTO;
import orders.ordersmicroservice.model.Car;
import orders.ordersmicroservice.model.Report;
import orders.ordersmicroservice.repository.CarRepository;
import orders.ordersmicroservice.repository.ReportRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class CarService {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ReportRepository reportRepository;

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
