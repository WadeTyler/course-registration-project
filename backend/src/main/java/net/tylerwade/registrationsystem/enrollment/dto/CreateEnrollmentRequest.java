package net.tylerwade.registrationsystem.enrollment.dto;

import jakarta.validation.constraints.NotNull;

public record CreateEnrollmentRequest(
        @NotNull(message = "Course Section ID is required.")
        Long courseSectionId
) {
}
