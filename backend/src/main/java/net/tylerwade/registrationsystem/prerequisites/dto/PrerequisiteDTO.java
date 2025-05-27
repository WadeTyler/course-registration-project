package net.tylerwade.registrationsystem.prerequisites.dto;

import java.math.BigDecimal;
import java.util.Date;

public record PrerequisiteDTO(
    Long id,
    Long courseId,
    Long requiredCourseId,
    String requiredCourseDepartment,
    Integer requiredCourseCode,
    BigDecimal minimumGrade,
    Date createdAt
) {
}
