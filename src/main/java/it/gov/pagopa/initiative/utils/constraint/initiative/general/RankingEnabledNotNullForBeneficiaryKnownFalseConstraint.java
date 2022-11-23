package it.gov.pagopa.initiative.utils.constraint.initiative.general;

import it.gov.pagopa.initiative.utils.validator.initiative.general.RankingEnabledNotNullForBeneficiaryKnownFalseValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {RankingEnabledNotNullForBeneficiaryKnownFalseValidator.class})
public @interface RankingEnabledNotNullForBeneficiaryKnownFalseConstraint {

    String RANKING_ENABLED_NOT_NULL_FOR_BENEFICIARY_KNOWN_FALSE_CONSTRAINT = "Fail: When 'beneficiaryKnow' is [false], then rankingEnabled must be SET to [true] or [false]";

    String message() default RANKING_ENABLED_NOT_NULL_FOR_BENEFICIARY_KNOWN_FALSE_CONSTRAINT;
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
