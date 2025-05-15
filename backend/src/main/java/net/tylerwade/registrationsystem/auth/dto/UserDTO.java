package net.tylerwade.registrationsystem.auth.dto;

import java.time.Instant;
import java.util.Set;

public record UserDTO(String id, String username, String firstName, String lastName, Set<String> grantedAuthorities, Instant createdAt, Instant modifiedAt) {
}
