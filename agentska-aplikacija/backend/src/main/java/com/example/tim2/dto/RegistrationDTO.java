package com.example.tim2.dto;

public class RegistrationDTO {
    private String name;
    private String surname;
    private String address;
    private String city;
    private String email;
    private String username;
    private String password;
    private boolean canReserve;
    private boolean canComment;
    private int numberRefusedComments;
    private int numberCanceledRequest;

    public RegistrationDTO() {

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
