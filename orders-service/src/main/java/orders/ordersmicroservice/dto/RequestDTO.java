package orders.ordersmicroservice.dto;

import lombok.Getter;
import lombok.Setter;
import orders.ordersmicroservice.model.Request;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class RequestDTO {
    private Long id;
    private String state;
    private Date startDate;
    private Date endDate;
    private String userId;      // customer rname
    private String agentId;     //agent name
    private Set<MiniCarDTO> cars;
    private String agentUsername;

    public RequestDTO(Long id, String state, Date startDate, Date endDate, String userId, String agentId, Set<MiniCarDTO> cars) {
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
        this.userId = r.getCustomerName();
        this.agentId = r.getAgentNamee();
        this.agentUsername = r.getAgentUsername();
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

    public Set<MiniCarDTO> getCars() {
        return cars;
    }

    public void setCars(Set<MiniCarDTO> cars) {
        this.cars = cars;
    }
}

