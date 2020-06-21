package com.example.tim2.service;

import com.example.tim2.dto.ProfileDTO;
import com.example.tim2.model.Admin;
import com.example.tim2.model.EndUser;
import com.example.tim2.model.Entrepreneur;
import com.example.tim2.model.User;
import com.example.tim2.repository.AdminRepository;
import com.example.tim2.repository.EndUserRepository;
import com.example.tim2.repository.EntrepreneurRepository;
import com.example.tim2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {

    @Autowired
    private EntrepreneurRepository agentRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private EndUserRepository endUserRepository;

    @Autowired
    private UserRepository userRepository;

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
}
