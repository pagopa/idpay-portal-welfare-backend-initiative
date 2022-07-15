package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.InitiativeGeneralDTO;
import it.gov.pagopa.initiative.utils.constraint.StartDateLessThanEndDate;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class StartDateLessThanEndDateValidator implements ConstraintValidator<StartDateLessThanEndDate, InitiativeGeneralDTO> {
    private static final SpelExpressionParser PARSER = new SpelExpressionParser();
    private String startDateField; //startDate
    private String endDateField; //rankingStartDate


    @Override
    public void initialize(StartDateLessThanEndDate constraintAnnotation) {
        startDateField = constraintAnnotation.date1();
        endDateField = constraintAnnotation.date2();
    }

    @Override
    public boolean isValid(InitiativeGeneralDTO value, ConstraintValidatorContext context) {
        LocalDate startDate = null;
        LocalDate endDate = null;
        if(PARSER.parseExpression(startDateField).getValue(value) instanceof LocalDate)
            startDate = (LocalDate) PARSER.parseExpression(startDateField).getValue(value);
        if(PARSER.parseExpression(this.endDateField).getValue(value) instanceof LocalDate)
            endDate = (LocalDate) PARSER.parseExpression(this.endDateField).getValue(value);
        if (startDate != null && endDate != null) {
            return startDate.isBefore(endDate);
        }
        return false;
    }
}
