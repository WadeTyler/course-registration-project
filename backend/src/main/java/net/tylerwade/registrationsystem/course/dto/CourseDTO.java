package net.tylerwade.registrationsystem.course.dto;

import java.time.Instant;

public record CourseDTO(
        Long id,
        String department,
        String code,
        String title,
        String description,
        Integer credits,
        Instant createdAt,
        Instant modifiedAt
) {
}
