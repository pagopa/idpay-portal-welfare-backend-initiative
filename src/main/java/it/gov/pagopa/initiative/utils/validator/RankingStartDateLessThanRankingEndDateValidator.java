package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.InitiativeGeneralDTO;
import it.gov.pagopa.initiative.utils.constraint.RankingStartDateLessThanRankingEndDate;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class RankingStartDateLessThanRankingEndDateValidator implements ConstraintValidator<RankingStartDateLessThanRankingEndDate, InitiativeGeneralDTO> {

    private static final SpelExpressionParser PARSER = new SpelExpressionParser();
    private String rankingStartDateField; //endDate
    private String rankingEndDateField; //rankingEndDate

    @Override
    public void initialize(RankingStartDateLessThanRankingEndDate constraintAnnotation) {
        rankingStartDateField = constraintAnnotation.date1();
        rankingEndDateField = constraintAnnotation.date2();
    }

    @Override
    public boolean isValid(InitiativeGeneralDTO value, ConstraintValidatorContext context) {
        LocalDate rankingStartDate = (LocalDate) PARSER.parseExpression(rankingStartDateField).getValue(value);
        LocalDate rankingEndDate = (LocalDate) PARSER.parseExpression(rankingEndDateField).getValue(value);
        return rankingStartDate.isBefore(rankingEndDate);
    }
}
