package net.tylerwade.registrationsystem.exception;

import lombok.extern.slf4j.Slf4j;
import net.tylerwade.registrationsystem.common.ErrorResponse;
import net.tylerwade.registrationsystem.config.AppProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.Instant;

import static org.springframework.http.HttpStatus.*;

/**
 * Global exception handler for the application.
 * Provides centralized handling of various exceptions thrown during API requests.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final AppProperties appProperties;

    /**
     * Constructs a GlobalExceptionHandler with the specified application properties.
     *
     * @param appProperties The application properties used for configuration.
     */
    public GlobalExceptionHandler(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    /**
     * Handles HttpRequestException and returns an appropriate error response.
     *
     * @param e The HttpRequestException to handle.
     * @return A ResponseEntity containing the error response.
     */
    @ExceptionHandler(HttpRequestException.class)
    public ResponseEntity<?> handleHttpRequestException(HttpRequestException e) {
        printDebugMessage(e);
        return ResponseEntity.status(e.getHttpStatus()).body(new ErrorResponse(Instant.now(), e.getHttpStatus().value(), e.getHttpStatus().getReasonPhrase(), e.getMessage()));
    }

    /**
     * Handles AuthorizationDeniedException and returns an appropriate error response.
     *
     * @param e The AuthorizationDeniedException to handle.
     * @return A ResponseEntity containing the error response.
     */
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<?> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
        printDebugMessage(e);
        return ResponseEntity.status(UNAUTHORIZED).body(new ErrorResponse(Instant.now(), UNAUTHORIZED.value(), UNAUTHORIZED.getReasonPhrase(), e.getMessage()));
    }

    /**
     * Handles MethodArgumentNotValidException and returns an appropriate error response.
     * Aggregates validation errors into a single message.
     *
     * @param e The MethodArgumentNotValidException to handle.
     * @return A ResponseEntity containing the error response.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        printDebugMessage(e);

        // Build message
        StringBuilder errors = new StringBuilder();

        for (int i = 0; i < e.getBindingResult().getAllErrors().size(); i++) {
            errors.append(e.getBindingResult().getAllErrors().get(i).getDefaultMessage());
            if (i != e.getBindingResult().getAllErrors().size() - 1) {
                errors.append("\n");
            }
        }

        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(Instant.now(), BAD_REQUEST.value(), BAD_REQUEST.getReasonPhrase(), errors.toString()));
    }

    /**
     * Handles NoResourceFoundException and returns an appropriate error response.
     *
     * @param e The NoResourceFoundException to handle.
     * @return A ResponseEntity containing the error response.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleNoResourceFoundException(NoResourceFoundException e) {
        return ResponseEntity.status(e.getStatusCode()).body(new ErrorResponse(Instant.now(), NOT_FOUND.value(), NOT_FOUND.getReasonPhrase(), e.getMessage()));
    }

    /**
     * Handles generic exceptions and returns an appropriate error response.
     * Provides a generic message in production mode.
     *
     * @param e The Exception to handle.
     * @return A ResponseEntity containing the error response.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        printDebugMessage(e);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ErrorResponse(Instant.now(), INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR.getReasonPhrase(), appProperties.isProduction() ? "Something unexpected happened. Try again later." : e.getMessage()));
    }

    /**
     * Utility function to print stack trace if not in production mode.
     *
     * @param e The exception to log.
     */
    private void printDebugMessage(Exception e) {
        if (!appProperties.isProduction()) {
            log.error("Global Exception Caught: ", e);
        }
    }

}