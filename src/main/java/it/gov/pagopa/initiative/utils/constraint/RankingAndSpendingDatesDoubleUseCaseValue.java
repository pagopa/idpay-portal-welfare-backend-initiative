package it.gov.pagopa.initiative.utils.constraint;

import it.gov.pagopa.initiative.utils.validator.RankingAndSpendingDatesDoubleUseCaseValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {RankingAndSpendingDatesDoubleUseCaseValidator.class})
public @interface RankingAndSpendingDatesDoubleUseCaseValue {
    String date1();
    String date2();
    String date3();
    String date4();
    String message() default "Dates inserted do not meet the business case requirements";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
