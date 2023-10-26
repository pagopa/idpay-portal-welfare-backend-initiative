package it.gov.pagopa.initiative.utils.validator.initiative.beneficiary;

import it.gov.pagopa.initiative.dto.AutomatedCriteriaDTO;
import it.gov.pagopa.initiative.utils.constraint.initiative.beneficiary.IseeCodeMustHaveValueNumericConstraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.math.NumberUtils;

public class IseeCodeMustHaveValueNumericValidator implements ConstraintValidator<IseeCodeMustHaveValueNumericConstraint, AutomatedCriteriaDTO> {
    private static final String ISEE = "ISEE";

    @Override
    public boolean isValid(AutomatedCriteriaDTO value, ConstraintValidatorContext context) {
        String code = value.getCode();
        String inputValue = value.getValue();
        if(ISEE.equals(code)) {
            return NumberUtils.isCreatable(inputValue);
        }

        return true;
    }
}
