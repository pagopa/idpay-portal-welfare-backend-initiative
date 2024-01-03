package it.gov.pagopa.initiative.exception.custom;

import it.gov.pagopa.common.web.exception.ServiceException;
import it.gov.pagopa.common.web.exception.ServiceExceptionPayload;
import it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.BadRequest;

public class InitiativePublicationException extends ServiceException {

    public InitiativePublicationException(String message) {
        this(BadRequest.INITIATIVE_ROLLBACK_TO_PREVIOUS_STATUS, message);
    }

    public InitiativePublicationException(String code, String message) {
        this(code, message, null, false, null);
    }

    public InitiativePublicationException(String code, String message, ServiceExceptionPayload payload, boolean printStackTrace, Throwable ex) {
        super(code, message, payload, printStackTrace, ex);
    }
}
