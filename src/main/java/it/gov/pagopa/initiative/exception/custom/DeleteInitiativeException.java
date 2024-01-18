package it.gov.pagopa.initiative.exception.custom;

import it.gov.pagopa.common.web.exception.ServiceException;
import it.gov.pagopa.common.web.exception.ServiceExceptionPayload;
import it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.BadRequest;

public class DeleteInitiativeException extends ServiceException {
    public DeleteInitiativeException(String message) {
        this(BadRequest.INITIATIVE_CANNOT_BE_DELETED, message);
    }

    public DeleteInitiativeException(String code, String message) {
        this(code, message, null,false,null);
    }

    public DeleteInitiativeException(String code, String message, ServiceExceptionPayload payload, boolean printStackTrace, Throwable ex) {
        super(code, message, payload, printStackTrace, ex);
    }
}
