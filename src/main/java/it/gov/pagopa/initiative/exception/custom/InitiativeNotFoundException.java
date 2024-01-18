package it.gov.pagopa.initiative.exception.custom;

import it.gov.pagopa.common.web.exception.ServiceException;
import it.gov.pagopa.common.web.exception.ServiceExceptionPayload;
import it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.NotFound;

public class InitiativeNotFoundException extends ServiceException {
    public InitiativeNotFoundException(String message) {
        this(NotFound.INITIATIVE_NOT_FOUND, message);
    }

    public InitiativeNotFoundException(String code, String message) {
        this(code, message, null, false, null);
    }

    public InitiativeNotFoundException(String code, String message, ServiceExceptionPayload payload, boolean printStackTrace, Throwable ex) {
        super(code, message, payload, printStackTrace, ex);
    }
}
