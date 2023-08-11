package it.gov.pagopa.initiative.exception;

import it.gov.pagopa.common.web.exception.ClientExceptionWithBody;
import it.gov.pagopa.initiative.constants.InitiativeConstants;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@SuppressWarnings("squid:S110")
public class IntegrationException extends ClientExceptionWithBody {

    public IntegrationException(HttpStatus httpStatus) {
        super(httpStatus, InitiativeConstants.Exception.Publish.BadRequest.CODE,
            InitiativeConstants.Exception.Publish.BadRequest.INTEGRATION_FAILED);
    }
}
