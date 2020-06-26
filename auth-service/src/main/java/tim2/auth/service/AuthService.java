package tim2.auth.service;

import org.apache.syncope.core.spring.security.SecureRandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tim2.auth.dto.PasswordChangeDTO;
import tim2.auth.dto.RegistrationDTO;
import tim2.auth.model.Agent;
import tim2.auth.model.EndUser;
import tim2.auth.model.Role;
import tim2.auth.model.User;
import tim2.auth.repository.AgentRepository;
import tim2.auth.repository.EndUserRepository;
import tim2.auth.repository.UserRepository;
import tim2.auth.security.TokenUtils;
import tim2.auth.validation.RegexExpressions;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class AuthService implements UserDetailsService {


    private AuthorityService authorityService;

    private PasswordEncoder passwordEncoder;

    private UserRepository userRepository;

    private AuthenticationManager authenticationManager;

    private EndUserRepository endUserRepository;

    private AgentRepository agentRepository;

    private TokenUtils tokenUtils;

    private MessageProducer messageProducer;

    @Autowired
    public AuthService(AuthorityService authorityService, PasswordEncoder passwordEncoder, UserRepository userRepository,
                       EndUserRepository endUserRepository, AgentRepository agentRepository, MessageProducer messageProducer,
                       AuthenticationManager authenticationManager,TokenUtils tokenUtils) {
        this.authorityService = authorityService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.endUserRepository = endUserRepository;
        this.agentRepository = agentRepository;
        this.authenticationManager = authenticationManager;
        this.messageProducer = messageProducer;
    }

    public User loginUser(String username, String password) {
        if (!RegexExpressions.isValidSomeName(username)) {
            return null;
        }
        User user = (User) loadUserByUsername(username);
        EndUser customer = endUserRepository.findByUserId(user.getId());
        if (customer != null && customer.isFirstLogin()) {
            long diff = Math.abs(user.getLastPasswordResetDate().getTime() - new Timestamp(System.currentTimeMillis()).getTime());
            long hrs = TimeUnit.MILLISECONDS.toHours(diff);
            if (hrs > 24) {     // lozinka mu vise nije validna
                return null;
            }
            customer.setFirstLogin(false);
            user.setLastPasswordResetDate(new Timestamp(System.currentTimeMillis()));
            userRepository.save(user);
            endUserRepository.save(customer);
        }
        return user;
    }

    public User registerUser(RegistrationDTO reg) {
        if (!validateInput(reg)) {
            return null;
        }
        User user = (User) loadUserByUsername(reg.getUsername());
        if (user != null) // there is already user with that username
            return null;
        else {
            user = new User();
            user.setPassword(passwordEncoder.encode(reg.getPassword()));
            user.setEnabled(true);
            user.setEmail(reg.getEmail());
            user.setUsername(reg.getUsername());
            user.setActivated(false);   //tek ga treba odobriti
            Set<Role> auth = authorityService.findByname("ROLE_CUSTOMER");
            if (reg.isCustomer()) {
                EndUser customer = new EndUser(reg.getName(), reg.getSurname(), reg.getAddress(), reg.getCity());
                user.setRoles(auth);
                customer.setUser(user.escapeParameters(user));
                customer.setCanComment(true);
                customer.setCanReserve(true);
                customer.setNumberCanceledRequest(0);
                customer.setNumberRefusedComments(0);
                endUserRepository.save(EndUser.escapeParameters(customer));
            } else {
                Agent agent = new Agent(reg.getCompanyName(), reg.getAddress(), reg.getCity(), reg.getRegistryNumber());
                auth = authorityService.findByname("ROLE_SELLER");
                user.setRoles(auth);
                agent.setUser(user.escapeParameters(user));
                agentRepository.save(agent.escapeParameters(agent));
            }

            return user;
        }
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

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userRepository.findOneByUsername(s);
        return user;
    }

    private boolean validateInput(RegistrationDTO dto) {
        return RegexExpressions.isValidInput(dto.getCity()) && RegexExpressions.isValidInput(dto.getName())
                && RegexExpressions.isValidInput(dto.getSurname()) && RegexExpressions.isValidPassword(dto.getPassword())
                && RegexExpressions.isValidEmail(dto.getEmail()) && RegexExpressions.isValidSomeName(dto.getUsername())
                && RegexExpressions.isValidSomeName(dto.getAddress());
    }

    /**
     * data[0] - shouldRegister data[1] - customer name data[2] - email  data[3] - surname data[4] - username
     * @param data
     */
    public boolean manualRegistration(String[] data) {
        EndUser customer = new EndUser(data[1], data[3]);
        User user = new User();
        user.setPassword(passwordEncoder.encode("w7$Q.R[xB8"));
        user.setEnabled(true);
        user.setEmail(data[2]);
        user.setUsername(data[4]);
        Set<Role> auth = authorityService.findByname("ROLE_CUSTOMER");
        user.setRoles(auth);
        user.setActivated(true);
        customer.setFirstLogin(true);
        customer.setCanComment(true);
        customer.setCanReserve(true);
        customer.setNumberCanceledRequest(0);
        customer.setNumberRefusedComments(0);
        customer.setUser(user.escapeParameters(user));

        endUserRepository.save(customer.escapeParameters(customer));
        return true;
    }

    public boolean accountRecovery(String email) {
        if (!RegexExpressions.isValidEmail(email)) {
            return false;
        }
        User user = userRepository.findOneByEmail(email);
        if (user == null) {
            return false;
        }
        EndUser customer = endUserRepository.findByUserId(user.getId());
        customer.setFirstLogin(true);
        String password = generateRandomSpecialCharacters() ;     
        user.setPassword(passwordEncoder.encode(password));
        user.setLastPasswordResetDate(new Timestamp(System.currentTimeMillis()));
        // TREBA GA DODATI U RED ZA RABITA, TREBA POSLATI MEJL
        try {
            messageProducer.send(password);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        userRepository.save(user);
       // endUserRepository.save(customer);
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
}
