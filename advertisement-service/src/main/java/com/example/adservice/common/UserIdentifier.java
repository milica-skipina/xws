package com.example.adservice.common;

import com.example.adservice.config.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserIdentifier {

    @Autowired
    private TokenUtils tokenUtils;

    public UserIdentifier() {
    }

    public String verifyUser(HttpServletRequest request){
        String token = tokenUtils.getToken(request);
        return tokenUtils.getUsernameFromToken(token);
    }

    public String verifyUserRole(HttpServletRequest request){
        String token = tokenUtils.getToken(request);
        return tokenUtils.getRoleFromToken(token);
    }
}
