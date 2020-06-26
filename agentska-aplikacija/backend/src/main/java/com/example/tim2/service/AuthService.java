package com.example.tim2.service;

import com.example.tim2.datavalidation.RegularExpressions;
import com.example.tim2.dto.PasswordChangeDTO;
import com.example.tim2.dto.RegistrationDTO;
import com.example.tim2.model.EndUser;
import com.example.tim2.model.Role;
import com.example.tim2.model.User;
import com.example.tim2.repository.EndUserRepository;
import com.example.tim2.repository.UserRepository;
import com.example.tim2.security.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private EndUserRepository endUserRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    public boolean confirmEmail(String token){
        String username  = tokenUtils.getUsernameFromToken(token);
        EndUser user = endUserRepository.findByUserUsername(username);
        if(user != null) {
            user.setActivated(true);
            endUserRepository.save(user);
            return true;
        }else
            return false;
    }

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
            customer.setCanComment(true);
            customer.setCanReserve(true);
            customer.setNumberCanceledRequest(0);
            customer.setNumberRefusedComments(0);
            customerRepo.save(customer.escapeParameters(customer));
            return customer;
        }
    }

    public User loginUser(String username) {
        RegularExpressions regx = new RegularExpressions();
        if (!regx.isValidSomeName(username)) {
            return null;
        }
        User user = (User) loadUserByUsername(username);
        EndUser customer = customerRepo.findByUserId(user.getId());
        if (customer != null && customer.isFirstLogin()) {
            long diff = Math.abs(user.getLastPasswordResetDate().getTime() - new Timestamp(System.currentTimeMillis()).getTime());
            long hrs = TimeUnit.MILLISECONDS.toHours(diff);
            if (hrs > 24) {     // lozinka mu vise nije validna
                return null;
            }
            customer.setFirstLogin(false);
            user.setLastPasswordResetDate(new Timestamp(System.currentTimeMillis()));
            userRepo.save(user.escapeParameters(user));
            customerRepo.save(customer.escapeParameters(customer));
        }

        if (customer != null && !customer.isActivated()) {
            return null;            // ne moze da se uloguje dok ne aktivira nalog
        }

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
                && (dto.getUsername().length() >= 6);
    }

    public User findOneByUsername(String username) {
       return userRepo.findOneByUsername(username);
    }

    /**
     * data[0] - shouldRegister data[1] - customer name data[2] - email  data[3] - surname data[4] - username
     * @param data
     */
    public User manualRegistration(String[] data) {
        RegularExpressions regx = new RegularExpressions();
        if (!regx.isValidInput(data[1]) || !regx.isValidEmail(data[2]) || !regx.isValidInput(data[3])
                || !regx.isValidInput(data[4])) {
            return null;
        }
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
        customer.setNumberCanceledRequest(0);
        customer.setNumberRefusedComments(0);
        customer.setCanReserve(true);
        customer.setCanComment(true);
        userRepo.save(user.escapeParameters(user));
        customerRepo.save(customer.escapeParameters(customer));
        return user;
    }

    public boolean changePassword(PasswordChangeDTO passwordChangeDTO) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        String name = currentUser.getName();

        if (authenticationManager == null || passwordChangeDTO.getNewPassword().equals("") || passwordChangeDTO.getOldPassword().equals("") || passwordChangeDTO.getOldPassword() == null || passwordChangeDTO.getNewPassword() == null) {
            return false;
        }

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(name, passwordChangeDTO.getOldPassword()));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        User user = (User) loadUserByUsername(name);
        user.setPassword(passwordEncoder.encode(passwordChangeDTO.getNewPassword()));
        userRepository.save(user);

        return true;
    }

}
