package com.example.emailservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    public static final String KEYSTORE_PROVIDER = "SunX509";
    public static final String QUEUE_NAME = "emailRequests";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, false);
    }

}