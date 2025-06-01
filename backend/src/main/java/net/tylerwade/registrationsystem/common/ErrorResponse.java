package net.tylerwade.registrationsystem.common;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    LocalDateTime timestamp;
    int status;
    String error;
    String message;

}
