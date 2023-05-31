package it.gov.pagopa.initiative.utils.constraint.initiative.beneficiary;

import it.gov.pagopa.initiative.utils.validator.initiative.beneficiary.IseeCodeMustHaveFieldNullValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {IseeCodeMustHaveFieldNullValidator.class})
public @interface IseeCodeMustHaveFieldNullConstraint {
    String message() default "Fail: when 'code' is ISEE 'field' must be null.";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
