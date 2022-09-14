package it.gov.pagopa.initiative.utils.constraint;

import it.gov.pagopa.initiative.utils.validator.SecondValueGreaterThanFirstWithBTWValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {SecondValueGreaterThanFirstWithBTWValidator.class})
public @interface SecondValueGreaterThanFirstWithBTW {
    String message() default "Fail: second value should be always greater than first.";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
