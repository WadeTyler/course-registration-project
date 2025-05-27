package net.tylerwade.registrationsystem.coursesection.dto;

import net.tylerwade.registrationsystem.auth.dto.UserDTO;
import net.tylerwade.registrationsystem.course.dto.CourseAttributeDTO;
import net.tylerwade.registrationsystem.enrollment.dto.InstructorEnrollmentDTO;
import net.tylerwade.registrationsystem.term.dto.TermDTO;

import java.util.Date;
import java.util.List;

public record InstructorCourseSectionDTO(
        Long id,
        CourseAttributeDTO course,
        TermDTO term,
        UserDTO instructor,
        String room,
        Integer capacity,
        String schedule,
        Integer enrolledCount,
        List<InstructorEnrollmentDTO> enrollments,
        Date createdAt
) {
}
