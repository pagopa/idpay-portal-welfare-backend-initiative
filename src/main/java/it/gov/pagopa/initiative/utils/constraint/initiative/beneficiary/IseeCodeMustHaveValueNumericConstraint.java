package it.gov.pagopa.initiative.utils.constraint.initiative.beneficiary;

import it.gov.pagopa.initiative.utils.validator.initiative.beneficiary.IseeCodeMustHaveValueNumericValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {IseeCodeMustHaveValueNumericValidator.class})
public @interface IseeCodeMustHaveValueNumericConstraint {
    String message() default "Fail: when 'code' is ISEE 'value' must be numeric.";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
