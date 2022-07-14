package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.InitiativeGeneralDTO;
import it.gov.pagopa.initiative.utils.constraint.BeneficiaryBudgetValue;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class BeneficiaryBudgetValidator implements ConstraintValidator<BeneficiaryBudgetValue, InitiativeGeneralDTO> {

    private static final SpelExpressionParser PARSER = new SpelExpressionParser();
    private String field1;
    private String field2;

    @Override
    public void initialize(BeneficiaryBudgetValue constraintAnnotation) {
        //field1 = BigDecimal.valueOf(Long.parseLong(constraintAnnotation.value1()));
        //field2 = BigDecimal.valueOf(Long.parseLong(constraintAnnotation.value2()));
        field1 = constraintAnnotation.value1();
        field2 = constraintAnnotation.value2();
    }

    @Override
    public boolean isValid(InitiativeGeneralDTO value, ConstraintValidatorContext context) {
        BigDecimal b1 = (BigDecimal) PARSER.parseExpression(field1).getValue(value);
        BigDecimal b2 = (BigDecimal) PARSER.parseExpression(field2).getValue(value);
        return b1.compareTo(b2) < 0;
    }

}
