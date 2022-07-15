package it.gov.pagopa.initiative.utils.constraint;

import it.gov.pagopa.initiative.utils.validator.DisjointSetOrderedDatesFieldsFromLowestToHighestValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {DisjointSetOrderedDatesFieldsFromLowestToHighestValidator.class})
public @interface DisjointSetOrderedDatesFieldsFromLowestToHighest {
    String[] orderedDates();
    String message() default "There must be an ordering of dates starting from Ranking/Adhesion Date to the end of period for spending funds";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
