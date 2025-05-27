package net.tylerwade.registrationsystem.auth.dto;

import java.util.Date;
import java.util.Set;

public record UserDTO(Long id, String username, String firstName, String lastName, Set<String> grantedAuthorities, Date createdAt) {
}
