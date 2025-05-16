package net.tylerwade.registrationsystem.coursesection.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ManageCourseSectionRequest(

        @NotNull(message = "Term ID is required.")
        Long termId,

        String instructorId,

        @NotBlank(message = "Room is required.")
        @Size(max = 255, message = "Room must be less than 255 characters.")
        String room,

        @NotNull(message = "Capacity is required.")
        @PositiveOrZero(message = "Capacity must be positive or zero.")
        Integer capacity,

        @NotBlank(message = "Schedule is required.")
        @Size(max = 255, message = "Schedule must be less than 255 characters.")
        String schedule
) {
}
