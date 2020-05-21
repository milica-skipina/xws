package tim2.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tim2.auth.model.User;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private AuthorityService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User loginUser(User u) {
        User user = (User) loadUserByUsername(u.getUsername());

        return null;
    }

    public User registerUser(User u) {
        User user = (User) loadUserByUsername(u.getUsername());

        return null;
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
        return null;
    }
}
