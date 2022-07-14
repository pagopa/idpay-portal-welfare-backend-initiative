package it.gov.pagopa.initiative.utils.constraint;

import it.gov.pagopa.initiative.utils.validator.BeneficiaryBudgetValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {BeneficiaryBudgetValidator.class})
public @interface BeneficiaryBudgetValue {

    String value1();
    String value2();

    String message() default "beneficiaryBudget cannot be greater than budget, or equal}";
    Class<?>[] groups() default { };
    Class<? extends Payload>[] payload() default { };
}
