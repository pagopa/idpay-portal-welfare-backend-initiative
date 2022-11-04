package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.InitiativeGeneralDTO;
import it.gov.pagopa.initiative.utils.constraint.BeneficiaryBudgetValue;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigDecimal;

public class BeneficiaryBudgetValidator implements ConstraintValidator<BeneficiaryBudgetValue, InitiativeGeneralDTO> {

    private static final SpelExpressionParser PARSER = new SpelExpressionParser();
    private String budgetField1;
    private String budgetField2;

    @Override
    public void initialize(BeneficiaryBudgetValue constraintAnnotation) {
        budgetField1 = constraintAnnotation.budget1();
        budgetField2 = constraintAnnotation.budget2();
    }

    @Override
    public boolean isValid(InitiativeGeneralDTO value, ConstraintValidatorContext context) {
        BigDecimal budget1 = null;
        BigDecimal budget2 = null;
        if (PARSER.parseExpression(budgetField1).getValue(value) instanceof BigDecimal bigDecimalInput)
            budget1 = bigDecimalInput;
        if (PARSER.parseExpression(budgetField2).getValue(value) instanceof BigDecimal bigDecimalInput){
            budget2 = bigDecimalInput;
        }
        if(budget1 != null && budget2 != null)
            return budget1.compareTo(budget2) < 0;
        return false;
    }

}
