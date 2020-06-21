package orders.ordersmicroservice.service;

import orders.ordersmicroservice.common.RegularExpressions;
import orders.ordersmicroservice.dto.ReportDTO;
import orders.ordersmicroservice.model.Car;
import orders.ordersmicroservice.model.Report;
import orders.ordersmicroservice.repository.CarRepository;
import orders.ordersmicroservice.repository.ReportRepository;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReportService {


    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private CarRepository carRepository;

    public ReportDTO createReport(ReportDTO report, Long id){
        RegularExpressions regExp = new RegularExpressions();
        if(!regExp.idIdValid(id)){
            return null;
        }
        Car car = carRepository.findOneById(id);
        if(car == null){
            return  null;
        }
        Double newMileage = car.getMileage() + report.getMileage();
        car.setMileage(newMileage);
        carRepository.save(car);
        Report r = new Report();
        report.setText(Encode.forHtml(report.getText()));
        r.setAdditional_text(report.getText());
        r.setCar(car);
        r.setPredjeniKilometri(report.getMileage());
        reportRepository.save(r);
        return new ReportDTO(r);
    }

}
