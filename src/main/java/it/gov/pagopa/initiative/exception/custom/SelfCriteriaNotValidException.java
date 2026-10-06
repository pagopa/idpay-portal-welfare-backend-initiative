package it.gov.pagopa.initiative.exception.custom;

import it.gov.pagopa.common.web.exception.ServiceException;
import it.gov.pagopa.common.web.exception.ServiceExceptionPayload;
import it.gov.pagopa.initiative.constants.InitiativeConstants;

public class SelfCriteriaNotValidException extends ServiceException {
    public SelfCriteriaNotValidException(String message) {
        this(InitiativeConstants.Exception.BadRequest.INITIATIVE_SELF_CRITERIA_NOT_VALID, message);
    }

    public SelfCriteriaNotValidException(String code, String message) {
        this(code, message, null,false,null);
    }

    public SelfCriteriaNotValidException(String code, String message, ServiceExceptionPayload payload, boolean printStackTrace, Throwable ex) {
        super(code, message, payload, printStackTrace, ex);
    }
}