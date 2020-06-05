package tim2.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegistrationDTO {
    private String name;
    private String surname;
    private String address;
    private String city;
    private String email;
    private String username;
    private String password;
    private boolean isCustomer;     // posto mogu da se reg i agenti
    private String companyName;
    private Long registryNumber;

    public RegistrationDTO() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isCustomer() {
        return isCustomer;
    }

    public void setCustomer(boolean customer) {
        isCustomer = customer;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getRegistryNumber() {
        return registryNumber;
    }

    public void setRegistryNumber(Long registryNumber) {
        this.registryNumber = registryNumber;
    }
}

