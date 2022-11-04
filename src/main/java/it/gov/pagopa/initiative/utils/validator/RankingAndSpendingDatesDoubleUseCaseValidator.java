package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.InitiativeGeneralDTO;
import it.gov.pagopa.initiative.utils.constraint.RankingAndSpendingDatesDoubleUseCaseValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

/**
 * Return true only in cases where:
 *         - all dates are present and are also compatible with each other.
 *         - there are no ranking dates but the start and end dates are present and compatible with each other.
 * for all other cases, false.
 */
@Slf4j
public class RankingAndSpendingDatesDoubleUseCaseValidator implements ConstraintValidator<RankingAndSpendingDatesDoubleUseCaseValue, InitiativeGeneralDTO> {

    private static final SpelExpressionParser PARSER = new SpelExpressionParser();
    private String rankingStart;
    private String rankingEnd;
    private String start;
    private String end;
    @Override
    public void initialize(RankingAndSpendingDatesDoubleUseCaseValue constraintAnnotation) {
        rankingStart = constraintAnnotation.date1();
        rankingEnd = constraintAnnotation.date2();
        start = constraintAnnotation.date3();
        end = constraintAnnotation.date4();
    }

    @Override
    public boolean isValid(InitiativeGeneralDTO value, ConstraintValidatorContext context) {
        LocalDate rankingStartDate = null;
        LocalDate rankingEndDate = null;
        LocalDate startDate = null;
        LocalDate endDate = null;
        if (PARSER.parseExpression(rankingStart).getValue(value) instanceof LocalDate localeDateInput){
            rankingStartDate = localeDateInput;
        }
        if (PARSER.parseExpression(rankingEnd).getValue(value) instanceof LocalDate localeDateInput){
            rankingEndDate = localeDateInput;
        }
        if (PARSER.parseExpression(start).getValue(value) instanceof LocalDate localeDateInput){
            startDate = localeDateInput;
        }
        if (PARSER.parseExpression(end).getValue(value) instanceof LocalDate localeDateInput){
            endDate = localeDateInput;
        }


        if (startDate != null && endDate != null){//if both start and end buy dates are not present, false.
            log.debug("start and end date not null");
            if (rankingStartDate != null){//if dates are all present, they are checked.
                if (rankingEndDate != null){
                    return rankingStartDate.isBefore(rankingEndDate) && rankingEndDate.isBefore(startDate) && startDate.isBefore(endDate);
                }
            } else { //if we have only start and end buy dates, those are checked.
                if (rankingEndDate == null){
                    return startDate.isBefore(endDate);
                }
            }
        }
        return false;

    }
}
