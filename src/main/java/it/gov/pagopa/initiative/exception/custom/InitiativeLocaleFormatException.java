package it.gov.pagopa.initiative.exception.custom;

import it.gov.pagopa.common.web.exception.ServiceException;
import it.gov.pagopa.common.web.exception.ServiceExceptionPayload;
import it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.BadRequest;

public class InitiativeLocaleFormatException extends ServiceException {

    public InitiativeLocaleFormatException(String message) {
        this(BadRequest.INITIATIVE_INVALID_LOCALE_FORMAT, message);
    }

    public InitiativeLocaleFormatException(String code, String message) {
        this(code, message, null, false, null);
    }

    public InitiativeLocaleFormatException(String code, String message, ServiceExceptionPayload payload, boolean printStackTrace, Throwable ex) {
        super(code, message, payload, printStackTrace, ex);
    }
}
