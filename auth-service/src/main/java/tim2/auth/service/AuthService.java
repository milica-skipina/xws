package tim2.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tim2.auth.dto.RegistrationDTO;
import tim2.auth.model.Agent;
import tim2.auth.model.EndUser;
import tim2.auth.model.Role;
import tim2.auth.model.User;
import tim2.auth.repository.AgentRepository;
import tim2.auth.repository.EndUserRepository;
import tim2.auth.repository.UserRepository;
import tim2.auth.validation.RegexExpressions;

import java.util.Set;

@Service
public class AuthService implements UserDetailsService {


    private AuthorityService authorityService;


    private PasswordEncoder passwordEncoder;


    private UserRepository userRepository;


    private EndUserRepository endUserRepository;

    private AgentRepository agentRepository;

    @Autowired
    public AuthService(AuthorityService authorityService, PasswordEncoder passwordEncoder, UserRepository userRepository,
                       EndUserRepository endUserRepository, AgentRepository agentRepository) {
        this.authorityService = authorityService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.endUserRepository = endUserRepository;
        this.agentRepository = agentRepository;
    }

    public User loginUser(String username, String password) {
        if (!RegexExpressions.isValidCharNumSpace(username)) {
            return null;
        }
        User user = (User) loadUserByUsername(username);

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


    public boolean forgotPassword(Long id) {
        // to be implemented
        return true;
    }

    public boolean changePassword(Long id) {
        // to be implemented
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
}
