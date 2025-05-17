package net.tylerwade.registrationsystem.enrollment.dto;

import net.tylerwade.registrationsystem.auth.dto.UserDTO;

import java.time.Instant;

public record InstructorEnrollmentDTO(
        Long id,
        UserDTO student,
        Long courseSectionId,
        Integer grade,
        String status,
        Instant createdAt,
        Instant modifiedAt
) {

}
