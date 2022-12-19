package it.gov.pagopa.initiative.model.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ConfigMccTest {
    final ConfigMcc actualConfigMcc = new ConfigMcc("Code", "The characteristics of someone or something");

    @Test
    void testCanEqual() {
        assertFalse((new ConfigMcc()).canEqual("Other"));
    }

    @Test
    void testCanEqual2() {
        assertTrue(actualConfigMcc.canEqual(actualConfigMcc));
    }

    @Test
    void testConstructor() {
        String actualToStringResult = actualConfigMcc.toString();
        assertEquals("Code", actualConfigMcc.getCode());
        assertEquals("The characteristics of someone or something", actualConfigMcc.getDescription());
        assertEquals("ConfigMcc(code=Code, description=The characteristics of someone or something)", actualToStringResult);
    }

    @Test
    void testConstructor2() {
        String actualToStringResult = actualConfigMcc.toString();
        assertEquals("Code", actualConfigMcc.getCode());
        assertEquals("The characteristics of someone or something", actualConfigMcc.getDescription());
        assertEquals("ConfigMcc(code=Code, description=The characteristics of someone or something)",
                actualToStringResult);
    }

    @Test
    void testEquals() {
        assertNotEquals(null, actualConfigMcc);
    }

    @Test
    void testEquals1() {
        assertEquals(actualConfigMcc, actualConfigMcc);
        int expectedHashCodeResult = actualConfigMcc.hashCode();
        assertEquals(expectedHashCodeResult, actualConfigMcc.hashCode());
    }

    @Test
    void testEquals2() {
        ConfigMcc configMcc1 = new ConfigMcc();
        configMcc1.setCode("Code");
        configMcc1.setDescription("The characteristics of someone or something");
        assertEquals(actualConfigMcc, configMcc1);
        int expectedHashCodeResult = actualConfigMcc.hashCode();
        assertEquals(expectedHashCodeResult, configMcc1.hashCode());
    }
}

