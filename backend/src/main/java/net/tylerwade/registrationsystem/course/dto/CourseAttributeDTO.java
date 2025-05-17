package net.tylerwade.registrationsystem.course.dto;

public record CourseAttributeDTO(
        Long id,
        String department,
        String code,
        String title,
        String description,
        Integer credits
) {
}
