package com.example.tim2.dto;

import com.example.tim2.model.Request;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class RequestDTO {
    private Long id;
    private String state;
    private Date startDate;
    private Date endDate;
    private String userName;
    private String agentName;
    private Set<MiniCarDTO> cars;
    private String userUsername;
    private String agentUsername;

    public RequestDTO(Long id, String state, Date startDate, Date endDate, String userName, String agentName, Set<MiniCarDTO> cars,String userUsername,String agentUsername) {
        this.id = id;
        this.state = state;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userName = userName;
        this.agentName = agentName;
        this.cars = cars;
        this.userUsername = userUsername;
        this.agentUsername = agentUsername;
    }

    public RequestDTO(Request r) {
        this.id = r.getId();
        this.state = r.getState();
        this.startDate = r.getStartDate();
        this.endDate = r.getEndDate();
        this.userName = r.getUser().getUsername();
        this.agentName = r.getSender().getCompanyName();
        this.cars = new HashSet<>();
        this.agentUsername = r.getEntrepreneur().getUser().getUsername();
        this.userUsername = r.getUser().getUsername();
    }

    public RequestDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Set<MiniCarDTO> getCars() {
        return cars;
    }

    public void setCars(Set<MiniCarDTO> cars) {
        this.cars = cars;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getUserUsername() {
        return userUsername;
    }

    public void setUserUsername(String userUsername) {
        this.userUsername = userUsername;
    }

    public String getAgentUsername() {
        return agentUsername;
    }

    public void setAgentUsername(String agentUsername) {
        this.agentUsername = agentUsername;
    }
}
