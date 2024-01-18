package it.gov.pagopa.initiative.exception.custom;

import it.gov.pagopa.common.web.exception.ServiceException;
import it.gov.pagopa.common.web.exception.ServiceExceptionPayload;
import it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.BadRequest;

public class InitiativeYearValueException extends ServiceException {
    public InitiativeYearValueException(String message) {
        this(BadRequest.INITIATIVE_YEAR_VALUE_NOT_VALID, message);
    }

    public InitiativeYearValueException(String code, String message) {
        this(code, message, null,false,null);
    }

    public InitiativeYearValueException(String code, String message, ServiceExceptionPayload payload, boolean printStackTrace, Throwable ex) {
        super(code, message, payload, printStackTrace, ex);
    }
}
