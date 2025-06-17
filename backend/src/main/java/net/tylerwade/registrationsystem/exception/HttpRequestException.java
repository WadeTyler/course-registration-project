package net.tylerwade.registrationsystem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.IOException;

/**
 * Custom exception class for handling HTTP request-related errors.
 * Extends the {@link IOException} class to represent input/output exceptions.
 */
public class HttpRequestException extends IOException {

    /**
     * The HTTP status associated with the exception.
     */
    @Getter
    private final HttpStatus httpStatus;

    /**
     * Constructs a new HttpRequestException with the specified HTTP status and message.
     *
     * @param httpStatus The HTTP status associated with the exception.
     * @param message    The detail message for the exception.
     */
    public HttpRequestException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }

    /**
     * Constructs a new HttpRequestException with the specified HTTP status.
     *
     * @param httpStatus The HTTP status associated with the exception.
     */
    public HttpRequestException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

}