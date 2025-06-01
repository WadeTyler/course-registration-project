package net.tylerwade.registrationsystem.auth.dto;

import net.tylerwade.registrationsystem.auth.authority.Authority;

import java.util.Date;
import java.util.Set;

public record UserDTO(Long id, String username, String firstName, String lastName, Set<Authority> userAuthorities, Date createdAt) {
}
