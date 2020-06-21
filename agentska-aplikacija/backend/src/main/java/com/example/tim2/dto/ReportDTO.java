package com.example.tim2.dto;

import com.example.tim2.model.Car;
import com.example.tim2.model.Report;

public class ReportDTO {

    private Long id;
    private String text;
    private Car reportCar;
    private Double mileage;

    public  ReportDTO(){

    }

    public ReportDTO(Report r){
        this(r.getId(),r.getText(),r.getReportCar(), r.getMileage());
    }

    public ReportDTO(Long id, String text, Car car, Double mileage){
        this.id = id;
        this.text = text;
        this.reportCar = car;
        this.mileage = mileage;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Car getReportCar() {
        return reportCar;
    }

    public void setReportCar(Car reportCar) {
        this.reportCar = reportCar;
    }

    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }
}
