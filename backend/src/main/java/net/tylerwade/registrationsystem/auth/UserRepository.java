package net.tylerwade.registrationsystem.auth;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Page<User> findAllByUsernameContainingIgnoreCaseOrFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String username, String firstName, String lastName, Pageable pageable);
    Page<User> findAll(Pageable pageable);
    Optional<User> findByUsername(String username);
    boolean existsByUsernameIgnoreCase(String username);
}
