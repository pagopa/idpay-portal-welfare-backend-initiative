package it.gov.pagopa.initiative.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class TypeBoolEnumTest {

    @Test
    void testFromValue() {
        assertNull(TypeBoolEnum.fromValue("Text"));
        assertEquals(TypeBoolEnum.BOOLEAN, TypeBoolEnum.fromValue("boolean"));
    }

    @Test
    void testValueOf() {
        assertEquals("boolean", TypeBoolEnum.valueOf("BOOLEAN").toString());
    }
}

