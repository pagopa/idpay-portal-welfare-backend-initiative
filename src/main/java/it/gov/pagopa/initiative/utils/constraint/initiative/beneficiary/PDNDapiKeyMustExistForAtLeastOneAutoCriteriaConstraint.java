package it.gov.pagopa.initiative.utils.constraint.initiative.beneficiary;

import it.gov.pagopa.initiative.utils.validator.initiative.beneficiary.PDNDapiKeyMustExistForAtLeastOneAutoCriteriaValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {PDNDapiKeyMustExistForAtLeastOneAutoCriteriaValidator.class})
public @interface PDNDapiKeyMustExistForAtLeastOneAutoCriteriaConstraint {

    String MESSAGE = "apiKeyClientID or apiKeiClientAssertion must exist together only when at least one AutomatedCriteria has been inserted.";

    String message() default MESSAGE;
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
