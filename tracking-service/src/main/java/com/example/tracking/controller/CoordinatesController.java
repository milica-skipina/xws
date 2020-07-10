package com.example.tracking.controller;

import com.example.tracking.service.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@Configuration
@EnableScheduling
@RequestMapping(value = "/coordinates")
public class CoordinatesController {

    @Autowired
    private MessageProducer messageProducer;

    public ArrayList<double[]> coordinates;

    public CoordinatesController(){
        coordinates = new ArrayList<>(13);
        coordinates.add(new double[]{45.2464362, 19.8517172});
        coordinates.add(new double[]{45.270171, 19.846985});
        coordinates.add(new double[]{45.280563, 19.908440});
        coordinates.add(new double[]{45.252534, 20.011437});
        coordinates.add(new double[]{45.173203, 20.077355});
        coordinates.add(new double[]{45.101517, 20.110314});
        coordinates.add(new double[]{45.062704, 20.160233});
        coordinates.add(new double[]{45.030685, 20.182206});
        coordinates.add(new double[]{44.980194, 20.223405});
        coordinates.add(new double[]{44.942297, 20.279710});
        coordinates.add(new double[]{44.904375, 20.301682});
        coordinates.add(new double[]{44.836247, 20.267350});
        coordinates.add(new double[]{44.818892, 20.428099});

        this.i = 0;
    }

    private int i;

    @Scheduled(fixedDelay = 1500)
    public void asd(){
        double[] arr = coordinates.get(i);
        String message = arr[0] + "," + arr[1];
        messageProducer.send(message);
        System.out.println(message);
        if(i < 12)
            this.i  = this.i +1;
        else
            this.i = 0;
    }
}
