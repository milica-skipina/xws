package com.example.tim2.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.joda.time.DateTime;
import org.owasp.encoder.Encode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "korisnik")
@NamedEntityGraph(name = "User.Roles.Privileges",
        attributeNodes = @NamedAttributeNode(value = "authorities", subgraph = "privileges"),
        subgraphs = @NamedSubgraph(name = "privileges", attributeNodes = @NamedAttributeNode("privileges")))
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "activated", nullable = true)
    private boolean activated;

    @Column(name = "enabled", nullable = true)
    private boolean enabled; // authorization for accessing methods

    @Column(name = "last_password_reset_date", nullable = true)
    private Timestamp lastPasswordResetDate;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_authority", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "authority_id", referencedColumnName = "id"))
    private Set<Role> authorities;    // role korisnika

    @JsonBackReference(value = "agent_movement")
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Entrepreneur agent;

    @JsonBackReference(value = "enduser_movement")
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private EndUser enduser;

    @JsonBackReference(value = "admin_movement")
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Admin admin;

    @JsonManagedReference(value = "request_mov")
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Request> requests;

    public void setRoles(Set<Role> authorities) {
        this.authorities = authorities;
    }

    public User() {

    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Timestamp getLastPasswordResetDate() {
        return lastPasswordResetDate;
    }


    public void setLastPasswordResetDate(Timestamp lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        Timestamp now = new Timestamp(DateTime.now().getMillis());
        this.setLastPasswordResetDate(now);
        // this.password = passwordEncoder.encode(password);
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> permissions = new ArrayList<GrantedAuthority>(20);
        for (Role role : this.authorities) {
            permissions.addAll(role.getPrivileges());
        }
        permissions.addAll(this.authorities);
        return permissions;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return enabled;
    }

    @Override
    public String getUsername() {
        // TODO Auto-generated method stub
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAgent(Entrepreneur agent) {
        this.agent = agent;
    }

    public void setEnduser(EndUser enduser) {
        this.enduser = enduser;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public User escapeParameters(User u) {
        u.setUsername(Encode.forHtml(u.getUsername()));
        u.setEmail(Encode.forHtml(u.getEmail()));
        return u;
    }

    public Collection<? extends GrantedAuthority> getRoles() {
        return this.authorities;
    }


}
