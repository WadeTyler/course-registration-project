package net.tylerwade.registrationsystem.auth;

import net.tylerwade.registrationsystem.auth.authority.Authority;
import net.tylerwade.registrationsystem.auth.authority.AuthorityService;
import net.tylerwade.registrationsystem.auth.dto.SignupRequest;
import net.tylerwade.registrationsystem.auth.dto.UpdateUserRequest;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthorityService authorityService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private Authority studentAuthority;

    @BeforeEach
    void setUp() {
        // Setup common test data
        studentAuthority = Authority.builder()
                .id(1)
                .name("STUDENT")
                .build();

        testUser = User.builder()
                .id(1L)
                .username("test@example.com")
                .firstName("Test")
                .lastName("User")
                .password("encodedPassword")
                .userAuthorities(Set.of(studentAuthority))
                .build();
    }

    @Test
    void findById_ExistingUser_ReturnsUser() throws HttpRequestException {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        // Act
        User foundUser = userService.findById(1L);

        // Assert
        assertNotNull(foundUser);
        assertEquals(testUser.getId(), foundUser.getId());
        assertEquals(testUser.getUsername(), foundUser.getUsername());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void findById_NonExistentUser_ThrowsHttpRequestException() {
        // Arrange
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        HttpRequestException exception = assertThrows(HttpRequestException.class, () -> userService.findById(999L));
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals("User not found.", exception.getMessage());
        verify(userRepository, times(1)).findById(999L);
    }

    @Test
    void loadUserByUsername_ExistingUser_ReturnsUser() {
        // Arrange
        when(userRepository.findByUsername("test@example.com")).thenReturn(Optional.of(testUser));

        // Act
        User foundUser = userService.loadUserByUsername("test@example.com");

        // Assert
        assertNotNull(foundUser);
        assertEquals(testUser.getUsername(), foundUser.getUsername());
        verify(userRepository, times(1)).findByUsername("test@example.com");
    }

    @Test
    void loadUserByUsername_NonExistentUser_ThrowsUsernameNotFoundException() {
        // Arrange
        when(userRepository.findByUsername("nonexistent@example.com")).thenReturn(Optional.empty());

        // Act & Assert
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("nonexistent@example.com"));
        assertEquals("User not found.", exception.getMessage());
        verify(userRepository, times(1)).findByUsername("nonexistent@example.com");
    }

    @Test
    void signup_NewUser_ReturnsCreatedUser() throws HttpRequestException {
        // Arrange
        SignupRequest signupRequest = new SignupRequest("new@example.com", "New", "User", "password123", "password123");

        when(userRepository.existsByUsernameIgnoreCase(signupRequest.username())).thenReturn(false);
        when(passwordEncoder.encode(signupRequest.password())).thenReturn("encodedPassword");
        when(authorityService.getStudentAuthority()).thenReturn(studentAuthority);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User createdUser = userService.signup(signupRequest);

        // Assert
        assertNotNull(createdUser);
        assertEquals(signupRequest.username(), createdUser.getUsername());
        assertEquals(signupRequest.firstName(), createdUser.getFirstName());
        assertEquals(signupRequest.lastName(), createdUser.getLastName());
        assertEquals("encodedPassword", createdUser.getPassword());
        assertTrue(createdUser.getUserAuthorities().contains(studentAuthority));

        verify(userRepository, times(1)).existsByUsernameIgnoreCase(signupRequest.username());
        verify(passwordEncoder, times(1)).encode(signupRequest.password());
        verify(authorityService, times(1)).getStudentAuthority();
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void signup_ExistingUsername_ThrowsHttpRequestException() {
        // Arrange
        SignupRequest signupRequest = new SignupRequest("existing@example.com", "Existing", "User", "password123", "password123");
        when(userRepository.existsByUsernameIgnoreCase(signupRequest.username())).thenReturn(true);

        // Act & Assert
        HttpRequestException exception = assertThrows(HttpRequestException.class, () -> userService.signup(signupRequest));
        assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
        assertEquals("Email already exists.", exception.getMessage());

        verify(userRepository, times(1)).existsByUsernameIgnoreCase(signupRequest.username());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void updateUserAsAdmin_ExistingUser_ReturnsUpdatedUser() throws HttpRequestException {
        // Arrange
        Long userId = 1L;
        UpdateUserRequest updateRequest = new UpdateUserRequest("ADMIN");

        Authority adminAuthority = Authority.builder()
                .id(3)
                .name("ADMIN")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(authorityService.findByName("ADMIN")).thenReturn(adminAuthority);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User updatedUser = userService.updateUserAsAdmin(userId, updateRequest, authentication);

        // Assert
        assertNotNull(updatedUser);
        assertEquals(1, updatedUser.getUserAuthorities().size());
        assertTrue(updatedUser.getUserAuthorities().contains(adminAuthority));
        assertEquals(1, updatedUser.getUserAuthorities().size());

        verify(userRepository, times(1)).findById(userId);
        verify(authorityService, times(1)).findByName("ADMIN");
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    void updateUserAsAdmin_NonExistentUser_ThrowsHttpRequestException() {
        // Arrange
        Long userId = 999L;
        UpdateUserRequest updateRequest = new UpdateUserRequest("ADMIN");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        HttpRequestException exception = assertThrows(HttpRequestException.class, () -> userService.updateUserAsAdmin(userId, updateRequest, authentication));
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        assertEquals("User not found.", exception.getMessage());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }
}








