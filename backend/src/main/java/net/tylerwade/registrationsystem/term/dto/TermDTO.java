package net.tylerwade.registrationsystem.term.dto;

import java.util.Date;

public record TermDTO(
        Long id,
        Date startDate,
        Date endDate,
        Date registrationStart,
        Date registrationEnd,
        Date createdAt
) {
}
