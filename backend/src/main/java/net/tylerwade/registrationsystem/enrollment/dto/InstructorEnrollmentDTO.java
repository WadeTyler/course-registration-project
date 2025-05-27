package net.tylerwade.registrationsystem.enrollment.dto;

import net.tylerwade.registrationsystem.auth.dto.UserDTO;

import java.math.BigDecimal;
import java.util.Date;

public record InstructorEnrollmentDTO(
        Long id,
        UserDTO student,
        Long courseSectionId,
        BigDecimal grade,
        String status,
        Date createdAt
) {

}
