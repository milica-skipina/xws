package com.example.tim2.model;

import com.example.tim2.soap.gen.Order;
import com.example.tim2.soap.gen.Wrapper;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
public class RequestWrapper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonManagedReference(value = "bundle_mov")
    @OneToMany(mappedBy = "requestWrapper", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Request> requests;      // sluzi ako se pravi bundle

    @Column(name = "customerUsername", nullable = false)
    private String customerUsername;

    @Column(name = "agentUsername", nullable = false)
    private String agentUsername;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "micro_id", nullable = true)
    private Long microId;

    public RequestWrapper(Set<Request> requests) {
        this.customerUsername = ((Request)requests.toArray()[0]).getUser().getUsername();
        this.agentUsername = ((Request)requests.toArray()[0]).getEntrepreneur().getUser().getUsername();
        this.state = ((Request)requests.toArray()[0]).getState();
        this.requests = requests;
    }

    public RequestWrapper() {
        this.requests = new HashSet<Request>();
    }

    public RequestWrapper(Wrapper w) {
        this.agentUsername = w.getAgentUsername();
        this.customerUsername = w.getCustomerUsername();
        this.state = ((Order)w.getRequests().toArray()[0]).getState();
        this.microId = w.getMicroId();
    }
}
