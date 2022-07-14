package it.gov.pagopa.initiative.exception;

import it.gov.pagopa.initiative.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
//@Slf4j
public class RestResponseExceptionHandler  {

    // API
    @ExceptionHandler({InitiativeException.class})
    public ResponseEntity<ErrorDTO> handleException(InitiativeException ex) {
        return new ResponseEntity<>(new ErrorDTO(ex.getCode(), ex.getMessage()),
                ex.getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(String.format("[%s]: %s", fieldName, errorMessage));
        });
        String message = String.join(" - ", errors);
        return new ResponseEntity<>(
                new ErrorDTO(message, message), HttpStatus.BAD_REQUEST);
    }

}
