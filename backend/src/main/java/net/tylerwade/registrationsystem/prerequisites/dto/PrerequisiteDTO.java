package net.tylerwade.registrationsystem.prerequisites.dto;

import java.time.Instant;

public record PrerequisiteDTO(
    Long id,
    Long courseId,
    Long requiredCourseId,
    String requiredCourseDepartment,
    String requiredCourseCode,
    Character minimumGrade,
    Instant createdAt,
    Instant modifiedAt
) {
}
