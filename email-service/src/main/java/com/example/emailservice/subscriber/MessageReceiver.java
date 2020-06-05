package com.example.emailservice.subscriber;


import com.example.emailservice.config.ApplicationConfiguration;
import com.example.emailservice.config.RabbitMQConfiguration;
import com.example.emailservice.service.EmailService;
import com.rabbitmq.client.*;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import javax.mail.MessagingException;

@Service
public class MessageReceiver {

    private final ApplicationConfiguration configuration;
    private final ConnectionFactory factory;
    private EmailService emailService;

    public MessageReceiver(ApplicationConfiguration configuration,
                           ConnectionFactory factory, EmailService emailService){
        this.configuration = configuration;
        this.factory = factory;
        this.emailService = emailService;
        init();
    }

    private void init() {
        try {
            String queueName = RabbitMQConfiguration.QUEUE_NAME;
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            channel.queueDeclare(queueName, false, false, false, null);

            Consumer consumer = new DefaultConsumer(channel) {


                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {
                    String log = new String(body, StandardCharsets.UTF_8); //jwt + " " + email
                    String[] arr =  log.split(" ");
                    System.out.println(log);
                    try {
                        emailService.sendHtmlMail(arr[1], "Verivication mail", "<html><body>Confirm account activation on link.</br><a href=\"https://localhost:8082/authpoint/user/verify/"+arr[0]+"\">Activation link</a><body/></html>");
                    }catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            };

            channel.basicConsume(queueName, true, consumer);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }
}
