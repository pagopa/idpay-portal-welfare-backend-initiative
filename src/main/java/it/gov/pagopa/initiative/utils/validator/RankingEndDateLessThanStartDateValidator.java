package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.InitiativeGeneralDTO;
import it.gov.pagopa.initiative.utils.constraint.RankingEndDateLessThanStartDate;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class RankingEndDateLessThanStartDateValidator implements ConstraintValidator<RankingEndDateLessThanStartDate, InitiativeGeneralDTO> {

    private static final SpelExpressionParser PARSER = new SpelExpressionParser();
    private String rankingEndDateField; //endDate
    private String startDateField; //rankingEndDate

    @Override
    public void initialize(RankingEndDateLessThanStartDate constraintAnnotation) {
        rankingEndDateField = constraintAnnotation.date1();
        startDateField = constraintAnnotation.date2();
    }

    @Override
    public boolean isValid(InitiativeGeneralDTO value, ConstraintValidatorContext context) {
        LocalDate rankingEndDate = null;
        LocalDate startDate = null;
        if (PARSER.parseExpression(rankingEndDateField).getValue(value) instanceof LocalDate)
            rankingEndDate = (LocalDate) PARSER.parseExpression(rankingEndDateField).getValue(value);
        if (PARSER.parseExpression(startDateField).getValue(value) instanceof LocalDate)
            startDate = (LocalDate) PARSER.parseExpression(startDateField).getValue(value);
        if (rankingEndDate != null && startDate != null) {
            return rankingEndDate.isBefore(startDate);
        }
        return false;
    }
}
