package it.gov.pagopa.initiative.utils.constraint.initiative.general;

import it.gov.pagopa.initiative.utils.validator.initiative.general.TRXRuleNullIfDiscountValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {TRXRuleNullIfDiscountValidator.class})
public @interface TRXRuleNullIfDiscountConstraint {
    String message() default "Fail: when 'reward type' is DISCOUNT 'trxRule' must be null.";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
