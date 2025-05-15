package net.tylerwade.registrationsystem.auth.dto;

import net.tylerwade.registrationsystem.config.security.authorities.UserRole;

public record UpdateUserRequest(
        UserRole role
) {
}
