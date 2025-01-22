package it.gov.pagopa.initiative.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TypeTextEnumTest {

    @Test
    void testFromValue() {
        assertNull(TypeTextEnum.fromValue("Text"));
        assertEquals(TypeTextEnum.TEXT, TypeTextEnum.fromValue("text"));
    }

    @Test
    void testValueOf() {
        assertEquals("text", TypeTextEnum.valueOf("TEXT").toString());
    }

}