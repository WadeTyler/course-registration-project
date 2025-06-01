package net.tylerwade.registrationsystem.term.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record ManageTermRequest(
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

    @JsonIgnore
    public boolean isValid() {
        return startDate != null
                && endDate != null
                && registrationStart != null
                && registrationEnd != null
                && endDate.after(startDate)
                && registrationEnd.after(registrationStart)
                && (registrationEnd.before(startDate) || registrationEnd.equals(startDate));
    }

}
