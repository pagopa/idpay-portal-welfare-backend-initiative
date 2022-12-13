package it.gov.pagopa.initiative.model.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ConfigTrxRuleTest {

    @Test
    void testCanEqual() {
        assertFalse((new ConfigTrxRule()).canEqual("Other"));
    }

    @Test
    void testCanEqual2() {
        ConfigTrxRule configTrxRule = new ConfigTrxRule();

        ConfigTrxRule configTrxRule1 = new ConfigTrxRule();
        configTrxRule1.setChecked(true);
        configTrxRule1.setCode("Code");
        configTrxRule1.setDescription("The characteristics of someone or something");
        configTrxRule1.setEnabled(true);
        assertTrue(configTrxRule.canEqual(configTrxRule1));
    }

    @Test
    void testConstructor() {
        ConfigTrxRule actualConfigTrxRule = new ConfigTrxRule();
        actualConfigTrxRule.setChecked(true);
        actualConfigTrxRule.setCode("Code");
        actualConfigTrxRule.setDescription("The characteristics of someone or something");
        actualConfigTrxRule.setEnabled(true);
        String actualToStringResult = actualConfigTrxRule.toString();
        assertTrue(actualConfigTrxRule.getChecked());
        assertEquals("Code", actualConfigTrxRule.getCode());
        assertEquals("The characteristics of someone or something", actualConfigTrxRule.getDescription());
        assertTrue(actualConfigTrxRule.getEnabled());
        assertEquals("ConfigTrxRule(code=Code, description=The characteristics of someone or something, enabled=true,"
                + " checked=true)", actualToStringResult);
    }

    @Test
    void testEquals() {
        ConfigTrxRule configTrxRule = new ConfigTrxRule();
        configTrxRule.setChecked(true);
        configTrxRule.setCode("Code");
        configTrxRule.setDescription("The characteristics of someone or something");
        configTrxRule.setEnabled(true);
        assertNotEquals(null, configTrxRule);
    }

    @Test
    void testEquals1() {
        ConfigTrxRule configTrxRule = new ConfigTrxRule();
        configTrxRule.setChecked(true);
        configTrxRule.setCode("Code");
        configTrxRule.setDescription("The characteristics of someone or something");
        configTrxRule.setEnabled(true);

        ConfigTrxRule configTrxRule1 = new ConfigTrxRule();
        configTrxRule1.setChecked(true);
        configTrxRule1.setCode("Code");
        configTrxRule1.setDescription("The characteristics of someone or something");
        configTrxRule1.setEnabled(true);
        assertEquals(configTrxRule, configTrxRule1);
        int expectedHashCodeResult = configTrxRule.hashCode();
        assertEquals(expectedHashCodeResult, configTrxRule1.hashCode());
    }

}

