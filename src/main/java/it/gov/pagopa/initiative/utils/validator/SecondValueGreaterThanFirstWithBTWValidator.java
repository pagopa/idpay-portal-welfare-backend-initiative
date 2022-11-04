package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.AutomatedCriteriaDTO;
import it.gov.pagopa.initiative.dto.FilterOperatorEnum;
import it.gov.pagopa.initiative.utils.constraint.SecondValueGreaterThanFirstWithBTW;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class SecondValueGreaterThanFirstWithBTWValidator implements ConstraintValidator<SecondValueGreaterThanFirstWithBTW, AutomatedCriteriaDTO> {

    @Override
    public boolean isValid(AutomatedCriteriaDTO automatedCriteriaDTO, ConstraintValidatorContext context) {
        String value1 = automatedCriteriaDTO.getValue();
        BigDecimal value1BigDecimal;
        String value2 = automatedCriteriaDTO.getValue2();
        BigDecimal value2BigDecimal;
        FilterOperatorEnum operator = automatedCriteriaDTO.getOperator();

        if (FilterOperatorEnum.BTW_CLOSED.equals(operator) || FilterOperatorEnum.BTW_OPEN.equals(operator)) {
            if(null != value1 && null != value2) {
                value1BigDecimal = new BigDecimal(value1).setScale(2, RoundingMode.DOWN);
                value2BigDecimal = new BigDecimal(value2).setScale(2, RoundingMode.DOWN);
                return value2BigDecimal.compareTo(value1BigDecimal) > 0;
            }
            else {
                return valueNotComparable(context);
            }
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
