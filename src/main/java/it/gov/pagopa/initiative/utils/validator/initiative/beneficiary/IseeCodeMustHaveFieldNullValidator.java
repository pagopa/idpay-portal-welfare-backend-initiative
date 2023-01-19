package it.gov.pagopa.initiative.utils.validator.initiative.beneficiary;

import it.gov.pagopa.initiative.dto.AutomatedCriteriaDTO;
import it.gov.pagopa.initiative.utils.constraint.initiative.beneficiary.IseeCodeMustHaveFieldNullConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IseeCodeMustHaveFieldNullValidator implements ConstraintValidator<IseeCodeMustHaveFieldNullConstraint, AutomatedCriteriaDTO> {
    private static final String ISEE = "ISEE";

    @Override
    public boolean isValid(AutomatedCriteriaDTO value, ConstraintValidatorContext context) {
        String code = value.getCode();
        String field = value.getField();
        return !ISEE.equals(code) || field == null;
    }
}
