package tim2.auth.service;

import org.springframework.stereotype.Service;
import tim2.auth.dto.ProfileDTO;
import tim2.auth.dto.UserDTO;
import tim2.auth.model.Admin;
import tim2.auth.model.Agent;
import tim2.auth.model.EndUser;
import tim2.auth.model.User;
import tim2.auth.repository.AdminRepository;
import tim2.auth.repository.AgentRepository;
import tim2.auth.repository.EndUserRepository;
import tim2.auth.repository.UserRepository;
import tim2.auth.security.TokenUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Service
public class UserService {

    private TokenUtils tokenUtils;

    private MessageProducer messageProducer;

    private UserRepository userRepository;

    private AgentRepository agentRepository;

    private AdminRepository adminRepository;

    private EndUserRepository endUserRepository;

    public UserService(TokenUtils tokenUtils, MessageProducer messageProducer, UserRepository userRepository,
                       AgentRepository agentRepository, AdminRepository adminRepository, EndUserRepository endUserRepository) {
        this.tokenUtils = tokenUtils;
        this.messageProducer = messageProducer;
        this.userRepository = userRepository;
        this.agentRepository = agentRepository;
        this.endUserRepository = endUserRepository;
        this.adminRepository = adminRepository;
        this.agentRepository = agentRepository;
    }

    public List<UserDTO> getAll() {
        List<User> users = userRepository.findAll();
        List<UserDTO> dtos = new ArrayList<UserDTO>();

        for (User u : users) {
            if (!u.getRoles().contains("ROLE_ADMIN"))
                dtos.add(new UserDTO(u));
        }

        return dtos;
    }


        public boolean sendVerificationMail(Long id) throws IOException, TimeoutException {
            System.out.println(id);
            User user = userRepository.findOneById(id);
            if(user != null) {
                if (!user.isActivated()) {
                    String jwt = tokenUtils.generateToken(user.getUsername(), user.getAuthorities(), "");
                    String message = jwt + " " + user.getEmail();
                    messageProducer.send(message);
                    return true;
                }
                return false;
            }
            return false;
        }

    public boolean verify(String token) {
        User user = userRepository.findByEmail(tokenUtils.getUsernameFromToken(token));
        if (user != null) {
            if (!user.isActivated()) {
                user.setActivated(true);
                userRepository.save(user);
                return true;
            }
            return false;
        }
        return false;
    }

    public User deleteUser(Long id) {
        User user = userRepository.findOneById(id);
        if (user != null) {
            user.setRoles(null);
            user.setAgent(null);
            user.setEnduser(null);
            user.setAdmin(null);
            userRepository.delete(user);
            return user;
        }
        return null;
    }

    public User deactivateAccount(Long id){
        User user = userRepository.findOneById(id);
        if (user != null) {
            user.setActivated(false);
            userRepository.save(user);
            return user;
        }
        return null;
    }

    public ProfileDTO getProfile(String username) {
        User user = userRepository.findOneByUsername(username);
        ProfileDTO profile = new ProfileDTO();
        profile.setUsername(user.getUsername());
        profile.setEmail(user.getEmail());
        Admin admin = adminRepository.findByUserId(user.getId());
        if (admin != null) {
            return profile;
        }

        Agent agent = agentRepository.findByUserId(user.getId());
        if (agent != null) {
            profile.setCompany(agent.getCompanyName());
            profile.setName(agent.getName());
            profile.setSurname(agent.getSurname());
            profile.setAddress(agent.getAddress());
            profile.setCity(agent.getCity());
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
        ArrayList<EndUser> customers = (ArrayList<EndUser>) endUserRepository.findAll();
        ArrayList<String> customerUsernames = new ArrayList<String>();
        for (EndUser u : customers) {
            if (u.getUser().getActivated()) {
                customerUsernames.add(u.getUser().getUsername());
            }
        }
        return customerUsernames;
    }
}
