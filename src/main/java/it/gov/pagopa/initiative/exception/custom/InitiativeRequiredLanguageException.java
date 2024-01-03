package it.gov.pagopa.initiative.exception.custom;

import it.gov.pagopa.common.web.exception.ServiceException;
import it.gov.pagopa.common.web.exception.ServiceExceptionPayload;
import it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.BadRequest;

public class InitiativeRequiredLanguageException extends ServiceException {
    public InitiativeRequiredLanguageException(String message) {
        this( BadRequest.INITIATIVE_ITALIAN_LANGUAGE_REQUIRED_FOR_DESCRIPTION, message);
    }

    public InitiativeRequiredLanguageException(String code, String message) {
        this(code, message, null, false, null);
    }

    public InitiativeRequiredLanguageException(String code, String message, ServiceExceptionPayload payload, boolean printStackTrace, Throwable ex) {
        super(code, message, payload, printStackTrace, ex);
    }
}
