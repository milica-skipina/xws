package tim2.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tim2.auth.model.Role;
import tim2.auth.repository.AuthorityRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    public List<Role> findById(Long id) {
        Role auth = this.authorityRepository.getOne(id);
        List<Role> auths = new ArrayList<>();
        auths.add(auth);
        return auths;
    }

    public Set<Role> findByname(String name) {
        Role auth = this.authorityRepository.findByName(name);
        Set<Role> auths = new HashSet<>();
        auths.add(auth);
        return auths;
    }
}
