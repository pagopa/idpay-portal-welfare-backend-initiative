package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.rule.trx.DayOfWeekDTO;
import it.gov.pagopa.initiative.utils.constraint.DayConfigIntervalsValue;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class DayConfigIntervalsValidator implements ConstraintValidator<DayConfigIntervalsValue, DayOfWeekDTO.DayConfig> {

    private static final SpelExpressionParser PARSER = new SpelExpressionParser();

    private String intervalsList;

    @Override
    public void initialize(DayConfigIntervalsValue constraintAnnotation) {
        intervalsList = constraintAnnotation.intervals();
    }

    @Override
    public boolean isValid(DayOfWeekDTO.DayConfig value, ConstraintValidatorContext context) {
        List<DayOfWeekDTO.Interval> listTmp = (List<DayOfWeekDTO.Interval>)  PARSER.parseExpression(intervalsList).getValue(value);
        if (listTmp.isEmpty()){
            return false;
        }
        for (int j = 0; j < listTmp.size()-1; j++){
            for (int i = j+1; i < listTmp.size()-1; i++){
                if (listTmp.get(j).getStartTime().equals(listTmp.get(i).getStartTime()) && listTmp.get(j).getEndTime().equals(listTmp.get(i).getEndTime())){
                    return false;
                }
            }
        }
        return true;
    }
}
