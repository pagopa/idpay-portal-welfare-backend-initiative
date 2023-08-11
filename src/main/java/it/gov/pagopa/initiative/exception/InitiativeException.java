package it.gov.pagopa.initiative.exception;

import it.gov.pagopa.common.web.exception.ClientExceptionWithBody;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@SuppressWarnings("squid:S110")
public class InitiativeException extends ClientExceptionWithBody {
  public InitiativeException(String code, String message, HttpStatus httpStatus){
    super(httpStatus, code, message);
  }

}
