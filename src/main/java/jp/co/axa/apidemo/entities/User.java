package jp.co.axa.apidemo.entities;


import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

// User Entity for Handling the Authentication and Authorization
// Generate the jwt token information

@Getter
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Setter
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Setter
    @Column(nullable = false, length = 50, unique = true)
    private String email;

    @Setter
    @Column(nullable = false, length = 64)
    private String password;

    public User() {}

    // This constructor is used for handling the user login and registration
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Override the UserDetails spring core, Make the Role null to make login simple
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    //  Add the Email as username during authentication and authorization
    @Override
    public String getUsername() {
        return this.email;
    }

    // Make Account non expired for token access
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}
