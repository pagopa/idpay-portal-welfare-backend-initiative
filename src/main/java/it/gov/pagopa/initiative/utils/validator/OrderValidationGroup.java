package it.gov.pagopa.initiative.utils.validator;

import jakarta.validation.GroupSequence;

@GroupSequence({ ValidationTRXGroup.class, ValidationApiEnabledGroup.class })
public interface OrderValidationGroup {
}
