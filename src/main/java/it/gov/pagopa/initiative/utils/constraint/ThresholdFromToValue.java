package it.gov.pagopa.initiative.utils.constraint;

import it.gov.pagopa.initiative.utils.validator.ThresholdFromToValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {ThresholdFromToValidator.class})
public @interface ThresholdFromToValue {
    String message() default "'from' must be before 'to'";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
