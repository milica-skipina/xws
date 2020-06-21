package tim2.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDTO {

    private String username;
    private String name;
    private String surname;
    private String email;
    private String address;
    private String city;
    private String company;

}
