package it.gov.pagopa.initiative.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class FilterOperatorEnumModelTest {

    @Test
    void testFromValue() {
        assertNull(FilterOperatorEnumModel.fromValue("Text"));
        assertEquals(FilterOperatorEnumModel.EQ, FilterOperatorEnumModel.fromValue("EQ"));
        assertEquals(FilterOperatorEnumModel.NOT_EQ, FilterOperatorEnumModel.fromValue("NOT_EQ"));
        assertEquals(FilterOperatorEnumModel.LT, FilterOperatorEnumModel.fromValue("LT"));
        assertEquals(FilterOperatorEnumModel.LE, FilterOperatorEnumModel.fromValue("LE"));
        assertEquals(FilterOperatorEnumModel.GT, FilterOperatorEnumModel.fromValue("GT"));
        assertEquals(FilterOperatorEnumModel.GE, FilterOperatorEnumModel.fromValue("GE"));
        assertEquals(FilterOperatorEnumModel.BTW_CLOSED, FilterOperatorEnumModel.fromValue("BTW_CLOSED"));
        assertEquals(FilterOperatorEnumModel.INSTANCE_OF, FilterOperatorEnumModel.fromValue("INSTANCE_OF"));
        assertEquals(FilterOperatorEnumModel.BTW_OPEN, FilterOperatorEnumModel.fromValue("BTW_OPEN"));
    }

    @Test
    void testValueOf() {
        assertEquals("EQ", FilterOperatorEnumModel.valueOf("EQ").toString());
    }
}

