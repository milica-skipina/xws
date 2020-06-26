package com.example.tim2.service;

import com.example.tim2.datavalidation.RegularExpressions;
import com.example.tim2.dto.EndUserDTO;
import com.example.tim2.dto.ProfileDTO;
import com.example.tim2.model.Admin;
import com.example.tim2.model.EndUser;
import com.example.tim2.model.Entrepreneur;
import com.example.tim2.model.User;
import com.example.tim2.repository.AdminRepository;
import com.example.tim2.repository.EndUserRepository;
import com.example.tim2.repository.EntrepreneurRepository;
import com.example.tim2.repository.UserRepository;
import com.example.tim2.security.TokenUtils;
import org.apache.commons.text.RandomStringGenerator;
import org.apache.syncope.core.spring.security.SecureRandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class UserService {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private EntrepreneurRepository agentRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private EndUserRepository endUserRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Environment env;

    @Autowired
    private EmailService emailService;

    public ProfileDTO getProfile(String username) {
        User user = userRepository.findOneByUsername(username);
        ProfileDTO profile = new ProfileDTO();
        profile.setUsername(user.getUsername());
        profile.setEmail(user.getEmail());
        Admin admin = adminRepository.findOneByUserId(user.getId());
        if (admin != null) {
            return profile;
        }

        Entrepreneur agent = agentRepository.findOneByUserId(user.getId());
        if (agent != null) {
            profile.setCompany(agent.getCompanyName());
            profile.setName(agent.getName());
            profile.setSurname(agent.getSurname());
            profile.setAddress(agent.getAddress());
            return profile;
        }

        EndUser endUser = endUserRepository.findByUserId(user.getId());
        if (endUser != null) {
            profile.setName(endUser.getName());
            profile.setSurname(endUser.getSurname());
            profile.setAddress(endUser.getAddress());
            profile.setCity(endUser.getCity());
            profile.setCanComment(endUser.isCanComment());
            profile.setCanReserve(endUser.isCanReserve());
            profile.setNumberCanceledRequest(endUser.getNumberCanceledRequest());
            profile.setNumberRefusedComments(endUser.getNumberRefusedComments());
            return profile;
        }
        return null;
    }

    public ArrayList<String> getAllCustomers() {
        ArrayList<EndUser> customers = (ArrayList<EndUser>) endUserRepository.findAllByActivated(true);
        ArrayList<String> customerUsernames = new ArrayList<String>();
        for (EndUser u : customers) {
            customerUsernames.add(u.getUser().getUsername());
        }
        return customerUsernames;
    }


    public boolean blockEndUser(String changedPermission, String username){
        EndUser endUser = endUserRepository.findByUserUsername(username);
        if(changedPermission.equals("alloweComment")){
            endUser.setCanComment(true);
            endUser.setNumberRefusedComments(0);
        }
        else if(changedPermission.equals("alloweReserve")){
            endUser.setCanReserve(true);
            endUser.setNumberCanceledRequest(0);
        }
        else if(changedPermission.equals("blockComment")){
            endUser.setCanComment(false);
            endUser.setNumberRefusedComments(0);
        }
        else if(changedPermission.equals("blockReserve")){
            endUser.setCanReserve(false);
            endUser.setNumberCanceledRequest(0);
        }
        endUserRepository.save(endUser);
        return true;
    }

    public List<EndUserDTO> getAllEndUsers(){
        List<EndUser> users = endUserRepository.findAll();
        List<EndUserDTO> dtos = new ArrayList<EndUserDTO>();

        for(EndUser eu : users){
            if(!eu.getUser().isDeleted()) {
                dtos.add(new EndUserDTO(eu));
            }
        }

        return dtos;
    }

    public boolean deleteUser(Long id){
        EndUser user = endUserRepository.findOneById(id);
        if(user != null) {
            user.getUser().setDeleted(true);
            endUserRepository.save(user);
            return true;
        }else
            return false;
    }

    public boolean activateUser(Long id) throws MessagingException {
        EndUser user = endUserRepository.findOneById(id);
        if(user != null) {
            String jwt = tokenUtils.generateToken(user.getUser().getUsername());
            try {
                emailService.sendHtmlMail("isaprojektovanje@gmail.com", "Verification mail", "<html><body>Confirm account activation on link.</br><a href=\"https://localhost:3000/validation/" + jwt + "\">Activation link</a><body/></html>");
            } catch (Exception e) {
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean blockUser(Long id){
        EndUser user = endUserRepository.findOneById(id);
        if(user != null) {
            user.getUser().setBlocked(true);
            endUserRepository.save(user);
            return true;
        }else
            return false;
    }

    public boolean unblockUser(Long id){
        EndUser user = endUserRepository.findOneById(id);
        if(user != null) {
            user.getUser().setBlocked(false);
            endUserRepository.save(user);
            return true;
        }else
            return false;
    }

    public boolean accountRecovery(String email) {
        RegularExpressions regularExpressions = new RegularExpressions();
        if (!regularExpressions.isValidEmail(email)) {
            return false;
        }
        User user = userRepository.findOneByEmail(email);
        if (user == null) {
            return false;
        }
        EndUser customer = endUserRepository.findByUserId(user.getId());
        customer.setFirstLogin(true);
        String password = generateRandomSpecialCharacters();
        user.setPassword(passwordEncoder.encode(password));
        user.setLastPasswordResetDate(new Timestamp(System.currentTimeMillis()));
        sendRecoveryMail(email, password);
        userRepository.save(user);
        endUserRepository.save(customer);
        return true;
    }

    public String generateRandomSpecialCharacters() {
        Random rand = new Random();
        int min = 33;
        int max = 45;
        int length = rand.nextInt(max - min) + min;
        String password = SecureRandomUtils.generateRandomPassword(length);
        password = password.concat("_");
        return password;
    }

    @Async
    public void sendRecoveryMail(String email, String newPassword) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo("pswisa.tim31.2019@gmail.com");
        msg.setFrom(env.getProperty("spring.mail.username"));
        msg.setSubject("Account recovery - rentacar");
        msg.setText("You received this email due to your request for account recovery in rent-a-car system. \n" +
                "This password is valid 24h after the mail has been sent and it is for one-time use. Please change" +
                "your password after first login.\n" +
                "Your one-time password: " + newPassword);

        javaMailSender.send(msg);
        System.out.println("Email sent.");
    }
}
