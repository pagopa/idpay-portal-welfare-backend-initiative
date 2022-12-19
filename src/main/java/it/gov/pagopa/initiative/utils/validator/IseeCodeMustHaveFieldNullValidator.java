package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.AutomatedCriteriaDTO;
import it.gov.pagopa.initiative.utils.constraint.IseeCodeMustHaveFieldNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IseeCodeMustHaveFieldNullValidator implements ConstraintValidator<IseeCodeMustHaveFieldNull, AutomatedCriteriaDTO> {
    private static final String ISEE = "ISEE";

    @Override
    public boolean isValid(AutomatedCriteriaDTO value, ConstraintValidatorContext context) {
        String code = value.getCode();
        String field = value.getField();
        return !ISEE.equals(code) || field == null;
    }
}
