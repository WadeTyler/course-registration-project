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

/**
 * Represents a user entity in the registration system.
 * Implements Spring Security's UserDetails interface for authentication and authorization.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails {

    /**
     * Unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Username of the user. Must be unique and not null.
     */
    @Column(nullable = false)
    private String username;

    /**
     * First name of the user. Cannot be null.
     */
    @Column(nullable = false)
    private String firstName;

    /**
     * Last name of the user. Cannot be null.
     */
    @Column(nullable = false)
    private String lastName;

    /**
     * Password for the user account. Stored securely and ignored in JSON serialization.
     */
    @Column(nullable = false)
    @JsonIgnore
    private String password;

    /**
     * Authorities assigned to the user, representing roles and permissions.
     * Fetched eagerly and mapped via a join table.
     */
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_authorities",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private Set<Authority> userAuthorities;

    /**
     * Timestamp indicating when the user was created.
     * Automatically populated during creation.
     */
    @CreatedDate
    private Date createdAt;

    // --- UserDetail Functions ---

    /**
     * Retrieves the authorities granted to the user.
     * Converts Authority entities to SimpleGrantedAuthority objects prefixed with "ROLE_".
     *
     * @return A collection of granted authorities.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> auth = userAuthorities.stream()
                .map(authority -> new SimpleGrantedAuthority("ROLE_" + authority.getName()))
                .collect(Collectors.toSet());
        System.out.println(auth);
        return auth;
    }

    /**
     * Retrieves the user's password.
     *
     * @return The password of the user.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Retrieves the user's username.
     *
     * @return The username of the user.
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Indicates whether the user's account is expired.
     *
     * @return Always true (account is not expired).
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user's account is locked.
     *
     * @return Always true (account is not locked).
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Indicates whether the user's credentials are expired.
     *
     * @return Always true (credentials are not expired).
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates whether the user's account is enabled.
     *
     * @return Always true (account is enabled).
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    // --- UTIL FUNCTIONS ---

    /**
     * Converts the User entity to a UserDTO object.
     * Excludes sensitive information like the password.
     *
     * @return A UserDTO representation of the user.
     */
    @JsonIgnore
    public UserDTO toDTO() {
        return new UserDTO(id,
                username,
                firstName,
                lastName,
                userAuthorities,
                createdAt);
    }

    /**
     * Checks if the user has the "ADMIN" authority.
     *
     * @return True if the user is an admin, false otherwise.
     */
    @JsonIgnore
    public boolean isAdmin() {
        return userAuthorities
                .stream()
                .filter(authority -> authority.getName().equalsIgnoreCase("ADMIN"))
                .findFirst()
                .orElse(null) != null;
    }

    /**
     * Checks if the user has the "INSTRUCTOR" authority.
     *
     * @return True if the user is an instructor, false otherwise.
     */
    @JsonIgnore
    public boolean isInstructor() {
        return userAuthorities
                .stream()
                .filter(authority -> authority.getName().equalsIgnoreCase("INSTRUCTOR"))
                .findFirst()
                .orElse(null) != null;
    }

    /**
     * Checks if the user has the "STUDENT" authority.
     *
     * @return True if the user is a student, false otherwise.
     */
    @JsonIgnore
    public boolean isStudent() {
        return userAuthorities
                .stream()
                .filter(authority -> authority.getName().equalsIgnoreCase("STUDENT"))
                .findFirst()
                .orElse(null) != null;
    }
}