package net.tylerwade.registrationsystem.auth;

import net.tylerwade.registrationsystem.config.security.authorities.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@TestComponent
public class AuthTestsUtil {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> createMockUsers() {
        User user1 = new User("johndoe@email.com", "John", "Doe", passwordEncoder.encode("123456"), UserRole.STUDENT.getGrantedAuthoritiesString());
        user1.setId(UUID.randomUUID().toString());
        user1.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        User user2 = new User("janedoe@email.com", "Jane", "Doe", passwordEncoder.encode("123456"), UserRole.INSTRUCTOR.getGrantedAuthoritiesString());
        user2.setId(UUID.randomUUID().toString());
        user2.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        User user3 = new User("jacobsmith@email.com", "Jacob", "Smith", passwordEncoder.encode("123456"), UserRole.ADMIN.getGrantedAuthoritiesString());
        user3.setId(UUID.randomUUID().toString());
        user3.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        return List.of(user1, user2, user3);
    }
}
