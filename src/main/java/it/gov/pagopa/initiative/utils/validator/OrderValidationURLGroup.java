package it.gov.pagopa.initiative.utils.validator;

import jakarta.validation.GroupSequence;

@GroupSequence({ URLValidationGroup.class, ValidationApiEnabledGroup.class })
public interface OrderValidationURLGroup {
}
