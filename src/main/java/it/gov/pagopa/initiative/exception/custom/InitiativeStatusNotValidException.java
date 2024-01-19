package it.gov.pagopa.initiative.exception.custom;

import it.gov.pagopa.common.web.exception.ServiceException;
import it.gov.pagopa.common.web.exception.ServiceExceptionPayload;
import it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.BadRequest;

public class InitiativeStatusNotValidException extends ServiceException {
    public InitiativeStatusNotValidException(String message) {
        this(BadRequest.INITIATIVE_STATUS_NOT_VALID, message);
    }

    public InitiativeStatusNotValidException(String code, String message) {
        this(code, message, null,false,null);
    }

    public InitiativeStatusNotValidException(String code, String message, ServiceExceptionPayload payload, boolean printStackTrace, Throwable ex) {
        super(code, message, payload, printStackTrace, ex);
    }
}
