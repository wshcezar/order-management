package order.management.partner.exception.handler;

import order.management.partner.exception.InsufficientCreditException;
import order.management.partner.exception.PartnerAlreadyExistsException;
import order.management.partner.exception.PartnerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PartnerNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(PartnerNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, List.of(ex.getMessage()));
    }

    @ExceptionHandler(PartnerAlreadyExistsException.class)
    public ResponseEntity<Object> handleAlreadyExists(PartnerAlreadyExistsException ex) {
        return buildResponse(HttpStatus.CONFLICT, List.of(ex.getMessage()));
    }

    @ExceptionHandler(InsufficientCreditException.class)
    public ResponseEntity<Object> handleInsufficientCredit(InsufficientCreditException ex) {
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, List.of(ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errorMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        return buildResponse(HttpStatus.BAD_REQUEST, errorMessages);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGeneric(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, List.of("An unexpected error occurred"));
    }

    private ResponseEntity<Object> buildResponse(HttpStatus status, List<String> messages) {
        Map<String, Object> body = Map.of(
                "timestamp", Instant.now().toString(),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "messages", messages
        );
        return new ResponseEntity<>(body, status);
    }
}