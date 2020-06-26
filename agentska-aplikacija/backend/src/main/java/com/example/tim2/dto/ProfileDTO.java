package com.example.tim2.dto;

public class ProfileDTO {

    private String username;
    private String name;
    private String surname;
    private String email;
    private String address;
    private String city;
    private String company;
    private boolean canReserve;
    private boolean canComment;
    private int numberRefusedComments;
    private int numberCanceledRequest;

    public ProfileDTO() {
    }

    public ProfileDTO(String username, String name, String surname, String email, String address, String city, String company, boolean a, boolean b, int c, int d) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.address = address;
        this.city = city;
        this.company = company;
        this.canReserve = a;
        this.canComment = b;
        this.numberRefusedComments = c;
        this.numberCanceledRequest = d;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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
