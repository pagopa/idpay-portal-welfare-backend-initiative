package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.rule.refund.AccumulatedAmountDTO;
import it.gov.pagopa.initiative.utils.constraint.AccumulatedAmountType;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class AccumulatedAmountTypeValidator implements ConstraintValidator<AccumulatedAmountType, AccumulatedAmountDTO> {
    private static final SpelExpressionParser PARSER = new SpelExpressionParser();

    private String accumulatedType;

    private String refundThreshold;

    @Override
    public void initialize(AccumulatedAmountType constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        accumulatedType = constraintAnnotation.value1();
        refundThreshold = constraintAnnotation.value2();
    }

    @Override
    public boolean isValid(AccumulatedAmountDTO value, ConstraintValidatorContext context) {
        AccumulatedAmountDTO.AccumulatedTypeEnum accumulatedType1 = null;
        Long refundThreshold1 = null;
        if (PARSER.parseExpression(accumulatedType).getValue(value) instanceof AccumulatedAmountDTO.AccumulatedTypeEnum accumulatedTypeEnum)
            accumulatedType1 = accumulatedTypeEnum;
        if (PARSER.parseExpression(refundThreshold).getValue(value) instanceof Long bigDecimalInput){
            refundThreshold1 = bigDecimalInput;
        }
        if (accumulatedType1 == AccumulatedAmountDTO.AccumulatedTypeEnum.THRESHOLD_REACHED && refundThreshold1 == null){
            return false;
        }
        return accumulatedType1 != AccumulatedAmountDTO.AccumulatedTypeEnum.BUDGET_EXHAUSTED || refundThreshold1 == null;
    }
}
