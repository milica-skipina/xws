package com.example.tim2.dto;

import com.example.tim2.model.Request;
import com.example.tim2.model.RequestWrapper;

import java.util.HashSet;
import java.util.Set;


public class RequestWrapDTO {
    private Long id;
    private Set<RequestDTO> requests= new HashSet<RequestDTO>();;
    private String customerUsername;
    private String agentUsername;
    private String customerName;
    private String agentName;
    private String state;

    public RequestWrapDTO(RequestWrapper r) {
        this.id = r.getId();
        this.customerUsername = r.getCustomerUsername();
        this.agentUsername = r.getAgentUsername();
        this.state = ((Request)r.getRequests().toArray()[0]).getState();
        this.agentName = ((Request)r.getRequests().toArray()[0]).getSender().getCompanyName();
        this.customerName = ((Request)r.getRequests().toArray()[0]).getUser().getEnduser().getName() + " " +
                ((Request)r.getRequests().toArray()[0]).getUser().getEnduser().getSurname();
        //this.requests = r.getRequests(); ovo ces rucno
    }

    public RequestWrapDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<RequestDTO> getRequests() {
        return requests;
    }

    public void setRequests(Set<RequestDTO> requests) {
        this.requests = requests;
    }

    public String getCustomerUsername() {
        return customerUsername;
    }

    public void setCustomerUsername(String customerUsername) {
        this.customerUsername = customerUsername;
    }

    public String getAgentUsername() {
        return agentUsername;
    }

    public void setAgentUsername(String agentUsername) {
        this.agentUsername = agentUsername;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
