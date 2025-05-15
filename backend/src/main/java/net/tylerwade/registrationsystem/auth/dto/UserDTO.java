package net.tylerwade.registrationsystem.auth.dto;

import java.sql.Timestamp;
import java.util.Set;

public record UserDTO(String id, String username, String firstName, String lastName, Set<String> grantedAuthorities, Timestamp createdAt) {
}
