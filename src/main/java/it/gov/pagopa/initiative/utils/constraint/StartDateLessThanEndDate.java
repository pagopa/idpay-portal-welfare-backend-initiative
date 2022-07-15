package it.gov.pagopa.initiative.utils.constraint;

import it.gov.pagopa.initiative.utils.validator.StartDateLessThanEndDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {StartDateLessThanEndDateValidator.class})
public @interface StartDateLessThanEndDate {
    String date1();
    String date2();
    String message() default "The ranking start date cannot be before the start date of the initiative";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
