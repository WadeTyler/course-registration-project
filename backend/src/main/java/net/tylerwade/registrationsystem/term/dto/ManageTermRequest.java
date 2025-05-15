package net.tylerwade.registrationsystem.term.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.sql.Date;

public record ManageTermRequest(
        @NotBlank(message = "Term title is required.")
        @Size(min = 3, max = 50, message = "Title must be between 3 - 50 characters.")
        String title,

        @NotNull(message = "Term start date is required")
        @FutureOrPresent(message = "Term start date must be today or in the future")
        Date startDate,

        @NotNull(message = "Term end date is required")
        Date endDate,

        @NotNull(message = "Registration start date is required")
        @FutureOrPresent(message = "Registration start date must be today or in the future")
        Date registrationStart,

        @NotNull(message = "Registration end date is required")
        Date registrationEnd
) {

    public boolean isValid() {
        return title != null
                && !title.isBlank()
                && startDate != null
                && endDate != null
                && registrationStart != null
                && registrationEnd != null
                && endDate.after(startDate)
                && registrationEnd.after(registrationStart)
                && (registrationEnd.before(startDate) || registrationEnd.equals(startDate));
    }

}
