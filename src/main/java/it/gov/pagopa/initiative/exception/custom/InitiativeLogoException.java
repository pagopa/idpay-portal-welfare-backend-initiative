package it.gov.pagopa.initiative.exception.custom;

import it.gov.pagopa.common.web.exception.ServiceException;
import it.gov.pagopa.common.web.exception.ServiceExceptionPayload;
import it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.InternalServerError;

public class InitiativeLogoException extends ServiceException {
    public InitiativeLogoException(String message) {
        this(InternalServerError.INITIATIVE_LOGO_ERROR, message);
    }

    public InitiativeLogoException(String code, String message) {
        this(code, message, null, false, null);
    }

    public InitiativeLogoException(String code, String message, ServiceExceptionPayload payload, boolean printStackTrace, Throwable ex) {
        super(code, message, payload, printStackTrace, ex);
    }
}
