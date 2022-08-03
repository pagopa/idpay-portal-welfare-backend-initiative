package it.gov.pagopa.initiative.exception;

import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.dto.ErrorDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
//@Slf4j
public class RestResponseExceptionHandler {

    // API
    @ExceptionHandler({InitiativeException.class})
    public ResponseEntity<ErrorDTO> handleInitiativeException(InitiativeException ex) {
        return new ResponseEntity<>(new ErrorDTO(ex.getCode(), ex.getMessage()),
                ex.getHttpStatus());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorDTO> handleMethodArgumentNotValidExceptions(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.add(String.format("[%s]: %s", fieldName, errorMessage));
        });
        String message = String.join(" - ", errors);
        return new ResponseEntity<>(
                new ErrorDTO(InitiativeConstants.Exception.BadRequest.CODE_PACKAGE, message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String localizedMessage = StringUtils.split(ex.getMostSpecificCause().getLocalizedMessage(), System.lineSeparator())[0];
        return new ResponseEntity<>(
                new ErrorDTO(InitiativeConstants.Exception.BadRequest.CODE_PACKAGE, localizedMessage), HttpStatus.BAD_REQUEST);
    }

}
