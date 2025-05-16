package net.tylerwade.registrationsystem.auth;

import net.tylerwade.registrationsystem.auth.dto.SignupRequest;
import net.tylerwade.registrationsystem.auth.dto.UpdateUserRequest;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     * Retrieves a paginated list of all users in the system.
     *
     * @param pageable the pagination information, including page number, size, and sort order
     * @return a page of User entities
     */
    Page<User> findAll(Pageable pageable, String search);

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

    /**
     * Updates an existing user with the provided information.
     *
     * @param userId the ID of the user to update
     * @param updateUserRequest the request object containing updated user details
     * @param authentication the authentication context of the administrator performing the update
     * @return the updated User object
     */
    User updateUserAsAdmin(String userId, UpdateUserRequest updateUserRequest, Authentication authentication) throws HttpRequestException;

    /**
     * Creates a default admin user in the system if one does not already exist.
     * This method is typically used for initial setup or to ensure that an admin account is available.
     */
    void createDefaultAdmin();


    /**
     * Creates a default instructor user in the system if one does not already exist.
     * This method is typically used for initial setup or to ensure that an instructor account is available.
     */
    void createDefaultInstructor();

    /**
     * Creates a default student user in the system if one does not already exist.
     * This method is typically used for initial setup or to ensure that an student account is available.
     */
    void createDefaultStudent();
}