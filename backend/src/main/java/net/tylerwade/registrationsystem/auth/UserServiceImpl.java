package net.tylerwade.registrationsystem.auth;

import net.tylerwade.registrationsystem.auth.dto.SignupRequest;
import net.tylerwade.registrationsystem.exception.HttpRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }

    @Override
    public User signup(SignupRequest signupRequest) throws HttpRequestException {
        // Check passwords match
        if (!signupRequest.password().equals(signupRequest.confirmPassword())) {
            throw new HttpRequestException(HttpStatus.BAD_REQUEST, "Passwords must match.");
        }


        // Check if user already exists
        if (this.userRepository.existsByUsernameIgnoreCase(signupRequest.username())) {
            throw new HttpRequestException(HttpStatus.NOT_ACCEPTABLE, "Email already exists.");
        }

        // Create new user
        User user = new User();
        user.setUsername(signupRequest.username());
        user.setFirstName(signupRequest.firstName());
        user.setLastName(signupRequest.lastName());
        user.setPassword(passwordEncoder.encode(signupRequest.password())); // Encode Password

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

}
