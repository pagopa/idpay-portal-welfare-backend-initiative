package it.gov.pagopa.initiative.exception;

import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.dto.ErrorDTO;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class RestResponseExceptionHandler {

    // API
    @ExceptionHandler({InitiativeException.class})
    public ResponseEntity<ErrorDTO> handleInitiativeException(InitiativeException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(new ErrorDTO(ex.getCode(), ex.getMessage()),
                ex.getHttpStatus());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorDTO> handleMethodArgumentNotValidExceptions(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage());
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            if(error instanceof FieldError fieldErrorInput) {
                String fieldName = fieldErrorInput.getField();
                String errorMessage = error.getDefaultMessage();
                errors.add(String.format("[%s]: %s", fieldName, errorMessage));
            }
            else {
                String objectName = error.getObjectName();
                String errorMessage = error.getDefaultMessage();
                errors.add(String.format("[%s]: %s", objectName, errorMessage));
            }
        });
        String message = String.join(" - ", errors);
        return new ResponseEntity<>(
                new ErrorDTO(InitiativeConstants.Exception.BadRequest.CODE, message), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error(ex.getMessage());
        String localizedMessage = StringUtils.split(ex.getMostSpecificCause().getLocalizedMessage(), System.lineSeparator())[0];
        return new ResponseEntity<>(
                new ErrorDTO(InitiativeConstants.Exception.BadRequest.CODE, localizedMessage), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IntegrationException.class})
    public ResponseEntity<ErrorDTO> handleIntegrationException(IntegrationException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                new ErrorDTO(InitiativeConstants.Exception.Publish.BadRequest.CODE, InitiativeConstants.Exception.Publish.BadRequest.INTEGRATION_FAILED), ex.getHttpStatus());
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorDTO> handleGenericException(Exception ex) {
        log.error(ex.getMessage());
        return new ResponseEntity<>(
                new ErrorDTO(InitiativeConstants.Exception.GeneralError.CODE, ex.getLocalizedMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
