package it.gov.pagopa.initiative.model;

import org.junit.jupiter.api.Test;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Contract checks for the "general" section published on the initiative topic
 * and consumed by idpay-reward-calculator (InitiativeGeneralDTO.voucherDurationDays).
 */
class InitiativeGeneralSerializationTest {

    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    @Test
    void serialize_withVoucherDurationDays_exposesField() {
        InitiativeGeneral general = InitiativeGeneral.builder().budgetCents(1000L).voucherDurationDays(30).build();

        JsonNode node = jsonMapper.valueToTree(general);

        assertEquals(30, node.get("voucherDurationDays").asInt());
    }

    @Test
    void serialize_withoutVoucherDurationDays_isNull() {
        InitiativeGeneral general = InitiativeGeneral.builder().budgetCents(1000L).build();

        JsonNode node = jsonMapper.valueToTree(general);

        assertTrue(node.get("voucherDurationDays") == null || node.get("voucherDurationDays").isNull());
    }

    @Test
    void deserialize_legacyDocumentWithoutVoucherDurationDays_isNull() {
        InitiativeGeneral general = jsonMapper.readValue("{\"budgetCents\":1000}", InitiativeGeneral.class);

        assertEquals(1000L, general.getBudgetCents());
        assertNull(general.getVoucherDurationDays());
    }
}

