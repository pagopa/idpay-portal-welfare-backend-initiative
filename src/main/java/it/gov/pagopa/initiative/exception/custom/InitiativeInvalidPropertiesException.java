package it.gov.pagopa.initiative.exception.custom;

import it.gov.pagopa.common.web.exception.ServiceException;
import it.gov.pagopa.common.web.exception.ServiceExceptionPayload;
import it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.BadRequest;

public class InitiativeInvalidPropertiesException extends ServiceException {
    public InitiativeInvalidPropertiesException( String message) {
        this(BadRequest.INITIATIVE_WHITELIST_INVALID_PROPERTIES, message);
    }

    public InitiativeInvalidPropertiesException(String code, String message) {
        this(code, message, null,false,null);
    }

    public InitiativeInvalidPropertiesException(String code, String message, ServiceExceptionPayload payload, boolean printStackTrace, Throwable ex) {
        super(code, message, payload, printStackTrace, ex);
    }
}
