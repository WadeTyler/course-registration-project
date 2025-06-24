package net.tylerwade.registrationsystem.auth;

import lombok.RequiredArgsConstructor;
import net.tylerwade.registrationsystem.auth.authority.Authority;
import net.tylerwade.registrationsystem.auth.authority.AuthorityService;
import net.tylerwade.registrationsystem.auth.dto.SignupRequest;
import net.tylerwade.registrationsystem.auth.dto.UpdateUserRequest;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthorityService authorityService;

    @Override
    public User findById(Long userId) throws HttpRequestException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new HttpRequestException(HttpStatus.NOT_FOUND, "User not found."));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }

    @Override
    public User signup(SignupRequest signupRequest) throws HttpRequestException {
        // Check if user already exists
        if (this.userRepository.existsByUsernameIgnoreCase(signupRequest.username())) {
            throw new HttpRequestException(HttpStatus.CONFLICT, "Email already exists.");
        }

        // Create new user
        User user = User.builder()
                .username(signupRequest.username())
                .firstName(signupRequest.firstName())
                .lastName(signupRequest.lastName())
                .password(passwordEncoder.encode(signupRequest.password())) // Encode password
                .userAuthorities(Set.of(authorityService.getStudentAuthority()))
                .build();

        // Save and return
        userRepository.save(user);

        return user;
    }

    @Override
    public Authentication createAuthenticationForUser(User user) {
        return new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                null,
                user.getAuthorities()
        );
    }

    @Override
    public User updateUserAsAdmin(Long userId, UpdateUserRequest updateUserRequest, Authentication authentication) throws HttpRequestException {
        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new HttpRequestException(HttpStatus.NOT_FOUND, "User not found."));

        // If updating role
        if (updateUserRequest.role() != null) {
            Authority authority = authorityService.findByName(updateUserRequest.role());
            // Use a mutable set instead of an immutable one
            targetUser.setUserAuthorities(new HashSet<>(Collections.singletonList(authority)));
        }

        // Save and return
        userRepository.save(targetUser);
        return targetUser;
    }

    @Override
    public void createDefaultAdmin() throws HttpRequestException {
        // Check if default admin exists
        if (userRepository.existsByUsernameIgnoreCase("admin@email.com")) return;

        // Create default admin
        User admin = User.builder()
                .username("admin@email.com")
                .password(passwordEncoder.encode("123456")) // Encode
                .firstName("admin")
                .lastName("admin")
                .userAuthorities(Set.of(authorityService.getAdminAuthority()))
                .build();

        userRepository.save(admin);
    }

    @Override
    public void createDefaultInstructor() throws HttpRequestException {
        // Check if default instructor exists
        if (userRepository.existsByUsernameIgnoreCase("instructor@email.com")) return;

        // Create default admin
        User instructor = User.builder()
                .username("instructor@email.com")
                .password(passwordEncoder.encode("123456")) // Encode
                .firstName("instructor")
                .lastName("instructor")
                .userAuthorities(Set.of(authorityService.getInstructorAuthority()))
                .build();

        userRepository.save(instructor);
    }

    @Override
    public void createDefaultStudent() throws HttpRequestException {
        // Check if default student exists
        if (userRepository.existsByUsernameIgnoreCase("student@email.com")) return;

        // Create default admin
        User student = User.builder()
                .username("student@email.com")
                .password(passwordEncoder.encode("123456")) // Encode
                .firstName("student")
                .lastName("student")
                .userAuthorities(Set.of(authorityService.getStudentAuthority()))
                .build();

        userRepository.save(student);
    }

}
