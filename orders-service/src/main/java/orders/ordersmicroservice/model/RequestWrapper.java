package orders.ordersmicroservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bouncycastle.cert.ocsp.Req;
import rs.ac.uns.ftn.xws_tim2.Wrapper;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor
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

    public RequestWrapper(Request r){
        this.state = r.getState();
        this.agentUsername = r.getAgentUsername();
        this.customerUsername = r.getCustomerUsername();
        this.requests = new HashSet<>();
        this.requests.add(r);
    }

    public RequestWrapper(Set<Request> requests) {
        this.customerUsername = ((Request)requests.toArray()[0]).getCustomerUsername();
        this.agentUsername = ((Request)requests.toArray()[0]).getAgentUsername();
        this.state = ((Request)requests.toArray()[0]).getState();
        this.requests = requests;
    }

    public Wrapper getGenerated() {
        Wrapper w = new Wrapper();
        w.setMicroId(this.id);
        w.setState(this.state);
        w.setCustomerUsername(this.customerUsername);
        w.setAgentUsername(this.agentUsername);
        for (Request r : this.requests) {
            w.getRequests().add(r.getGenerated());
        }
        return w;
    }
}

