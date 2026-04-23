package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.InitiativeGeneralDTO;
import it.gov.pagopa.initiative.utils.constraint.BeneficiaryBudgetValue;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
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
        BigDecimal beneficiaryBudgetFixed = null;
        BigDecimal budget = null;
        if (PARSER.parseExpression(budgetField1).getValue(value) instanceof BigDecimal bigDecimalInput) {
            beneficiaryBudgetFixed= bigDecimalInput;
        }
        if (PARSER.parseExpression(budgetField2).getValue(value) instanceof BigDecimal bigDecimalInput) {
            budget = bigDecimalInput;
        }
        if (beneficiaryBudgetFixed == null) {
            return true;
        }
        if (budget != null) {
            return beneficiaryBudgetFixed.compareTo(budget) < 0;
        }
        return false;
    }
}
