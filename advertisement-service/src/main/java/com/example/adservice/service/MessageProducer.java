package com.example.adservice.service;

import com.example.adservice.config.RabbitMQConfiguration;
import com.example.adservice.dto.AdvertisementOrderDTO;
import com.example.adservice.dto.CarOrderDTO;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {

    private RabbitTemplate rabbitTemplate;

    public MessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(AdvertisementOrderDTO ad) {
       // rabbitTemplate.convertAndSend(RabbitMQConfiguration.QUEUE_NAME, car.toBytes());
      //  rabbitTemplate.convertAndSend(RabbitMQConfiguration.QUEUE_NAME, car.toBytes());
        rabbitTemplate.convertAndSend(RabbitMQConfiguration.EXCHANGE_NAME, RabbitMQConfiguration.ROUTING_KEY, ad);
       // rabbitTemplate.convertAndSend("", RabbitMQConfiguration.QUEUE_NAME, car);
    }

   /* private ConnectionFactory factory;

    @Autowired
    public MessageProducer(ConnectionFactory factory) {
        this.factory = factory;
    }

    public void send(String message) throws IOException, TimeoutException {
        String queueName = RabbitMQConfiguration.QUEUE_NAME;

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(queueName, false, false, false, null);
        channel.basicPublish("", queueName, null, message.getBytes());
        channel.close();
        connection.close();
    }*/

}