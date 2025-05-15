package net.tylerwade.registrationsystem.auth.dto;

import org.springframework.security.core.GrantedAuthority;

import java.sql.Timestamp;
import java.util.Set;

public record UserDTO(String id, String username, String firstName, String lastName, Set<? extends GrantedAuthority> grantedAuthorities, Timestamp createdAt) {
}
