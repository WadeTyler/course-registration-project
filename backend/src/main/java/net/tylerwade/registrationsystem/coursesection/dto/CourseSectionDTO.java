package net.tylerwade.registrationsystem.coursesection.dto;

import net.tylerwade.registrationsystem.auth.dto.UserDTO;
import net.tylerwade.registrationsystem.course.dto.CourseAttributeDTO;
import net.tylerwade.registrationsystem.term.dto.TermDTO;

import java.util.Date;

public record CourseSectionDTO(
        Long id,
        CourseAttributeDTO course,
        TermDTO term,
        UserDTO instructor,
        String room,
        Integer capacity,
        String schedule,
        Integer enrolledCount,
        Date createdAt
) {
}
