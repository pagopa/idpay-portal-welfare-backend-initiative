package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.AutomatedCriteriaDTO;
import it.gov.pagopa.initiative.dto.FilterOperatorEnum;
import it.gov.pagopa.initiative.utils.constraint.SecondValueGreaterThanFirstWithBTW;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParsePosition;

public class SecondValueGreaterThanFirstWithBTWValidator implements ConstraintValidator<SecondValueGreaterThanFirstWithBTW, AutomatedCriteriaDTO> {

    private static final SpelExpressionParser PARSER = new SpelExpressionParser();
    private String valueField1;
    private String valueField2;
    private String operatorField3;

    @Override
    public void initialize(SecondValueGreaterThanFirstWithBTW constraintAnnotation) {
        valueField1 = constraintAnnotation.value1();
        valueField2 = constraintAnnotation.value2();
        operatorField3 = constraintAnnotation.operator();
    }

    @Override
    public boolean isValid(AutomatedCriteriaDTO automatedCriteriaDTO, ConstraintValidatorContext context) {
        String value1 = null;
        BigDecimal value1BigDecimal = null;
        Number number1 = null;
        String value2 = null;
        BigDecimal value2BigDecimal = null;
        Number number2 = null;
        FilterOperatorEnum operator = null;
        DecimalFormat decimalFormat = new DecimalFormat();
        decimalFormat.setParseBigDecimal(true);
        decimalFormat.setMinimumFractionDigits(2);

        if (PARSER.parseExpression(operatorField3).getValue(automatedCriteriaDTO) instanceof FilterOperatorEnum filterOperatorEnumInput){
            operator = filterOperatorEnumInput;
        }
        if (PARSER.parseExpression(valueField1).getValue(automatedCriteriaDTO) instanceof String stringInput) {
            value1 = stringInput;
        }
        if (PARSER.parseExpression(valueField2).getValue(automatedCriteriaDTO) instanceof String stringInput) {
            value2 = stringInput;
        }

        if (FilterOperatorEnum.BTW_CLOSED.equals(operator) || FilterOperatorEnum.BTW_OPEN.equals(operator)) {
            number1 = decimalFormat.parse(value1, new ParsePosition(0));
            if (number1 instanceof BigDecimal bigDecimalInput)
                value1BigDecimal = new BigDecimal(value1).setScale(2);
            number2 = decimalFormat.parse(value2, new ParsePosition(0));
            if (number2 instanceof BigDecimal bigDecimalInput)
                value2BigDecimal = new BigDecimal(value2).setScale(2);

            if (number1 != null && number2 != null) {
                if (value1BigDecimal != null && value2BigDecimal != null){
                    return value2BigDecimal.compareTo(value1BigDecimal) > 0;
                }
            }else
                return valueNotComparable(context);
        }
        return true;
    }

    private boolean valueNotComparable(ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();

        // Default validity is false until proven otherwise.

        context.buildConstraintViolationWithTemplate("Values not comparable for given type. Must be a Number").addConstraintViolation();
        return false;
    }

}
