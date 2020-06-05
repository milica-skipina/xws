package tim2.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tim2.auth.model.User;
import tim2.auth.security.TokenUtils;

@Service
public class AdminService {

    private TokenUtils tokenUtils;

    private AuthService authService;

    @Autowired
    public AdminService(TokenUtils tokenUtils, AuthService authService) {
        this.tokenUtils = tokenUtils;
        this.authService = authService;
    }

    public boolean acceptRequest(String token) {
        String username = tokenUtils.getUsernameFromToken(token);
        User user = (User) authService.loadUserByUsername(username);
        if (user != null) {
            user.setActivated(true);
            return true;
        }
        return false;
    }

    public boolean blockUser(Long id) {
        // to be implemented
        return true;
    }


}
