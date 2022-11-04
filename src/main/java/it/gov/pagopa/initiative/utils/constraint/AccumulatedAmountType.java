package it.gov.pagopa.initiative.utils.constraint;

import it.gov.pagopa.initiative.utils.validator.AccumulatedAmountTypeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static it.gov.pagopa.initiative.constants.InitiativeConstants.Exception.ErrorDtoDefaultMsg.ACCUMULATED_AMOUNT_TYPE;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {AccumulatedAmountTypeValidator.class})
public @interface AccumulatedAmountType {
    String value1();
    String value2();
    String message() default ACCUMULATED_AMOUNT_TYPE;
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
