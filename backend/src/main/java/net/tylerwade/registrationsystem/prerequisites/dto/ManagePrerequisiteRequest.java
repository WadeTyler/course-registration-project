package net.tylerwade.registrationsystem.prerequisites.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record ManagePrerequisiteRequest(

        @NotNull(message = "Required course Id is required.")
        Long requiredCourseId,

        @NotNull(message = "Minimum grade is required.")
        @PositiveOrZero(message = "Minimum Grade must be positive or zero.")
        @Max(100)
        Integer minimumGrade
) {
}
