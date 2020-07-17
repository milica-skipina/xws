package com.example.tim2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.owasp.encoder.Encode;

import javax.persistence.*;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "REQUEST_AND_CAR", joinColumns = {
            @JoinColumn(name = "request_id") }, inverseJoinColumns = { @JoinColumn(name = "car_id") })
    private Set<Car> cars = new HashSet<Car>();

    @Column(name = "state", nullable = false)
    private String state;           // PENDING, RESERVED , PAID , CANCELED

    // predjeni kilometri
    @Column(name = "mileage", nullable = false)
    private double mileage;

    @Column(name = "startDate", nullable = false)
    private Date startDate;

    @Column(name = "endDate", nullable = false)
    private Date endDate;

    @Column(name = "dateCreated", nullable = false)
    private Date dateCreated;

    @JsonBackReference(value = "request_mov")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private User user;       // end user koji je napravio zahtev, tj customer

    @JsonBackReference(value = "entrepreneurrequest_mov")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Entrepreneur entrepreneur;

    @JsonBackReference(value = "bundle_mov")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private RequestWrapper requestWrapper;

    @Column(name = "micro_id",  nullable = true)
    private Long microId;       // id iz orders microservisa

    public Request() {
    }

    public Request(Set<Car> cars, String state, double mileage, Date startDate, Date endDate, User user, Entrepreneur entrepreneur, Date dateCreated) {
        this.cars = cars;
        this.state = state;
        this.mileage = mileage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
        this.entrepreneur = entrepreneur;
        this.dateCreated = dateCreated;
    }



    public com.example.tim2.soap.gen.Order getGenerated(){
        com.example.tim2.soap.gen.Order ret = new com.example.tim2.soap.gen.Order();
        ret.setAgentUsername(this.entrepreneur.getUser().getUsername());
        ret.setCustomerUsername(this.user.getUsername());
        if (this.microId != null) {
            ret.setMicroId(this.microId);       // bice null ako je novi zahtev na agentu i nema ga na micro
        } else {
            ret.setMicroId(-100L);
        }

        GregorianCalendar c = new GregorianCalendar();
        c.setTime(getEndDate());
        try {
            ret.setEndDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
            c.setTime(getStartDate());
            ret.setStartDate(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
            c.setTime(this.dateCreated);
            ret.setDateCreated(DatatypeFactory.newInstance().newXMLGregorianCalendar(c));
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        ret.setId(this.id);
        ret.setMileage(this.mileage);
        ret.setState(this.state);
        for (Car car : this.cars) {
            ret.getCars().add(car.getMicroId());
        }
        return ret;
    }

    public Long getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public double getMileage() {
        return mileage;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setMileage(double mileage) {
        this.mileage = mileage;
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

    public Set<Car> getCars() {
        return cars;
    }

    public void setCars(Set<Car> cars) {
        this.cars = cars;
    }

    public Request(String state, Double mileage, Date startDate, Date endDate, User user, Date dateCreated) {
        this.state = state;
        this.mileage = mileage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
        this.dateCreated = dateCreated;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Entrepreneur getSender() {
        return entrepreneur;
    }

    public void setSender(Entrepreneur sender) {
        this.entrepreneur = sender;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Request escapeParameters(Request r) {
        r.setState(Encode.forHtml(r.getState()));
        return r;
    }

    public RequestWrapper getRequestWrapper() {
        return requestWrapper;
    }

    public void setRequestWrapper(RequestWrapper requestWrapper) {
        this.requestWrapper = requestWrapper;
    }

    public Entrepreneur getEntrepreneur() {
        return entrepreneur;
    }

    public void setEntrepreneur(Entrepreneur entrepreneur) {
        this.entrepreneur = entrepreneur;
    }

    public Long getMicroId() {
        return microId;
    }

    public void setMicroId(Long microId) {
        this.microId = microId;
    }
}
