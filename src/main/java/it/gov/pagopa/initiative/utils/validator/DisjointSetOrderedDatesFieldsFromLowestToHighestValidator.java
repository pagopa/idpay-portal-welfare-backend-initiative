package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.dto.InitiativeGeneralDTO;
import it.gov.pagopa.initiative.utils.constraint.DisjointSetOrderedDatesFieldsFromLowestToHighest;
import org.springframework.cglib.core.Local;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

public class DisjointSetOrderedDatesFieldsFromLowestToHighestValidator implements ConstraintValidator<DisjointSetOrderedDatesFieldsFromLowestToHighest, InitiativeGeneralDTO> {

    private static final SpelExpressionParser PARSER = new SpelExpressionParser();
    private String[] orderedDatesFields;

    @Override
    public void initialize(DisjointSetOrderedDatesFieldsFromLowestToHighest constraintAnnotation) {
        orderedDatesFields = constraintAnnotation.orderedDates();
    }

    @Override
    public boolean isValid(InitiativeGeneralDTO value, ConstraintValidatorContext context) {
        String[] orderedDatesFieldsStringList = orderedDatesFields;
        List<LocalDate> orderedDatesList = new ArrayList<>();
        for(String dateFieldString : orderedDatesFieldsStringList){
            orderedDatesList.add((LocalDate) PARSER.parseExpression(dateFieldString).getValue(value));
        }

        for(int index = 0; index < orderedDatesList.size() - 1; index++){
            if(!orderedDatesList.get(index).isBefore(orderedDatesList.get(index+1)))
                return false;
        }
        return true;
    }
}
