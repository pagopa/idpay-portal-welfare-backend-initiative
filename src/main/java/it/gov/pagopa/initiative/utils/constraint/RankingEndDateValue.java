package it.gov.pagopa.initiative.utils.constraint;

import it.gov.pagopa.initiative.utils.validator.RankingEndDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {RankingEndDateValidator.class})
public @interface RankingEndDateValue {
    String value1(); //endDate
    String value2(); //rankingEndDate

    String message() default "the ranking end date cannot be after the end date of the initiative";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
