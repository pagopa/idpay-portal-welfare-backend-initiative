package it.gov.pagopa.initiative.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class TypeMultiEnumTest {

    @Test
    void testFromValue() {
        assertNull(TypeMultiEnum.fromValue("Text"));
        assertEquals(TypeMultiEnum.MULTI, TypeMultiEnum.fromValue("multi"));
    }

    @Test
    void testValueOf() {
        assertEquals("multi", TypeMultiEnum.valueOf("MULTI").toString());
    }
}

