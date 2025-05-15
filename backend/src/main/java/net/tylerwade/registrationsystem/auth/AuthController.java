package net.tylerwade.registrationsystem.auth;

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

        // Add authCookie
        Authentication authentication = userService.createAuthenticationForUser(user);
        Cookie authCookie = tokenService.generateAuthTokenCookie(authentication);
        response.addCookie(authCookie);

        return ResponseEntity.status(HttpStatus.CREATED).body(new APIResponse<>(true, "Signup successful.", user.toDTO()));
    }

    // Get Token
    @PostMapping("/login")
    public ResponseEntity<?> getToken(Authentication authentication, HttpServletResponse response) {
        Cookie authCookie = tokenService.generateAuthTokenCookie(authentication);
        response.addCookie(authCookie);

        UserDTO user = userService.getUser(authentication).toDTO();
        return ResponseEntity.status(HttpStatus.CREATED).body(new APIResponse<>(true, "Login successful.", user));
    }

    // Get User
    @GetMapping
    public ResponseEntity<?> getUser(Authentication authentication) {
        UserDTO user = this.userService.getUser(authentication).toDTO();
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse<>(true, "User Retrieved.", user));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        response.addCookie(tokenService.generateLogoutCookie());
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse<>(true, "Logout Successful.", null));

    }


}
