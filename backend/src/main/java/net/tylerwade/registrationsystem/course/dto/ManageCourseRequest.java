package net.tylerwade.registrationsystem.course.dto;

import jakarta.validation.constraints.*;

public record ManageCourseRequest(
        @NotBlank(message = "Department is required.")
        @Size(max = 10, message = "Department must be at most 10 characters.")
        String department,

        @NotNull(message = "Code is required.")
        @PositiveOrZero(message = "Code must be positive or zero.")
        Integer code,

        @NotBlank(message = "Title is required.")
        @Size(max = 100, message = "Title must be at most 100 characters.")
        String title,

        @NotBlank(message = "Description is required.")
        @Size(max = 1000, message = "Description must be at most 1000 characters.")
        String description,

        @NotNull(message = "Credits are required.")
        @Positive(message = "Credits must be a positive number.")
        Integer credits
) {
}
