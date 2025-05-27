package net.tylerwade.registrationsystem.enrollment.dto;

import net.tylerwade.registrationsystem.auth.dto.UserDTO;
import net.tylerwade.registrationsystem.coursesection.dto.CourseSectionDTO;

import java.math.BigDecimal;
import java.util.Date;

public record EnrollmentDTO(
        Long id,
        UserDTO student,
        CourseSectionDTO courseSection,
        BigDecimal grade,
        String status,
        Date createdAt
) {
}
