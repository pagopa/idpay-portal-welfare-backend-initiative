package it.gov.pagopa.initiative.exception.custom;

import it.gov.pagopa.common.web.exception.ServiceException;
import it.gov.pagopa.common.web.exception.ServiceExceptionPayload;
import it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.BadRequest;

public class InvalidRewardRuleException extends ServiceException {
    public InvalidRewardRuleException(String message) {
        this(BadRequest.INITIATIVE_REWARD_RULES_NOT_VALID, message);
    }

    public InvalidRewardRuleException(String code, String message) {
        this(code, message, null,false,null);
    }

    public InvalidRewardRuleException(String code, String message, ServiceExceptionPayload payload, boolean printStackTrace, Throwable ex) {
        super(code, message, payload, printStackTrace, ex);
    }
}
