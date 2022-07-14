package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.InitiativeGeneralDTO;
import it.gov.pagopa.initiative.utils.constraint.RankingEndDateValue;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class RankingEndDateValidator implements ConstraintValidator<RankingEndDateValue, InitiativeGeneralDTO> {

    private static final SpelExpressionParser PARSER = new SpelExpressionParser();
    private String field1; //endDate
    private String field2; //rankingEndDate

    @Override
    public void initialize(RankingEndDateValue constraintAnnotation) {
        field1 = constraintAnnotation.value1();
        field2 = constraintAnnotation.value2();
    }

    @Override
    public boolean isValid(InitiativeGeneralDTO value, ConstraintValidatorContext context) {
        LocalDate d1 = (LocalDate) PARSER.parseExpression(field1).getValue(value);
        LocalDate d2 = (LocalDate) PARSER.parseExpression(field2).getValue(value);
        return d1.isAfter(d2);
    }
}
