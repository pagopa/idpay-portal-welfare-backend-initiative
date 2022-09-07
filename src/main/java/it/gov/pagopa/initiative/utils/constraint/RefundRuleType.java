package it.gov.pagopa.initiative.utils.constraint;

import it.gov.pagopa.initiative.utils.validator.RefundRuleTypeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {RefundRuleTypeValidator.class})
public @interface RefundRuleType {
    String message() default "Something wrong with the refund type";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
