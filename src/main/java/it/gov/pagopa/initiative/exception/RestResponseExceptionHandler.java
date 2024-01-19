package it.gov.pagopa.initiative.exception;

import it.gov.pagopa.common.web.dto.ErrorDTO;
import it.gov.pagopa.initiative.constants.InitiativeConstants.Exception;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestResponseExceptionHandler {

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<ErrorDTO> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error(ex.getMessage());
        String localizedMessage = StringUtils.split(ex.getMostSpecificCause().getLocalizedMessage(), System.lineSeparator())[0];
        return new ResponseEntity<>(
                new ErrorDTO(Exception.BadRequest.INITIATIVE_INVALID_REQUEST, localizedMessage), HttpStatus.BAD_REQUEST);
    }
}
