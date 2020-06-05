package com.example.tim2.dto;

import com.example.tim2.model.Request;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class RequestDTO {
    private Long id;
    private String state;
    private Date startDate;
    private Date endDate;
    private Long userId;
    private Long agentId;
    private Set<MiniCarDTO> cars;

    public RequestDTO(Long id, String state, Date startDate, Date endDate, Long userId, Long agentId, Set<MiniCarDTO> cars) {
        this.id = id;
        this.state = state;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
        this.agentId = agentId;
        this.cars = cars;
    }

    public RequestDTO(Request r) {
        this.id = r.getId();
        this.state = r.getState();
        this.startDate = r.getStartDate();
        this.endDate = r.getEndDate();
        this.userId = r.getUser().getId();
        this.agentId = r.getSender().getId();
        this.cars = new HashSet<>();
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public Set<MiniCarDTO> getCars() {
        return cars;
    }

    public void setCars(Set<MiniCarDTO> cars) {
        this.cars = cars;
    }
}
