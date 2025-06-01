package net.tylerwade.registrationsystem.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import net.tylerwade.registrationsystem.auth.authority.Authority;
import net.tylerwade.registrationsystem.auth.dto.UserDTO;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_authorities",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private Set<Authority> userAuthorities;

    @CreatedDate
    private Date createdAt;

    // --- UserDetail Functions ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> auth = userAuthorities.stream()
                .map(authority -> new SimpleGrantedAuthority("ROLE_" + authority.getName()))
                .collect(Collectors.toSet());
        System.out.println(auth);
        return auth;
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
                userAuthorities,
                createdAt);
    }

    @JsonIgnore
    public boolean isAdmin() {
        return userAuthorities
                .stream()
                .filter(authority -> authority.getName().equalsIgnoreCase("ADMIN"))
                .findFirst()
                .orElse(null) != null;
    }

    @JsonIgnore
    public boolean isInstructor() {
        return userAuthorities
                .stream()
                .filter(authority -> authority.getName().equalsIgnoreCase("INSTRUCTOR"))
                .findFirst()
                .orElse(null) != null;
    }

    @JsonIgnore
    public boolean isStudent() {
        return userAuthorities
                .stream()
                .filter(authority -> authority.getName().equalsIgnoreCase("STUDENT"))
                .findFirst()
                .orElse(null) != null;
    }
}
