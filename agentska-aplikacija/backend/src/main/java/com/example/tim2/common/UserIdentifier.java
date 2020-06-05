package com.example.tim2.common;

import com.example.tim2.model.User;
import com.example.tim2.security.TokenUtils;
import com.example.tim2.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserIdentifier {

    @Autowired
    private TokenUtils tokenUtils;

    @Autowired
    private AuthService userService;

    public UserIdentifier() {
    }

    public User verifyUser(HttpServletRequest request){
        String token = tokenUtils.getToken(request);
        String username = tokenUtils.getUsernameFromToken(token);
        return userService.findOneByUsername(username);
    }
}
