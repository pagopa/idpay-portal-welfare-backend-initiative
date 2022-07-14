package it.gov.pagopa.initiative.utils.constraint;

import it.gov.pagopa.initiative.utils.validator.RankingStartDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {RankingStartDateValidator.class})
public @interface RankingStartDateValue {
    String value1(); //startDate
    String value2(); //rankingStartDate

    String message() default "The ranking start date cannot be before the start date of the initiative";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
