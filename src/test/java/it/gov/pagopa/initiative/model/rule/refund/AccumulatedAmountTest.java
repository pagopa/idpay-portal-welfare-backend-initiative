package it.gov.pagopa.initiative.model.rule.refund;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class AccumulatedAmountTest {

    @Test
    void testAccumulatedTypeEnumFromValue() {
        assertNull(AccumulatedAmount.AccumulatedTypeEnum.fromValue("Text"));
        assertEquals(AccumulatedAmount.AccumulatedTypeEnum.BUDGET_EXHAUSTED,
                AccumulatedAmount.AccumulatedTypeEnum.fromValue("BUDGET_EXHAUSTED"));
        assertEquals(AccumulatedAmount.AccumulatedTypeEnum.THRESHOLD_REACHED,
                AccumulatedAmount.AccumulatedTypeEnum.fromValue("THRESHOLD_REACHED"));
    }

    @Test
    void testAccumulatedTypeEnumValueOf() {
        assertEquals("BUDGET_EXHAUSTED", AccumulatedAmount.AccumulatedTypeEnum.valueOf("BUDGET_EXHAUSTED").toString());
    }
}

