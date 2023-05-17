package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.rule.trx.TrxCountDTO;
import it.gov.pagopa.initiative.utils.constraint.TrxCountFromToValue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class TrxCountFromToValidator implements ConstraintValidator<TrxCountFromToValue, TrxCountDTO> {
    @Override
    public boolean isValid(TrxCountDTO value, ConstraintValidatorContext context) {
        Long fromTmp = value.getFrom();
        Long toTmp = value.getTo();
        return (fromTmp == null || toTmp == null || fromTmp.compareTo(toTmp) < 0);
    }
}
