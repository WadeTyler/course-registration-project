package net.tylerwade.registrationsystem.term.dto;

import java.sql.Date;
import java.time.Instant;

public record TermDTO(
        Long id,
        String title,
        Date startDate,
        Date endDate,
        Date registrationStart,
        Date registrationEnd,
        Instant createdAt,
        Instant modifiedAt
) {
}
