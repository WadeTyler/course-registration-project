package net.tylerwade.registrationsystem.course.dto;

public record CourseAttributeDTO(
        Long id,
        String department,
        Integer code,
        String title,
        String description,
        Integer credits
) {
}
