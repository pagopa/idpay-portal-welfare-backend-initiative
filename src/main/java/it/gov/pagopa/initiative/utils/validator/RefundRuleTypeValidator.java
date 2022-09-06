package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.rule.refund.AccumulatedAmountDTO;
import it.gov.pagopa.initiative.dto.rule.refund.InitiativeRefundRuleDTO;
import it.gov.pagopa.initiative.dto.rule.refund.TimeParameterDTO;
import it.gov.pagopa.initiative.utils.constraint.RefundRuleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RefundRuleTypeValidator implements ConstraintValidator<RefundRuleType, InitiativeRefundRuleDTO> {
//    private static final SpelExpressionParser PARSER = new SpelExpressionParser();
//
//    private String accomulatedAmount;
//    private String timeParameter;

    @Override
    public void initialize(RefundRuleType constraintAnnotation) {
//        ConstraintValidator.super.initialize(constraintAnnotation);
//        accomulatedAmount = constraintAnnotation.value1();
//        timeParameter = constraintAnnotation.value2();
    }

    @Override
    public boolean isValid(InitiativeRefundRuleDTO value, ConstraintValidatorContext context) {
//        AccumulatedAmountDTO amountDTO = null;
//        TimeParameterDTO timeParameterDTO = null;
//        if ((PARSER.parseExpression(accomulatedAmount).getValue(value)) instanceof AccumulatedAmountDTO accumulatedAmountDTO){
//            amountDTO = accumulatedAmountDTO;
//        }
//        if ((PARSER.parseExpression(timeParameter).getValue(value)) instanceof TimeParameterDTO parameterDTO){
//            timeParameterDTO = parameterDTO;
//        }
////        amountDTO = (AccumulatedAmountDTO) PARSER.parseExpression(accomulatedAmount).getValue(value);
////        timeParameterDTO = (TimeParameterDTO) PARSER.parseExpression(timeParameter).getValue(value);
//
//        if ((amountDTO == null && timeParameterDTO != null) || (amountDTO != null && timeParameterDTO == null)){
//            return true;
//        }
//        return false;
        AccumulatedAmountDTO amountDTO = value.getAccumulatedAmount();
        TimeParameterDTO timeParameterDTO = value.getTimeParameter();
        if ((amountDTO == null && timeParameterDTO != null) || (amountDTO != null && timeParameterDTO == null)){
            return true;
        }
        return false;
    }
}
