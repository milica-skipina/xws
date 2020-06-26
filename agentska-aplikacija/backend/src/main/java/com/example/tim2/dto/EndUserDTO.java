package com.example.tim2.dto;

import com.example.tim2.model.EndUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EndUserDTO {

    private Long id;
    private Long userId;
    private String name;
    private String surname;
    private String address;
    private String city;
    private boolean firstLogin;
    private boolean canReserve;
    private boolean canComment;
    private int numberRefusedComments;
    private int numberCanceledRequest;
    private UserDTO user;
    private boolean activated;

    public EndUserDTO(EndUser u){
        this.id = u.getId();
        this.userId = u.getUser().getId();
        this.name = u.getName();
        this.surname = u.getSurname();
        this.address = u.getAddress();
        this.city = u.getCity();
        this.firstLogin = u.isFirstLogin();
        this.canReserve = u.isCanReserve();
        this.canComment = u.isCanComment();
        this.numberRefusedComments = u.getNumberRefusedComments();
        this.numberCanceledRequest = u.getNumberCanceledRequest();
        this.user = new UserDTO(u.getUser());
        this.activated = u.isActivated();
    }
}
