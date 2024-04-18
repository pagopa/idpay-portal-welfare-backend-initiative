package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.rule.trx.ThresholdDTO;
import it.gov.pagopa.initiative.utils.constraint.ThresholdFromToValue;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class ThresholdFromToValidator implements ConstraintValidator<ThresholdFromToValue, ThresholdDTO> {
    @Override
    public boolean isValid(ThresholdDTO value, ConstraintValidatorContext context) {
        Long fromTmp = value.getFromCents();
        Long toTmp = value.getToCents();
        return fromTmp < toTmp;
    }
}
