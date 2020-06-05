package tim2.auth.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tim2.auth.model.Admin;
import tim2.auth.model.Agent;
import tim2.auth.model.EndUser;
import tim2.auth.repository.AdminRepository;
import tim2.auth.repository.AgentRepository;
import tim2.auth.repository.EndUserRepository;
import tim2.auth.repository.UserRepository;

@Service
public class UserIdentifier {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private EndUserRepository endUserRepository;


    public String  findByUserId(Long id) {
        Admin a = adminRepository.findByUserId(id);
        EndUser eu = endUserRepository.findByUserId(id);
        Agent ag = agentRepository.findByUserId(id);

        if (a != null) {
            return a.getUser().getUsername();
        }

        if (eu != null) {
            return eu.getName() + " " + eu.getSurname();
        }

        if (ag != null) {
            return ag.getCompanyName();
        }

        return "";    // nije se dobro autentifikovao
    }
}
