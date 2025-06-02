package net.tylerwade.registrationsystem.enrollment.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import net.tylerwade.registrationsystem.enrollment.enums.EnrollmentStatus;

import java.math.BigDecimal;

public record ManageEnrollmentRequest(

        @NotNull(message = "Grade is required.")
        @PositiveOrZero(message = "Grade must be positive or zero.")
        @Max(100)
        BigDecimal grade,

        @NotNull(message = "Status is required.")
        EnrollmentStatus status
) {
}
