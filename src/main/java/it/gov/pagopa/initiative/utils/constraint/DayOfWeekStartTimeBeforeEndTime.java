package it.gov.pagopa.initiative.utils.constraint;


import it.gov.pagopa.initiative.utils.validator.DayOfWeekStartTimeBeforeEndTimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {DayOfWeekStartTimeBeforeEndTimeValidator.class})
public @interface DayOfWeekStartTimeBeforeEndTime {
    String message() default "Start time must be before end time";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
