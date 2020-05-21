package com.example.emailservice.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Environment env;

    @Async
    public void sendAccountConfirmationEmail(String email, String text) throws MailException, InterruptedException {
        System.out.println("Sending email...");
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("pswisa.tim31.2019@gmail.com");
        msg.setFrom(env.getProperty("spring.mail.username"));
        msg.setSubject("Subject");
        msg.setText("Some text");
        javaMailSender.send(msg);
        System.out.println("Email sent.");
    }


}
