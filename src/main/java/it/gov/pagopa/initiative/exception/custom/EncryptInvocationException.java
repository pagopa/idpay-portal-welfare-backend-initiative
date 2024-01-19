package it.gov.pagopa.initiative.exception.custom;

import it.gov.pagopa.common.web.exception.ServiceException;
import it.gov.pagopa.common.web.exception.ServiceExceptionPayload;
import it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.InternalServerError;

public class EncryptInvocationException extends ServiceException {
    public EncryptInvocationException(String code, String message) {
        this(code, message, null,false,null);
    }
    public EncryptInvocationException(String message, boolean printStackTrace, Throwable ex) {
        this(InternalServerError.INITIATIVE_GENERIC_ERROR, message,null, printStackTrace, ex);
    }

    public EncryptInvocationException(String code, String message, ServiceExceptionPayload payload, boolean printStackTrace, Throwable ex) {
        super(code, message, payload, printStackTrace, ex);
    }
}
