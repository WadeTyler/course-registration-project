package net.tylerwade.registrationsystem.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.tylerwade.registrationsystem.auth.dto.SignupRequest;
import net.tylerwade.registrationsystem.config.security.SecurityConfig;
import net.tylerwade.registrationsystem.config.security.authorities.UserRole;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import({SecurityConfig.class, AuthTestsUtil.class})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class AuthControllerTests {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private UserService service;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    AuthTestsUtil authTestsUtil;

    List<User> mockUsers;

    @BeforeEach
    void loadMockUsers() {
        this.mockUsers = authTestsUtil.createMockUsers();
    }

    @Test
    void signup_shouldReturnBadRequest_whenBadFields() throws Exception {

        var signupRequest = new SignupRequest("java@email.com",
                "jd",
                "k",
                "123",
                "321");

        mvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signup_shouldReturnCreated_whenValidRequest() throws Exception {
        // Arrange
        var signupRequest = new SignupRequest(
                "test@example.com",
                "John",
                "Doe",
                "password123",
                "password123"
        );

        User mockUser = new User();
        mockUser.setId(UUID.randomUUID().toString());
        mockUser.setUsername("test@example.com");
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        mockUser.setGrantedAuthorities(UserRole.STUDENT.getGrantedAuthoritiesString());

        Authentication mockAuth = new UsernamePasswordAuthenticationToken(mockUser.getUsername(), null, mockUser.getAuthorities());

        when(service.signup(any(SignupRequest.class))).thenReturn(mockUser);
        when(service.createAuthenticationForUser(any(User.class))).thenReturn(mockAuth);

        // Act & Assert
        mvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.message").value("Signup successful."));
    }

    @Test
    void signup_shouldReturnBadRequest_whenPasswordsDoNotMatch() throws Exception {
        // Arrange
        var signupRequest = new SignupRequest(
                "test@example.com",
                "John",
                "Doe",
                "password123",
                "differentPassword"
        );

        // Act & Assert
        mvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void signup_shouldReturnConflict_whenEmailAlreadyExists() throws Exception {
        // Arrange
        var signupRequest = new SignupRequest(
                "existing@example.com",
                "John",
                "Doe",
                "password123",
                "password123"
        );

        when(service.signup(any(SignupRequest.class)))
                .thenThrow(new HttpRequestException(HttpStatus.CONFLICT, "Email already registered"));

        // Act & Assert
        mvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isConflict());
    }

    @Test
    void login_shouldReturnUserAndCreatedStatus_whenAuthenticated() throws Exception {
        // Arrange
        User mockUser = new User();
        mockUser.setId(UUID.randomUUID().toString());
        mockUser.setUsername("user@example.com");
        mockUser.setPassword("123456");
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        mockUser.setGrantedAuthorities(UserRole.STUDENT.getGrantedAuthoritiesString());

        when(service.getUser(any(Authentication.class))).thenReturn(mockUser);

        // Act & Assert
        mvc.perform(post("/api/auth/login")
                .with(user(mockUser.getUsername()).roles("STUDENT").password(mockUser.getPassword())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.message").value("Login successful."))
                .andExpect(jsonPath("$.data.username").value(mockUser.getUsername()))
                .andExpect(jsonPath("$.data.firstName").value(mockUser.getFirstName()))
                .andExpect(jsonPath("$.data.lastName").value(mockUser.getLastName()));
    }

    @Test
    void getUser_shouldReturnUser_whenAuthenticated() throws Exception {
        // Arrange
        User mockUser = new User();
        mockUser.setId(UUID.randomUUID().toString());
        mockUser.setUsername("user@example.com");
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        mockUser.setGrantedAuthorities(UserRole.STUDENT.getGrantedAuthoritiesString());

        when(service.getUser(any(Authentication.class))).thenReturn(mockUser);

        // Act & Assert
        mvc.perform(get("/api/auth")
                        .with(user(mockUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.message").value("User Retrieved."))
                .andExpect(jsonPath("$.data.username").value(mockUser.getUsername()));
    }

    @Test
    void logout_shouldReturnOk_andClearCookie() throws Exception {

        // Arrange
        User mockUser = new User();
        mockUser.setId(UUID.randomUUID().toString());
        mockUser.setUsername("user@example.com");
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        mockUser.setGrantedAuthorities(UserRole.STUDENT.getGrantedAuthoritiesString());

        // Act & Assert
        mvc.perform(post("/api/auth/logout").with(user(mockUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.message").value("Logout Successful."))
                .andExpect(cookie().exists("AuthToken"))
                .andExpect(cookie().maxAge("AuthToken", 0));
    }
}
