package net.tylerwade.registrationsystem.prerequisites.dto;

import jakarta.validation.constraints.NotNull;

public record ManagePrerequisiteRequest(

        @NotNull(message = "Required course Id is required.")
        Long requiredCourseId,

        @NotNull(message = "Minimum grade is required.")
        Character minimumGrade
) {
}
