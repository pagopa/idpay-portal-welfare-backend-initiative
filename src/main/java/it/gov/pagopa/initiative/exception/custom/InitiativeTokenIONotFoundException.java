package it.gov.pagopa.initiative.exception.custom;

import it.gov.pagopa.common.web.exception.ServiceException;
import it.gov.pagopa.common.web.exception.ServiceExceptionPayload;
import it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.NotFound;

public class InitiativeTokenIONotFoundException extends ServiceException {
    public InitiativeTokenIONotFoundException(String message) {
        this(NotFound.INITIATIVE_PRIMARY_AND_SECONDARY_TOKEN_IO_NOT_FOUND, message);
    }

    public InitiativeTokenIONotFoundException(String code, String message) {
        this(code, message, null, false, null);
    }

    public InitiativeTokenIONotFoundException(String code, String message, ServiceExceptionPayload payload, boolean printStackTrace, Throwable ex) {
        super(code, message, payload, printStackTrace, ex);
    }
}
