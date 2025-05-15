package net.tylerwade.registrationsystem.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.tylerwade.registrationsystem.auth.dto.UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    private Set<? extends GrantedAuthority> grantedAuthorities;

    @Column(nullable = false)
    private Timestamp createdAt = new Timestamp(System.currentTimeMillis());

    public User(String username, String firstName, String lastName, String password, Set<? extends GrantedAuthority> grantedAuthorities) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.grantedAuthorities = grantedAuthorities;
    }

    // --- UserDetail Functions ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

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

    // --- UTIL FUNCTIONS ---
    @JsonIgnore
    public UserDTO toDTO() {
        return new UserDTO(id,
                username,
                firstName,
                lastName,
                grantedAuthorities,
                createdAt);
    }
}
