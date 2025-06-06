package net.tylerwade.registrationsystem.term.dto;

import java.time.LocalDate;
import java.util.Date;

public record TermDTO(
        Long id,
        LocalDate startDate,
        LocalDate endDate,
        LocalDate registrationStart,
        LocalDate registrationEnd,
        Date createdAt
) {
}
