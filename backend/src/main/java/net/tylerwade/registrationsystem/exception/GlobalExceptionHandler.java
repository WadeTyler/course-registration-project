package net.tylerwade.registrationsystem.exception;

import lombok.extern.slf4j.Slf4j;
import net.tylerwade.registrationsystem.common.APIResponse;
import net.tylerwade.registrationsystem.config.AppProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;


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
        return ResponseEntity.status(e.getHttpStatus()).body(new APIResponse<>(false, e.getMessage(), null));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<?> handleAuthorizationDeniedException(AuthorizationDeniedException e) {
        printDebugMessage(e);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new APIResponse<>(false, e.getMessage(), null));
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

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new APIResponse<>(false, errors.toString(), null));
    }


    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleNoResourceFoundException(NoResourceFoundException e) {
        return ResponseEntity.status(e.getStatusCode()).body(new APIResponse<>(false, e.getMessage(), null));
    }

    // Catch all
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        printDebugMessage(e);
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new APIResponse<>(false, "Something unexpected happened. Try again later.", null));
    }

    // Utility function to print stack trace in not in production mode
    private void printDebugMessage(Exception e) {
        if (!appProperties.isProduction()) {
            log.error("Global Exception Caught: ", e);
        }
    }

}
