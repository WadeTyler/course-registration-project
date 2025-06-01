package net.tylerwade.registrationsystem.common;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    Instant timestamp;
    int status;
    String error;
    String message;

}
