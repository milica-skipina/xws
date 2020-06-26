package tim2.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tim2.auth.model.Agent;
import tim2.auth.model.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgentDTO {

    private Long id;
    private Long userId;
    private String companyName;
    private String name;
    private String surname;
    private String address;
    private String city;
    private Long registryNumber;        // poslovni maticni broj

    public AgentDTO(Agent a){
        this.id = a.getId();
        this.userId = a.getUser().getId();
        this.companyName = a.getCompanyName();
        this.name = a.getName();
        this.surname = a.getSurname();
        this.address = a.getAddress();
        this.city = a.getCity();
        this.registryNumber = a.getRegistryNumber();
    }




}
