package net.tylerwade.registrationsystem.coursesection.dto;

import net.tylerwade.registrationsystem.auth.dto.UserDTO;
import net.tylerwade.registrationsystem.term.dto.TermDTO;

public record CourseSectionDTO(
        Long id,
        Long courseId,
        TermDTO term,
        UserDTO instructor,
        String room,
        Integer capacity,
        String schedule,
        Integer enrolledCount
) {
}
