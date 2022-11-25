package it.gov.pagopa.initiative.utils.constraint.initiative.general;

import it.gov.pagopa.initiative.utils.validator.initiative.general.RankingGracePeriodValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {RankingGracePeriodValidator.class})
public @interface RankingGracePeriodConstraint {

    String FAIL_WHEN_RANKING_ENABLED_THE_START_DATE_MUST_BE_10_DAYS_GREATER_THAN_THE_RANKING_END_DATE = "Fail: When ranking Enabled, the startDate must be 10 days greater than the rankingEndDate";

    String message() default FAIL_WHEN_RANKING_ENABLED_THE_START_DATE_MUST_BE_10_DAYS_GREATER_THAN_THE_RANKING_END_DATE;
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
