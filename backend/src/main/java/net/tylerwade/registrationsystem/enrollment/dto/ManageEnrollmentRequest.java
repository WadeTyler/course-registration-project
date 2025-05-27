package net.tylerwade.registrationsystem.enrollment.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ManageEnrollmentRequest(

        @NotNull(message = "Grade is required.")
        @PositiveOrZero(message = "Grade must be positive or zero.")
        @Max(100)
        BigDecimal grade,

        @NotBlank(message = "Status is required.")
        @Size(max = 255, message = "Status must be less than 255 characters.")
        String status
) {
}
