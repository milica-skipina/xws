package com.example.tim2.service;

import com.example.tim2.datavalidation.RegularExpressions;
import com.example.tim2.dto.RegistrationDTO;
import com.example.tim2.model.EndUser;
import com.example.tim2.model.Role;
import com.example.tim2.model.User;
import com.example.tim2.repository.EndUserRepository;
import com.example.tim2.repository.UserRepository;
import com.example.tim2.security.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

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

    @Autowired
    private TokenUtils tokenUtils;

    public EndUser registerUser(RegistrationDTO reg) {
        if (!validateInput(reg)) {
            return null;
        }
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
            Set<Role> auth = authorityService.findByname("ROLE_CUSTOMER");
            user.setRoles(auth);
            customer.setActivated(false);
            customer.setFirstLogin(false);
            customer.setUser(user.escapeParameters(user));
            customerRepo.save(customer.escapeParameters(customer));
            return customer;
        }
    }

    public User loginUser(String username, String password) {
        RegularExpressions regx = new RegularExpressions();
        if (!regx.isValidSomeName(username)) {
            return null;
        }
        User user = (User) loadUserByUsername(username);

        /** KAD SE DODA MEJL PROVERA DA LI JE VALIDIRAN NALOG
         *

        if (!user.getActivated()) {
            return null;            // ne moze da se uloguje dok ne aktivira nalog
        }*/

        return user;
    }

    /*public boolean acceptRequest(String token) {
        String username = tokenUtils.getUsernameFromToken(token);
        User user = (User) loadUserByUsername(username);
        if (user != null) {
            user.setActivated(true);
            return true;
        }
        return false;
    }*/


    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepo.findOneByUsername(username);
        return user;
    }

    private boolean validateInput(RegistrationDTO dto) {
        RegularExpressions regx = new RegularExpressions();
        return regx.isValidInput(dto.getCity()) && regx.isValidInput(dto.getName()) && regx.isValidInput(dto.getSurname())
                && regx.isValidPassword(dto.getPassword()) && regx.isValidEmail(dto.getEmail())
                && regx.isValidSomeName(dto.getUsername()) && regx.isValidSomeName(dto.getAddress())
                && (dto.getUsername().length() >=6);
    }

    public User findOneByUsername(String username) {
       return userRepo.findOneByUsername(username);
    }

    /**
     * data[0] - shouldRegister data[1] - customer name data[2] - email  data[3] - surname data[4] - username
     * @param data
     */
    public User manualRegistration(String[] data) {
        EndUser customer = new EndUser(data[1], data[3]);
        User user = new User();
        user.setPassword(passwordEncoder.encode("w7$Q.R[xB8"));
        user.setEnabled(true);
        user.setEmail(data[2]);
        user.setUsername(data[4]);
        Set<Role> auth = authorityService.findByname("ROLE_CUSTOMER");
        user.setRoles(auth);
        customer.setActivated(true);
        customer.setFirstLogin(true);
        customer.setUser(user.escapeParameters(user));
        customerRepo.save(customer.escapeParameters(customer));
        return user;
    }

}
