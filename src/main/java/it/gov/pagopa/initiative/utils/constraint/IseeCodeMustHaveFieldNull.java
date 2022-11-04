package it.gov.pagopa.initiative.utils.constraint;

import it.gov.pagopa.initiative.utils.validator.IseeCodeMustHaveFieldNullValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {IseeCodeMustHaveFieldNullValidator.class})
public @interface IseeCodeMustHaveFieldNull {
    String message() default "Fail: when 'code' is ISEE 'field' must be null.";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
