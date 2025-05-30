package net.tylerwade.registrationsystem.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import net.tylerwade.registrationsystem.auth.dto.SignupRequest;
import net.tylerwade.registrationsystem.auth.dto.UpdateUserRequest;
import net.tylerwade.registrationsystem.auth.dto.UserDTO;
import net.tylerwade.registrationsystem.auth.token.TokenService;
import net.tylerwade.registrationsystem.common.APIResponse;
import net.tylerwade.registrationsystem.common.PageResponse;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth Controller", description = "Operations related to users")
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

    /*
     * Signup
     */
    @Operation(summary = "Signup and create an account.", description = "Creates a user account nad authenticates the user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Signup successful."),
            @ApiResponse(responseCode = "400", description = "Invalid fields."),
            @ApiResponse(responseCode = "409", description = "Account already exists with email.")
    })
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequest signupRequest, HttpServletResponse response) throws HttpRequestException {
        User user = this.userService.signup(signupRequest);

        // Add authCookie
        Authentication authentication = userService.createAuthenticationForUser(user);
        Cookie authCookie = tokenService.generateAuthTokenCookie(authentication);
        response.addCookie(authCookie);

        return ResponseEntity.status(HttpStatus.CREATED).body(new APIResponse<>(true, "Signup successful.", user.toDTO()));
    }

    /*
     * Get Token
     */
    @Operation(summary = "Login and get authentication token", description = "Authenticates the user and returns a token cookie.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Login successful")
    })
    @SecurityRequirement(name = "basicAuth")
    @PostMapping("/login")
    public ResponseEntity<?> getToken(Authentication authentication, HttpServletResponse response) {
        Cookie authCookie = tokenService.generateAuthTokenCookie(authentication);
        response.addCookie(authCookie);

        UserDTO user = userService.getUser(authentication).toDTO();
        return ResponseEntity.status(HttpStatus.CREATED).body(new APIResponse<>(true, "Login successful.", user));
    }

    /*
     * Get User
     */
    @Operation(summary = "Get current user info", description = "Returns the authenticated user's information.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<?> getUser(Authentication authentication) {
        UserDTO user = this.userService.getUser(authentication).toDTO();
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse<>(true, "User Retrieved.", user));
    }

    /*
     * Logout
     */
    @Operation(summary = "Logout user", description = "Logs out the current user and clears the authentication cookie.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Logout successful")
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        response.addCookie(tokenService.generateLogoutCookie());
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse<>(true, "Logout Successful.", null));

    }

    /*
     * Find all users
     */
    @Operation(summary = "Get all users (admin only)", description = "Returns a paginated list of all users. Admin access required.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers(@ParameterObject Pageable pageable,
                                         @Parameter(description = "Search query for users") @RequestParam(required = false) String search) {
        Page<UserDTO> page = userService.findAll(pageable, search).map(User::toDTO);
        PageResponse<UserDTO> pageResponse = new PageResponse<>(page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages());
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse<>(true, "Users retrieved.", pageResponse));
    }

    /*
     * Update specific user
     */
    @Operation(summary = "Update a user (admin only)", description = "Updates the specified user's information. Admin access required.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully")
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/users/{userId}")
    public ResponseEntity<?> updateUser(Authentication authentication, @Parameter(description = "ID of the user to update") @PathVariable Long userId, @RequestBody UpdateUserRequest updateUserRequest) throws HttpRequestException {
        UserDTO updatedUser = userService.updateUserAsAdmin(userId, updateUserRequest, authentication).toDTO();
        return ResponseEntity.status(HttpStatus.OK).body(new APIResponse<>(true, "User updated.", updatedUser));
    }
}

