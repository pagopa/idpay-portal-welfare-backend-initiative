package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.AutomatedCriteriaDTO;
import it.gov.pagopa.initiative.utils.constraint.IseeHasFieldNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IseeHasFieldNullValidator implements ConstraintValidator<IseeHasFieldNull, AutomatedCriteriaDTO> {

    @Override
    public boolean isValid(AutomatedCriteriaDTO value, ConstraintValidatorContext context) {
        String code = value.getCode();
        String field = value.getField();
        if (code.equals("ISEE") && field != null){
            return false;
        }
        return true;
    }
}
