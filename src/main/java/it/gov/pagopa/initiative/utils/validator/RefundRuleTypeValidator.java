package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.rule.refund.AccumulatedAmountDTO;
import it.gov.pagopa.initiative.dto.rule.refund.InitiativeRefundRuleDTO;
import it.gov.pagopa.initiative.dto.rule.refund.TimeParameterDTO;
import it.gov.pagopa.initiative.utils.constraint.RefundRuleType;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RefundRuleTypeValidator implements ConstraintValidator<RefundRuleType, InitiativeRefundRuleDTO> {

    @Override
    public boolean isValid(InitiativeRefundRuleDTO value, ConstraintValidatorContext context) {
        AccumulatedAmountDTO amountDTO = value.getAccumulatedAmount();
        TimeParameterDTO timeParameterDTO = value.getTimeParameter();
        if ((amountDTO == null && timeParameterDTO != null) || (amountDTO != null && timeParameterDTO == null)){
            return true;
        }
        return false;
    }
}
