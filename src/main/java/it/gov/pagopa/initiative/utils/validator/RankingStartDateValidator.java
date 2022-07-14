package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.InitiativeGeneralDTO;
import it.gov.pagopa.initiative.utils.constraint.RankingEndDateValue;
import it.gov.pagopa.initiative.utils.constraint.RankingStartDateValue;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class RankingStartDateValidator implements ConstraintValidator<RankingStartDateValue, InitiativeGeneralDTO> {
    private static final SpelExpressionParser PARSER = new SpelExpressionParser();
    private String field1; //startDate
    private String field2; //rankingStartDate


    @Override
    public void initialize(RankingStartDateValue constraintAnnotation) {
        field1 = constraintAnnotation.value1();
        field2 = constraintAnnotation.value2();
    }

    @Override
    public boolean isValid(InitiativeGeneralDTO value, ConstraintValidatorContext context) {
        LocalDate d1 = (LocalDate) PARSER.parseExpression(field1).getValue(value); //startDate
        LocalDate d2 = (LocalDate) PARSER.parseExpression(field2).getValue(value); //rankingStartDate

        return d1.isBefore(d2);
    }
}
