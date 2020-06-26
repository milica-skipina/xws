package tim2.auth.dto;

import tim2.auth.model.EndUser;

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

    public EndUserDTO() {
    }

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
    }

    public EndUserDTO(Long id, Long id1, String name, String surname, String address, String city, boolean firstLogin, boolean canReserve, boolean canComment, int numberRefusedComments, int numberCanceledRequest) {
        this.id = id;
        this.userId = id1;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.city = city;
        this.firstLogin = firstLogin;
        this.canReserve = canReserve;
        this.canComment = canComment;
        this.numberRefusedComments = numberRefusedComments;
        this.numberCanceledRequest = numberCanceledRequest;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    public boolean isCanReserve() {
        return canReserve;
    }

    public void setCanReserve(boolean canReserve) {
        this.canReserve = canReserve;
    }

    public boolean isCanComment() {
        return canComment;
    }

    public void setCanComment(boolean canComment) {
        this.canComment = canComment;
    }

    public int getNumberRefusedComments() {
        return numberRefusedComments;
    }

    public void setNumberRefusedComments(int numberRefusedComments) {
        this.numberRefusedComments = numberRefusedComments;
    }

    public int getNumberCanceledRequest() {
        return numberCanceledRequest;
    }

    public void setNumberCanceledRequest(int numberCanceledRequest) {
        this.numberCanceledRequest = numberCanceledRequest;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

}
