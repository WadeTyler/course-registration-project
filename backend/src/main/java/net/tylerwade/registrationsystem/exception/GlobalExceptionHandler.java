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


@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final AppProperties appProperties;

    public GlobalExceptionHandler(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    // HttpRequestException
    @ExceptionHandler(HttpRequestException.class)
    public ResponseEntity<?> handleHttpRequestException(HttpRequestException e) {
        printDebugMessage(e);
        return ResponseEntity.status(e.getHttpStatus()).body(new ErrorResponse(Instant.now(), e.getHttpStatus().value(), e.getHttpStatus().getReasonPhrase(), e.getMessage()));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<?> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
        printDebugMessage(e);
        return ResponseEntity.status(UNAUTHORIZED).body(new ErrorResponse(Instant.now(), UNAUTHORIZED.value(), UNAUTHORIZED.getReasonPhrase(), e.getMessage()));
    }

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


    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleNoResourceFoundException(NoResourceFoundException e) {
        return ResponseEntity.status(e.getStatusCode()).body(new ErrorResponse(Instant.now(), NOT_FOUND.value(), NOT_FOUND.getReasonPhrase(), e.getMessage()));
    }

    // Catch all
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        printDebugMessage(e);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ErrorResponse(Instant.now(), INTERNAL_SERVER_ERROR.value(), INTERNAL_SERVER_ERROR.getReasonPhrase(), appProperties.isProduction() ? "Something unexpected happened. Try again later." : e.getMessage()));
    }

    // Utility function to print stack trace in not in production mode
    private void printDebugMessage(Exception e) {
        if (!appProperties.isProduction()) {
            log.error("Global Exception Caught: ", e);
        }
    }

}
