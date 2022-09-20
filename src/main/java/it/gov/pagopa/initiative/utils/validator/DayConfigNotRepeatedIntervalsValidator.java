package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.rule.trx.DayOfWeekDTO;
import it.gov.pagopa.initiative.utils.constraint.DayConfigNotRepeatedIntervalsConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalTime;
import java.util.List;

public class DayConfigNotRepeatedIntervalsValidator implements ConstraintValidator<DayConfigNotRepeatedIntervalsConstraint, DayOfWeekDTO.DayConfig> {
    @Override
    public boolean isValid(DayOfWeekDTO.DayConfig value, ConstraintValidatorContext context) {
        List<DayOfWeekDTO.Interval> listTmp = value.getIntervals();
        if (listTmp.isEmpty()){
            return false;
        }
        for (int j = 0; j < listTmp.size()-1; j++){
            for (int i = j+1; i <= listTmp.size()-1; i++){
                LocalTime startTimeJ = listTmp.get(j).getStartTime();
                LocalTime endTimeJ = listTmp.get(j).getEndTime();
                LocalTime startTimeI = listTmp.get(i).getStartTime();
                LocalTime endTimeI = listTmp.get(i).getEndTime();
                if(null != startTimeJ && null != endTimeJ && null != startTimeI && null != endTimeI) {
                    if (startTimeJ.equals(startTimeI) && endTimeJ.equals(endTimeI)) {
                        return false;
                    }
                }
                else
                    return true;
            }
        }
        return true;
    }
}
