package com.example.tim2.model;

public class UserTokenState {

    private String accessToken;
    private long expiresIn;
    private String role;        // poslati na front ulogu ulogovanog usera

    public UserTokenState() {
        super();
        // TODO Auto-generated constructor stub
    }

    public UserTokenState(String accessToken, long expiresIn, String role) {
        super();
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.role = role;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}


