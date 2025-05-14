package net.tylerwade.registrationsystem.auth.token;

import jakarta.servlet.http.Cookie;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * Service interface for managing token-related operations.
 */
@Service
public interface TokenService {

    /**
     * Generates a token for the authenticated user.
     *
     * @param authentication The authentication object of the current user.
     * @return A string representing the generated token.
     */
    String generateToken(Authentication authentication);

    /**
     * Generates an authentication token cookie for the authenticated user.
     *
     * @param authentication The authentication object of the current user.
     * @return A Cookie object containing the authentication token.
     */
    Cookie generateAuthTokenCookie(Authentication authentication);

    /**
     * Generates a logout cookie to invalidate the user's session.
     *
     * @return A Cookie object configured for logout.
     */
    Cookie generateLogoutCookie();
}