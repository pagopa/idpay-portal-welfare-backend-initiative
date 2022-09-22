package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.rule.trx.DayOfWeekDTO;
import it.gov.pagopa.initiative.utils.constraint.DayOfWeekStartTimeBeforeEndTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalTime;

public class DayOfWeekStartTimeBeforeEndTimeValidator implements ConstraintValidator<DayOfWeekStartTimeBeforeEndTime, DayOfWeekDTO.Interval> {
    @Override
    public boolean isValid(DayOfWeekDTO.Interval value, ConstraintValidatorContext context) {
        LocalTime time1Tmp = value.getStartTime();
        LocalTime time2Tmp = value.getEndTime();
        if(null != time1Tmp && null != time2Tmp)
            return time1Tmp.isBefore(time2Tmp);
        return true;
    }
}
