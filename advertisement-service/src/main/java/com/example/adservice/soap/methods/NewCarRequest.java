package com.example.adservice.soap.methods;

import com.example.adservice.model.Car;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "car"})
@XmlRootElement(namespace = "http://ftn.uns.ac.rs/xws_tim2", name = "newCarRequest")
public class NewCarRequest {

    @XmlElement(required = true)
    private Car car;

    public NewCarRequest(){

    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}
