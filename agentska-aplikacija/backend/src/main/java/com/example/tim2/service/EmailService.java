package com.example.tim2.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;

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

    @Async
    public void sendHtmlMail(String to,String subject,String htmlText) throws MessagingException {
        MimeMessage mess = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mess, "utf-8");
        helper.setText(htmlText,true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom("isaprojektovanje@gmail.com");
        javaMailSender.send(mess);
    }

}
