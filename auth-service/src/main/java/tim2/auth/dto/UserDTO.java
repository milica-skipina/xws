package tim2.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tim2.auth.model.User;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String email;
    private String username;
    private boolean activated;
    private EndUserDTO endUserDTO;
    private AgentDTO agentDTO;
    private int numberFailedLogin;
    private boolean deleted;
    private boolean blocked;

    public UserDTO(User user){
        this.id = user.getId();
        this.email = user.getEmail();
        this.username = user.getUsername();
        this.activated = user.getActivated();
        if(user.getEnduser()!=null) this.endUserDTO = new EndUserDTO(user.getEnduser());
        if(user.getAgent()!=null) this.agentDTO = new AgentDTO(user.getAgent());
        this.numberFailedLogin = user.getNumberFailedLogin();
        this.deleted = user.isDeleted();
        this.blocked = user.isBlocked();
    }


}
