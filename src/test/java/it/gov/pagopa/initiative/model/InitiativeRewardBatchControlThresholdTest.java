package it.gov.pagopa.initiative.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InitiativeRewardBatchControlThresholdTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void nullThresholdIdentifiesAnInitiativeWithoutRewardBatches() {
        Initiative initiative = Initiative.builder().rewardBatchControlThreshold(null).build();

        assertTrue(validator.validate(initiative).isEmpty());
    }

    @Test
    void thresholdMustBeBetweenOneAndOneHundredWhenConfigured() {
        Initiative initiative = Initiative.builder().rewardBatchControlThreshold(0).build();
        assertEquals(1, validator.validate(initiative).size());

        initiative.setRewardBatchControlThreshold(101);
        assertEquals(1, validator.validate(initiative).size());

        initiative.setRewardBatchControlThreshold(50);
        assertTrue(validator.validate(initiative).isEmpty());
    }
}
