package orders.ordersmicroservice.service;


import orders.ordersmicroservice.config.RabbitMQConfiguration;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class MessageProducer {

    private RabbitTemplate rabbitTemplate;

    public MessageProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void send(boolean ok, Long id) {
       // rabbitTemplate.convertAndSend(RabbitMQConfiguration.QUEUE_NAME, car.toBytes());
      //  rabbitTemplate.convertAndSend(RabbitMQConfiguration.QUEUE_NAME, car.toBytes());
        HashMap<Long, Boolean> ret = new HashMap<Long, Boolean>();
        ret.put(id,ok);
        rabbitTemplate.convertAndSend(RabbitMQConfiguration.EXCHANGE_NAME, RabbitMQConfiguration.ROUTING_KEY_RESPONSE, ret);
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