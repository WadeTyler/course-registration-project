package net.tylerwade.registrationsystem.enrollment.dto;

import net.tylerwade.registrationsystem.auth.dto.UserDTO;
import net.tylerwade.registrationsystem.coursesection.dto.CourseSectionDTO;

import java.time.Instant;

public record EnrollmentDTO(
        Long id,
        UserDTO student,
        CourseSectionDTO courseSection,
        Integer grade,
        String status,
        Instant createdAt,
        Instant modifiedAt
) {
}
