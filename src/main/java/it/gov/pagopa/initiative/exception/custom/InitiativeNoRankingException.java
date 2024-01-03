package it.gov.pagopa.initiative.exception.custom;

import it.gov.pagopa.common.web.exception.ServiceException;
import it.gov.pagopa.common.web.exception.ServiceExceptionPayload;
import it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.InternalServerError;

public class InitiativeNoRankingException extends ServiceException {
    public InitiativeNoRankingException(String message, boolean printStackTrace, Throwable ex) {
        this(InternalServerError.INITIATIVE_GENERIC_ERROR, message, null, printStackTrace, ex);
    }

    public InitiativeNoRankingException(String code, String message, ServiceExceptionPayload payload, boolean printStackTrace, Throwable ex) {
        super(code, message, payload, printStackTrace, ex);
    }
}
