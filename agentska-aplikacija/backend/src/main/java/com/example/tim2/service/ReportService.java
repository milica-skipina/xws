package com.example.tim2.service;

import com.example.tim2.datavalidation.RegularExpressions;
import com.example.tim2.dto.ReportDTO;
import com.example.tim2.model.Car;
import com.example.tim2.model.Report;
import com.example.tim2.repository.CarRepository;
import com.example.tim2.repository.ReportRepository;
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
        report.setText(Encode.forHtml(report.getText()));
        Car car = carRepository.findOneById(id);
        Double newMileage = car.getMileage() + report.getMileage();
        car.setMileage(newMileage);
        carRepository.save(car);
        Report r = new Report();
        r.setText(report.getText());
        r.setReportCar(car);
        r.setMileage(report.getMileage());
        reportRepository.save(r);
        return new ReportDTO(r);
    }
}
