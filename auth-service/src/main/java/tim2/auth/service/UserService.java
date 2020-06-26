package tim2.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import tim2.auth.dto.ProfileDTO;
import tim2.auth.dto.UserDTO;
import tim2.auth.model.Admin;
import tim2.auth.model.Agent;
import tim2.auth.model.EndUser;
import tim2.auth.model.Role;
import tim2.auth.model.User;
import tim2.auth.repository.AdminRepository;
import tim2.auth.repository.AgentRepository;
import tim2.auth.repository.EndUserRepository;
import tim2.auth.repository.UserRepository;
import tim2.auth.security.TokenUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
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

    @Autowired
    public UserService(TokenUtils tokenUtils, MessageProducer messageProducer, UserRepository userRepository,
                       AgentRepository agentRepository, AdminRepository adminRepository, EndUserRepository endUserRepository) {
        this.tokenUtils = tokenUtils;
        this.messageProducer = messageProducer;
        this.userRepository = userRepository;
        this.agentRepository = agentRepository;
        this.endUserRepository = endUserRepository;
        this.adminRepository = adminRepository;
    }

    public List<UserDTO> getAll() {
        List<User> users = new ArrayList<>();
        try {
            users = userRepository.findAll();
        }catch (Exception e){
            e.printStackTrace();
        }
        List<UserDTO> dtos = new ArrayList<UserDTO>();

        for (User u : users) {
            if (!u.getRoleNames().contains("ROLE_ADMIN") && !u.isDeleted())
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
        if(token != null || !token.isEmpty()) {
            String username = tokenUtils.getUsernameFromToken(token);
            if(username != null || !username.isEmpty()) {
                User user = userRepository.findByUsername(username);
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
            return false;
        }
        return false;
    }

    public User deleteUser(Long id) {
        User user = userRepository.findOneById(id);
        if (user != null) {
            user.setDeleted(true);
            userRepository.save(user);
            return user;
        }
        return null;
    }

    public User deactivateAccount(Long id){
        User user = userRepository.findOneById(id);
        if (user != null) {
            user.setBlocked(true);
            userRepository.save(user);
            return user;
        }
        return null;
    }

    public User activateAccount(Long id){
        User user = userRepository.findOneById(id);
        if (user != null) {
            user.setBlocked(false);
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
            profile.setCanComment(endUser.isCanComment());
            profile.setCanReserve(endUser.isCanReserve());
            profile.setNumberCanceledRequest(endUser.getNumberCanceledRequest());
            profile.setNumberRefusedComments(endUser.getNumberRefusedComments());
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

    public boolean changeS(String username, String change){
        EndUser endUser = endUserRepository.findByUserUsername(username);
        if(change.equals("REJECTED")){
            endUser.setNumberRefusedComments(endUser.getNumberRefusedComments()+1);
            endUserRepository.save(endUser);
        }
        return true;
    }

    public boolean canR(String username){
        EndUser endUser = endUserRepository.findByUserUsername(username);
        return endUser.isCanReserve();
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

}
