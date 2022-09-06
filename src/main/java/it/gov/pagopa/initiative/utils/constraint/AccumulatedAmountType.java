package it.gov.pagopa.initiative.utils.constraint;

import it.gov.pagopa.initiative.utils.validator.AccumulatedAmountTypeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {AccumulatedAmountTypeValidator.class})
public @interface AccumulatedAmountType {
    String value1();
    String value2();
    String message() default "Something wrong with the accumulated refund type selected";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
