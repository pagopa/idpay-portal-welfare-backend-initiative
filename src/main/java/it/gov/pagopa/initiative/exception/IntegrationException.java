package it.gov.pagopa.initiative.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class IntegrationException extends RuntimeException{

    private final HttpStatus httpStatus;
}
