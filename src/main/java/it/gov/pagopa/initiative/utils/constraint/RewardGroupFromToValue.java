package it.gov.pagopa.initiative.utils.constraint;

import it.gov.pagopa.initiative.utils.validator.RewardGroupFromToValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {RewardGroupFromToValidator.class})
public @interface RewardGroupFromToValue {
    String listOfGroupValue();
    String message() default "Something wrong with the reward groups items";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
