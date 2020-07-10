package com.example.adservice.service;

import com.example.adservice.config.RabbitMQConfiguration;
import com.example.adservice.model.Advertisement;
import com.example.adservice.repository.AdvertisementRepository;
import com.example.adservice.repository.CarRepository;
import com.example.adservice.repository.PricelistRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class MessageRabbitReceiver {

    private AdvertisementRepository advertisementRepository;

    private CarRepository carRepository;

    private PricelistRepository pricelistRepository;

    private MessageProducer messageProducer;

    @Autowired
    public MessageRabbitReceiver(AdvertisementRepository advertisementRepository, CarRepository carRepository, PricelistRepository pricelistRepository, MessageProducer messageProducer) {
        this.advertisementRepository = advertisementRepository;
        this.carRepository = carRepository;
        this.pricelistRepository = pricelistRepository;
        this.messageProducer = messageProducer;
        //  init();
    }

    @RabbitListener(queues = RabbitMQConfiguration.QUEUE_NAME)
    public void recievedMessage(HashMap<Long, Boolean> message) {
    Iterator it = message.entrySet().iterator();
    while(it.hasNext()){
        Map.Entry pair = (Map.Entry)it.next();
        if((Boolean) pair.getValue()){
            Advertisement ad = advertisementRepository.findOneById((Long) pair.getKey());
            ad.setState("APPROVED");
            advertisementRepository.save(ad);
        }
    }
        // System.out.println("Recieved Message: " + message.getMessageProperties().getHeaders().get("__TypeId__").toString());
       // String body = new String(message.getBody(), StandardCharsets.UTF_8);
    }
/*
    private void init() {
        try {
            String queueName = RabbitMQConfiguration.QUEUE_NAME;
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUri("amqp://qphutegs:X3f6xAhz9VClTmtXksUBAbhLzGci5wRI@roedeer.rmq.cloudamqp.com/qphutegs");
            //factory.setRequestedHeartbeat(30);
            //factory.setConnectionTimeout(30);
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(queueName, true, false, false, null);

            Consumer consumer = new DefaultConsumer(channel);

            channel.basicConsume(queueName, true, consumer);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }
 */
}
