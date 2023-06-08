package it.gov.pagopa.initiative.utils.constraint;

import it.gov.pagopa.initiative.utils.validator.DayConfigNotRepeatedIntervalsValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {DayConfigNotRepeatedIntervalsValidator.class})
public @interface DayConfigNotRepeatedIntervalsConstraint {
    String message() default "Something wrong with intervals items ";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
