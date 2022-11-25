package it.gov.pagopa.initiative.model.rule.refund;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class TimeParameterTest {

    @Test
    void testTimeTypeEnumFromValue() {
        assertNull(TimeParameter.TimeTypeEnum.fromValue("Text"));
        assertEquals(TimeParameter.TimeTypeEnum.CLOSED, TimeParameter.TimeTypeEnum.fromValue("CLOSED"));
        assertEquals(TimeParameter.TimeTypeEnum.DAILY, TimeParameter.TimeTypeEnum.fromValue("DAILY"));
        assertEquals(TimeParameter.TimeTypeEnum.WEEKLY, TimeParameter.TimeTypeEnum.fromValue("WEEKLY"));
        assertEquals(TimeParameter.TimeTypeEnum.MONTHLY, TimeParameter.TimeTypeEnum.fromValue("MONTHLY"));
        assertEquals(TimeParameter.TimeTypeEnum.QUARTERLY, TimeParameter.TimeTypeEnum.fromValue("QUARTERLY"));
    }

    @Test
    void testTimeTypeEnumValueOf() {
        assertEquals("CLOSED", TimeParameter.TimeTypeEnum.valueOf("CLOSED").toString());
    }
}

