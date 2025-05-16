package net.tylerwade.registrationsystem.auth;

import net.tylerwade.registrationsystem.auth.dto.SignupRequest;
import net.tylerwade.registrationsystem.auth.dto.UpdateUserRequest;
import net.tylerwade.registrationsystem.config.security.authorities.UserRole;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static net.tylerwade.registrationsystem.config.security.authorities.UserRole.STUDENT;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findById(String userId) throws HttpRequestException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new HttpRequestException(HttpStatus.NOT_FOUND, "User not found."));
    }

    @Override
    public Page<User> findAll(Pageable pageable, String search) {
        if (search != null) {
            return userRepository.findAllByUsernameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(search, search, search, pageable);
        } else {
            return userRepository.findAll(pageable);
        }
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
        User user = new User();
        user.setUsername(signupRequest.username());
        user.setFirstName(signupRequest.firstName());
        user.setLastName(signupRequest.lastName());
        user.setPassword(passwordEncoder.encode(signupRequest.password())); // Encode Password
        user.setGrantedAuthorities(STUDENT.getGrantedAuthoritiesString());    // Set student role

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
    public User updateUserAsAdmin(String userId, UpdateUserRequest updateUserRequest, Authentication authentication) throws HttpRequestException {
        User targetUser = userRepository.findById(userId)
                .orElseThrow(() -> new HttpRequestException(HttpStatus.NOT_FOUND, "User not found."));

        // If updating role
        if (updateUserRequest.role() != null) {
            UserRole newRole = updateUserRequest.role();
            targetUser.setGrantedAuthorities(newRole.getGrantedAuthoritiesString());
        }

        // Save and return
        userRepository.save(targetUser);
        return targetUser;
    }

    @Override
    public void createDefaultAdmin() {
        // Check if default admin exists
        if (userRepository.existsByUsernameIgnoreCase("admin@email.com")) return;

        // Create default admin
        User admin = new User("admin@email.com", "admin", "admin", passwordEncoder.encode("123456"), UserRole.ADMIN.getGrantedAuthoritiesString());

        userRepository.save(admin);
    }

    @Override
    public void createDefaultInstructor() {
        // Check if default instructor exists
        if (userRepository.existsByUsernameIgnoreCase("instructor@email.com")) return;

        // Create default admin
        User instructor = new User("instructor@email.com", "instructor", "instructor", passwordEncoder.encode("123456"), UserRole.INSTRUCTOR.getGrantedAuthoritiesString());

        userRepository.save(instructor);
    }

    @Override
    public void createDefaultStudent() {
        // Check if default student exists
        if (userRepository.existsByUsernameIgnoreCase("student@email.com")) return;

        // Create default admin
        User student = new User("student@email.com", "student", "student", passwordEncoder.encode("123456"), STUDENT.getGrantedAuthoritiesString());

        userRepository.save(student);
    }

}
