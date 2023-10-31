package it.gov.pagopa.initiative.utils.validator.initiative.general;

import it.gov.pagopa.initiative.dto.InitiativeRewardAndTrxRulesDTO;
import it.gov.pagopa.initiative.utils.constraint.initiative.general.TRXRuleNullIfDiscountConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TRXRuleNullIfDiscountValidator implements ConstraintValidator<TRXRuleNullIfDiscountConstraint, InitiativeRewardAndTrxRulesDTO> {
    private static final String REWARD_TYPE = "DISCOUNT";

    @Override
    public boolean isValid(InitiativeRewardAndTrxRulesDTO value, ConstraintValidatorContext context) {
        String code = value.getInitiativeRewardType().toString();
        return !REWARD_TYPE.equals(code) ||
                value.getTrxRule() == null ||
                value.getTrxRule().getTrxCount() == null ||
                value.getTrxRule().getTrxCount().getFrom() == null;
    }
}
