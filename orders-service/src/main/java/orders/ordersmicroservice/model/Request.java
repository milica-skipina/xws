package orders.ordersmicroservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.owasp.encoder.Encode;
import rs.ac.uns.ftn.xws_tim2.Order;

import java.util.*;

@Entity
@Getter
@Setter
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "REQUEST_AND_CAR", joinColumns = {
            @JoinColumn(name = "request_id") }, inverseJoinColumns = { @JoinColumn(name = "car_id") })
    private Set<Car> cars = new HashSet<Car>();

    @Column(name = "state", nullable = false)
    private String state;

    // predjeni kilometri
    @Column(name = "mileage", nullable = false)
    private double mileage;

    @Column(name = "dateCreated", nullable = false)
    private Date dateCreated;

    // ko salje
    @Column(name = "customer_username", nullable = false)
    private String customerUsername;            // ovo iz jwta

    // ko odobrava
    @Column(name = "agent_username", nullable = false)
    private String agentUsername;         // ovo iz dobavljenog oglasa

    // ko salje
    @Column(name = "customer_name", nullable = true)
    private String customerName;            // ovo iz jwta

    // ko odobrava
    @Column(name = "agent_name", nullable = true)
    private String agentNamee;

    @Column(name = "startDate", nullable = false)
    private Date startDate;

    @Column(name = "endDate", nullable = false)
    private Date endDate;

    @JsonBackReference(value = "bundle_mov")
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private RequestWrapper requestWrapper;

    public Request() {
    }

    public Request(String state, double mileage, Date startDate, Date endDate, String customerUsername,
                   String agentUsername, Date dateCreated) {
        this.state = state;
        this.mileage = mileage;
        this.startDate = startDate;
        this.endDate = endDate;
        this.customerUsername = customerUsername;
        this.agentUsername = agentUsername;
        this.dateCreated = dateCreated;
    }

    public Request(Order order) {
        this.state = order.getState();
        this.mileage = order.getMileage();
        this.startDate = order.getStartDate().toGregorianCalendar().getTime();
        this.endDate = order.getEndDate().toGregorianCalendar().getTime();
        this.dateCreated = new Date();
        this.agentUsername = "prodavac";
        this.customerUsername = order.getCustomerUsername();
        this.agentNamee = "RentACar";
        this.customerName = "unknown";      // poslati u orderu i ime customera!
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Car> getCars() {
        return cars;
    }

    public void setCars(Set<Car> cars) {
        this.cars = cars;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getMileage() {
        return mileage;
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

    public Request escapeParameters(Request r) {
        r.setState(Encode.forHtml(r.getState()));
        return r;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
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

    public String getAgentNamee() {
        return agentNamee;
    }

    public void setAgentNamee(String agentNamee) {
        this.agentNamee = agentNamee;
    }

    public Order getGenerated(){
        Order ret = new Order();
        ret.setAgentUsername(this.agentUsername);
        ret.setCustomerUsername(this.customerUsername);
        ret.setMicroId(this.id);
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
            ret.getCars().add(car.getAdId());
        }
        return ret;
    }
}

