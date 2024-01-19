package it.gov.pagopa.initiative.exception.custom;

import it.gov.pagopa.common.web.exception.ServiceException;
import it.gov.pagopa.common.web.exception.ServiceExceptionPayload;

public class AutomatedCriteriaNotValidException extends ServiceException {

    public AutomatedCriteriaNotValidException(String code, String message) {
        this(code, message, null,false,null);
    }

    public AutomatedCriteriaNotValidException(String code, String message, ServiceExceptionPayload payload, boolean printStackTrace, Throwable ex) {
        super(code, message, payload, printStackTrace, ex);
    }
}
