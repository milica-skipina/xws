package com.example.adservice.service;

import com.example.adservice.config.RabbitMQConfiguration;
import com.example.adservice.model.Coords;
import com.example.adservice.repository.CoordinatesRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoordinatesReceiver {

    @Autowired
    private CoordinatesRepository coordinatesRepository;

    @RabbitListener(queues = RabbitMQConfiguration.QUEUE_COORDINATES)
    public void listen(String message) {
        System.out.println(message);
        if(message != null && !message.equals("")) {
            String[] split = message.split(",");
            double x = Double.parseDouble(split[0]);
            double y = Double.parseDouble(split[1]);

            long id = 1;
            Coords curr = coordinatesRepository.findOneById(id);
            if(curr != null) {
                curr.setX(x);
                curr.setY(y);
                coordinatesRepository.save(curr);
            }
        }
    }

}
