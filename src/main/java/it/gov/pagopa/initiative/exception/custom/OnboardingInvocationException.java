package it.gov.pagopa.initiative.exception.custom;

import it.gov.pagopa.common.web.exception.ServiceException;
import it.gov.pagopa.common.web.exception.ServiceExceptionPayload;
import it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.InternalServerError;

public class OnboardingInvocationException extends ServiceException {
    public OnboardingInvocationException(String code, String message) {
        this(InternalServerError.INITIATIVE_GENERIC_ERROR, message, null, false, null);
    }

    public OnboardingInvocationException(String code, String message, ServiceExceptionPayload payload, boolean printStackTrace, Throwable ex) {
        super(code, message, payload, printStackTrace, ex);
    }
}
