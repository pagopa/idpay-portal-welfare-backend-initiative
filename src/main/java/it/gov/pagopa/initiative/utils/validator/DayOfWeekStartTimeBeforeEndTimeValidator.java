package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.rule.trx.DayOfWeekDTO;
import it.gov.pagopa.initiative.utils.constraint.DayOfWeekStartTimeBeforeEndTime;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalTime;

public class DayOfWeekStartTimeBeforeEndTimeValidator implements ConstraintValidator<DayOfWeekStartTimeBeforeEndTime, DayOfWeekDTO.Interval> {
    private static final SpelExpressionParser PARSER = new SpelExpressionParser();

    private String time_1;
    private String time_2;

    @Override
    public void initialize(DayOfWeekStartTimeBeforeEndTime constraintAnnotation) {
        time_1 = constraintAnnotation.time1();
        time_2 = constraintAnnotation.time2();
    }

    @Override
    public boolean isValid(DayOfWeekDTO.Interval value, ConstraintValidatorContext context) {
        LocalTime time1Tmp = (LocalTime)  PARSER.parseExpression(time_1).getValue(value);
        LocalTime time2Tmp = (LocalTime)  PARSER.parseExpression(time_2).getValue(value);
        return time1Tmp.isBefore(time2Tmp);
    }
}
