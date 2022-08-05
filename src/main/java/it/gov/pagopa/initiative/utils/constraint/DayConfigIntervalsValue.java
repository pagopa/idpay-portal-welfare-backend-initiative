package it.gov.pagopa.initiative.utils.constraint;

import it.gov.pagopa.initiative.utils.validator.DayConfigIntervalsValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {DayConfigIntervalsValidator.class})
public @interface DayConfigIntervalsValue {
    String intervals();
    String message() default "Something wrong with intervals items ";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
