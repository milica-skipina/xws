package tim2.auth.service;

import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tim2.auth.dto.UserDTO;
import tim2.auth.model.User;
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

    public UserService(TokenUtils tokenUtils,MessageProducer messageProducer,UserRepository userRepository){
        this.tokenUtils = tokenUtils;
        this.messageProducer = messageProducer;
        this.userRepository = userRepository;
    }

    public List<UserDTO> getAll(){
        List<User> users = userRepository.findAll();
        List<UserDTO> dtos = new ArrayList<UserDTO>();

        for(User u : users){
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
        if (user != null)
        {
            if(!user.isActivated())
            {
                user.setActivated(true);
                userRepository.save(user);
                return true;
            }
            return false;
        }
        return false;
    }

    public boolean deleteUser(Long id) {
        User user = userRepository.findOneById(id);
        if (user != null)
        {
            user.setRoles(null);
            user.setAgent(null);
            user.setEnduser(null);
            user.setAdmin(null);
            userRepository.delete(user);
            return true;
        }
        return false;
    }

    public boolean deactivateAccount(Long id){
        User user = userRepository.findOneById(id);
        if (user != null)
        {
            user.setActivated(false);
            userRepository.save(user);
            return true;
        }
        return false;
    }
}
