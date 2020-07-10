package orders.ordersmicroservice.service;

import com.rabbitmq.client.*;
import orders.ordersmicroservice.config.RabbitMQConfiguration;
import orders.ordersmicroservice.dto.AdvertisementDTO;
import orders.ordersmicroservice.dto.CarOrderDTO;
import orders.ordersmicroservice.model.Advertisement;
import orders.ordersmicroservice.model.Car;
import orders.ordersmicroservice.model.Pricelist;
import orders.ordersmicroservice.repository.AdvertisementRepository;
import orders.ordersmicroservice.repository.CarRepository;
import orders.ordersmicroservice.repository.PricelistRepository;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;
import org.springframework.amqp.core.Message;

@Service
public class MessageBugReceiver {

    private AdvertisementRepository advertisementRepository;

    private CarRepository carRepository;

    private PricelistRepository pricelistRepository;

    private MessageProducer messageProducer;

    @Autowired
    public MessageBugReceiver(AdvertisementRepository advertisementRepository, CarRepository carRepository,
                              PricelistRepository pricelistRepository, MessageProducer messageProducer) {
        this.advertisementRepository = advertisementRepository;
        this.carRepository = carRepository;
        this.pricelistRepository = pricelistRepository;
        this.messageProducer = messageProducer;
        //  init();
    }

    @RabbitListener(queues = RabbitMQConfiguration.QUEUE_NAME)
    public void recievedMessage(AdvertisementDTO ad) {
        Pricelist p = pricelistRepository.findOneByAdId(ad.getPricelist().getId());

        if (p == null) {
            p = new Pricelist(ad.getPricelist());
            p = pricelistRepository.save(p);
        }

        Car c = carRepository.findOneByAdId(ad.getCarAd().getId());
        if (c == null) {
            c = carRepository.save(new Car(ad.getCarAd()));
        } else {
            c.setMileage(ad.getCarAd().getMileage());
            c.setMileageLimit(ad.getCarAd().getMileageLimit());
            c = carRepository.save(c);
        }



        Advertisement advertisement = advertisementRepository.findOneByAdId(ad.getId());
        if (advertisement == null) {
            advertisement = new Advertisement(ad);
        } else {
            advertisement.setEndDate(ad.getEndDate());
            advertisement.setStartDate(ad.getStartDate());
        }
        advertisement.setCarAd(c);
        advertisement.setPricelist(p);
        try {
            advertisementRepository.save(advertisement);
            messageProducer.send(true, advertisement.getAdId());
        } catch (Exception e) {
            e.printStackTrace();
            messageProducer.send(false, null);
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
