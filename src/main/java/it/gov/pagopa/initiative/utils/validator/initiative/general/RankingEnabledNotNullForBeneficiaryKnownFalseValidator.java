package it.gov.pagopa.initiative.utils.validator.initiative.general;

import it.gov.pagopa.initiative.dto.InitiativeGeneralDTO;
import it.gov.pagopa.initiative.utils.constraint.initiative.general.RankingEnabledNotNullForBeneficiaryKnownFalseConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RankingEnabledNotNullForBeneficiaryKnownFalseValidator implements ConstraintValidator<RankingEnabledNotNullForBeneficiaryKnownFalseConstraint, InitiativeGeneralDTO> {

    @Override
    public boolean isValid(InitiativeGeneralDTO value, ConstraintValidatorContext context) {
        Boolean beneficiaryKnown = value.getBeneficiaryKnown();
        Boolean rankingEnabled = value.getRankingEnabled();
        if(Boolean.FALSE.equals(beneficiaryKnown)){
            return rankingEnabled != null;
        }
        return true;
    }
}
