package com.example.tim2.service;

import com.example.tim2.dto.RegistrationDTO;
import com.example.tim2.model.Authority;
import com.example.tim2.model.EndUser;
import com.example.tim2.model.User;
import com.example.tim2.repository.EndUserRepository;
import com.example.tim2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private EndUserRepository customerRepo;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public EndUser registerUser(RegistrationDTO reg) {
        User user = (User) loadUserByUsername(reg.getUsername());
        if (user != null) // there is already user with that username
            return null;
        else {
            EndUser customer = new EndUser(reg.getName(), reg.getSurname(), reg.getAddress(), reg.getCity());
            user = new User();
            user.setPassword(passwordEncoder.encode(reg.getPassword()));
            user.setEnabled(true);
            user.setEmail(reg.getEmail());
            user.setUsername(reg.getUsername());
            List<Authority> auth = authorityService.findByname("CUSTOMER");
            user.setAuthorities(auth);
            customer.setUser(user);
            customerRepo.save(customer);
            return customer;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepo.findOneByUsername(username);
        return user;
    }

}
