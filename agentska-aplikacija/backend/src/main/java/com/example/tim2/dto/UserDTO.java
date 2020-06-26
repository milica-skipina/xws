package com.example.tim2.dto;

import com.example.tim2.model.EndUser;
import com.example.tim2.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String username;
    private boolean enabled;
    private boolean deleted;
    private boolean blocked;
    private int numberFailedLogin;


    public UserDTO(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.enabled = user.isEnabled();
        this.deleted = user.isDeleted();
        this.numberFailedLogin = user.getNumberFailedLogin();
        this.blocked = user.isBlocked();
    }
}
