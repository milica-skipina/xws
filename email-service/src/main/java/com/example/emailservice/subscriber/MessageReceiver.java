package com.example.emailservice.subscriber;


import com.example.emailservice.config.ApplicationConfiguration;
import com.example.emailservice.config.RabbitMQConfiguration;
import com.example.emailservice.service.EmailService;
import com.rabbitmq.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;
import javax.mail.MessagingException;

@Service
public class MessageReceiver {

    private final ApplicationConfiguration configuration;
    private EmailService emailService;


    @Autowired
    public MessageReceiver(ApplicationConfiguration configuration, EmailService emailService){

        this.configuration = configuration;
        this.emailService = emailService;
        init();
    }

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

            Consumer consumer = new DefaultConsumer(channel) {


                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                                           byte[] body) throws IOException {
                    String log = new String(body, StandardCharsets.UTF_8); //jwt + " " + email
                    System.out.println(log);
                    if (log.contains(" ")) {
                        String[] arr = log.split(" ");
                        try {
                            emailService.sendHtmlMail(arr[1], "Verification mail", "<html><body>Confirm account activation on link.</br><a href=\"http://localhost:8082/validation/" + arr[0] + "\">Activation link</a><body/></html>");
                        } catch (Exception e) {

                        }
                    }
                    else
                        emailService.sendRecoveryMail("", log);

                }
            };

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
}
