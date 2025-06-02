package net.tylerwade.registrationsystem.common;

import lombok.*;

import java.time.Instant;

/**
 * Represents an error response object used to provide details about errors
 * encountered during API requests.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    /**
     * The timestamp when the error occurred.
     */
    Instant timestamp;

    /**
     * The HTTP status code associated with the error.
     */
    int status;

    /**
     * A short description of the error.
     */
    String error;

    /**
     * A detailed message explaining the error.
     */
    String message;

}