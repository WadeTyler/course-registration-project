package net.tylerwade.registrationsystem.enrollment.dto;

import jakarta.validation.constraints.*;

public record ManageEnrollmentRequest(

        @NotNull(message = "Grade is required.")
        @PositiveOrZero(message = "Grade must be positive or zero.")
        @Max(100)
        Integer grade,

        @NotBlank(message = "Status is required.")
        @Size(max = 50, message = "Status must be less than 50 characters.")
        String status
) {
}
