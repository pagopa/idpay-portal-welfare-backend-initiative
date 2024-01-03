package it.gov.pagopa.initiative.exception.custom;

import it.gov.pagopa.common.web.exception.ServiceException;
import it.gov.pagopa.common.web.exception.ServiceExceptionPayload;
import it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.BadRequest;

public class InitiativeDateInvalidException extends ServiceException {
    public InitiativeDateInvalidException(String message) {
        this(BadRequest.INITIATIVE_START_DATE_AND_END_DATE_NOT_VALID, message);
    }

    public InitiativeDateInvalidException (String code, String message) {
        this(code, message, null,false,null);
    }

    public InitiativeDateInvalidException (String code, String message, ServiceExceptionPayload payload, boolean printStackTrace, Throwable ex) {
        super(code, message, payload, printStackTrace, ex);
    }
}
