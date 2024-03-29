package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.rule.trx.ThresholdDTO;
import it.gov.pagopa.initiative.utils.constraint.ThresholdFromToValue;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class ThresholdFromToValidator implements ConstraintValidator<ThresholdFromToValue, ThresholdDTO> {
    @Override
    public boolean isValid(ThresholdDTO value, ConstraintValidatorContext context) {
        BigDecimal fromTmp = value.getFrom();
        BigDecimal toTmp = value.getTo();
        return fromTmp.compareTo(toTmp) < 0;
    }
}
