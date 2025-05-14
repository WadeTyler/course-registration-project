package net.tylerwade.registrationsystem.auth;

import net.tylerwade.registrationsystem.auth.dto.SignupRequest;
import net.tylerwade.registrationsystem.auth.dto.UserDTO;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service interface for managing user-related operations.
 */
@Service
public interface UserService extends UserDetailsService {

    /**
     * Finds a user by their ID.
     *
     * @param userId The ID of the user.
     * @return The user entity.
     * @throws HttpRequestException If the user is not found.
     */
    User findById(String userId) throws HttpRequestException;

    /**
     * Loads a user by their username.
     *
     * @param username The username of the user.
     * @return The user entity.
     * @throws UsernameNotFoundException If the user is not found.
     */
    User loadUserByUsername(String username) throws UsernameNotFoundException;

    /**
     * Signs up a new user.
     *
     * @param signupRequest The request object containing user signup details.
     * @return The created user entity.
     * @throws HttpRequestException If an error occurs during signup.
     */
    User signup(SignupRequest signupRequest) throws HttpRequestException;

    /**
     * Converts a User entity to a UserDTO.
     *
     * @param user The user entity.
     * @return The UserDTO representation.
     */
    default UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getFirstName(), user.getLastName(), user.getCreatedAt());
    }

    /**
     * Retrieves the currently authenticated user.
     *
     * @param authentication The authentication object of the current user.
     * @return The user entity.
     */
    default User getUser(Authentication authentication) {
        return this.loadUserByUsername(authentication.getName());
    }


    /**
     * Creates an Authentication object for the specified user.
     *
     * @param user the user to authenticate
     * @return an Authentication representing the user
     */
    Authentication createAuthenticationForUser(User user);
}