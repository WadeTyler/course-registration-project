package net.tylerwade.registrationsystem.term.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ManageTermRequest(
        @NotNull(message = "Term start date is required")
        @FutureOrPresent(message = "Term start date must be today or in the future")
        LocalDate startDate,

        @NotNull(message = "Term end date is required")
        LocalDate endDate,

        @NotNull(message = "Registration start date is required")
        @FutureOrPresent(message = "Registration start date must be today or in the future")
        LocalDate registrationStart,

        @NotNull(message = "Registration end date is required")
        LocalDate registrationEnd
) {

    @JsonIgnore
    public boolean isValid() {
        return startDate != null
                && endDate != null
                && registrationStart != null
                && registrationEnd != null
                && endDate.isAfter(startDate)
                && registrationEnd.isAfter(registrationStart)
                && (registrationEnd.isBefore(startDate) || registrationEnd.equals(startDate));
    }

}
