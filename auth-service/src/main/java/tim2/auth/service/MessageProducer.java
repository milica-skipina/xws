package tim2.auth.service;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tim2.auth.rabbitmq.RabbitMQConfiguration;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Service
public class MessageProducer {


    private ConnectionFactory factory;

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
    }

}