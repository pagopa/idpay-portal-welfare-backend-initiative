package it.gov.pagopa.initiative.exception.custom;

import it.gov.pagopa.common.web.exception.ServiceException;
import it.gov.pagopa.common.web.exception.ServiceExceptionPayload;
import it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.BadRequest;

public class InvalidRefundRuleException extends ServiceException {
    public InvalidRefundRuleException(String message) {
        this(BadRequest.INITIATIVE_REFUND_RULES_NOT_VALID, message);
    }

    public InvalidRefundRuleException(String code, String message) {
        this(code, message, null,false,null);
    }

    public InvalidRefundRuleException(String code, String message, ServiceExceptionPayload payload, boolean printStackTrace, Throwable ex) {
        super(code, message, payload, printStackTrace, ex);
    }
}
