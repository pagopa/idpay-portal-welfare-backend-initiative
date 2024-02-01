package it.gov.pagopa.initiative.utils.validator;

import it.gov.pagopa.initiative.utils.validator.initiative.ValidatedURL;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {ValidatedURL.class})
public @interface URLValidation {
    String message() default "[contact]: must be valid";

    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
