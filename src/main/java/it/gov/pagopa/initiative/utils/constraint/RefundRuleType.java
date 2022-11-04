package it.gov.pagopa.initiative.utils.constraint;

import it.gov.pagopa.initiative.utils.validator.RefundRuleTypeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.ErrorDtoDefaultMsg.SOMETHING_WRONG_WITH_THE_REFUND_TYPE;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {RefundRuleTypeValidator.class})
public @interface RefundRuleType {

    String message() default SOMETHING_WRONG_WITH_THE_REFUND_TYPE;
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
