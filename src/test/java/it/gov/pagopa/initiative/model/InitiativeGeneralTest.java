package it.gov.pagopa.initiative.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class InitiativeGeneralTest {

    @Test
    void testBeneficiaryTypeEnumFromValue() {
        assertNull(InitiativeGeneral.BeneficiaryTypeEnum.fromValue("Text"));
        assertEquals(InitiativeGeneral.BeneficiaryTypeEnum.PF, InitiativeGeneral.BeneficiaryTypeEnum.fromValue("PF"));
        assertEquals(InitiativeGeneral.BeneficiaryTypeEnum.PG, InitiativeGeneral.BeneficiaryTypeEnum.fromValue("PG"));
    }

    @Test
    void testBeneficiaryTypeEnumValueOf() {
        assertEquals("PF", InitiativeGeneral.BeneficiaryTypeEnum.valueOf("PF").toString());
    }
}

