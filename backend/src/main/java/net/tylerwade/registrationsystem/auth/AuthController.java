package net.tylerwade.registrationsystem.auth;

import com.nimbusds.jose.proc.SecurityContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import net.tylerwade.registrationsystem.auth.dto.SignupRequest;
import net.tylerwade.registrationsystem.auth.dto.UserDTO;
import net.tylerwade.registrationsystem.auth.token.TokenService;
import net.tylerwade.registrationsystem.common.APIResponse;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final TokenService tokenService;

    @Autowired
    public AuthController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    // Signup
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest, HttpServletResponse response) throws HttpRequestException {
        User user = this.userService.signup(signupRequest);
        UserDTO userDTO = this.userService.convertToDTO(user);

        // Add authCookie
        Authentication authentication = userService.createAuthenticationForUser(user);
        Cookie authCookie = tokenService.generateAuthTokenCookie(authentication);
        response.addCookie(authCookie);

        return ResponseEntity.status(HttpStatus.CREATED).body(new APIResponse<>(true, "Signup successful.", userDTO));
    }

    // Get Token
    @PostMapping("/login")
    public ResponseEntity<?> getToken(Authentication authentication, HttpServletResponse response) {
        Cookie authCookie = tokenService.generateAuthTokenCookie(authentication);

        response.addCookie(authCookie);
        return ResponseEntity.status(HttpStatus.CREATED).body(new APIResponse<>(true, "Token retrieved.", authCookie.getValue()));
    }

    // Get User
    @GetMapping
    public ResponseEntity<?> getUser(Authentication authentication) {
        UserDTO userDTO = this.userService.convertToDTO(
                this.userService.getUser(authentication)
        );
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse<>(true, "User Retrieved.", userDTO));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        response.addCookie(tokenService.generateLogoutCookie());
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse<>(true, "Logout Successful.", null));

    }


}
