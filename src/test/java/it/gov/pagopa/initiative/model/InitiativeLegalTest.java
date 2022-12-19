package it.gov.pagopa.initiative.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class InitiativeLegalTest {

    @Test
    void testCanEqual() {
        assertFalse((new InitiativeLegal("Privacy Link", "Tc Link", "Regulation Link", "Dpia Link")).canEqual("Other"));
    }

    @Test
    void testCanEqual2() {
        InitiativeLegal initiativeLegal = new InitiativeLegal("Privacy Link", "Tc Link", "Regulation Link", "Dpia Link");
        assertTrue(
                initiativeLegal.canEqual(new InitiativeLegal("Privacy Link", "Tc Link", "Regulation Link", "Dpia Link")));
    }

    @Test
    void testConstructor() {
        InitiativeLegal actualInitiativeLegal = new InitiativeLegal();
        actualInitiativeLegal.setDpiaLink("Dpia Link");
        actualInitiativeLegal.setPrivacyLink("Privacy Link");
        actualInitiativeLegal.setRegulationLink("Regulation Link");
        actualInitiativeLegal.setTcLink("Tc Link");
        String actualToStringResult = actualInitiativeLegal.toString();
        assertEquals("Dpia Link", actualInitiativeLegal.getDpiaLink());
        assertEquals("Privacy Link", actualInitiativeLegal.getPrivacyLink());
        assertEquals("Regulation Link", actualInitiativeLegal.getRegulationLink());
        assertEquals("Tc Link", actualInitiativeLegal.getTcLink());
        assertEquals(
                "InitiativeLegal(privacyLink=Privacy Link, tcLink=Tc Link, regulationLink=Regulation Link, dpiaLink=Dpia"
                        + " Link)",
                actualToStringResult);
    }

    @Test
    void testConstructor2() {
        InitiativeLegal actualInitiativeLegal = new InitiativeLegal("Privacy Link", "Tc Link", "Regulation Link",
                "Dpia Link");
        actualInitiativeLegal.setDpiaLink("Dpia Link");
        actualInitiativeLegal.setPrivacyLink("Privacy Link");
        actualInitiativeLegal.setRegulationLink("Regulation Link");
        actualInitiativeLegal.setTcLink("Tc Link");
        String actualToStringResult = actualInitiativeLegal.toString();
        assertEquals("Dpia Link", actualInitiativeLegal.getDpiaLink());
        assertEquals("Privacy Link", actualInitiativeLegal.getPrivacyLink());
        assertEquals("Regulation Link", actualInitiativeLegal.getRegulationLink());
        assertEquals("Tc Link", actualInitiativeLegal.getTcLink());
        assertEquals(
                "InitiativeLegal(privacyLink=Privacy Link, tcLink=Tc Link, regulationLink=Regulation Link, dpiaLink=Dpia"
                        + " Link)",
                actualToStringResult);
    }

    @Test
    void testEquals() {
        assertNotEquals(null, new InitiativeLegal("Privacy Link", "Tc Link", "Regulation Link", "Dpia Link"));
    }

    @Test
    void testEquals2() {
        InitiativeLegal initiativeLegal = new InitiativeLegal("Privacy Link", "Tc Link", "Regulation Link", "Dpia Link");
        InitiativeLegal initiativeLegal1 = new InitiativeLegal("Privacy Link", "Tc Link", "Regulation Link", "Dpia Link");

        assertEquals(initiativeLegal, initiativeLegal1);
        int expectedHashCodeResult = initiativeLegal.hashCode();
        assertEquals(expectedHashCodeResult, initiativeLegal1.hashCode());
    }

    @Test
    void testEquals3() {
        InitiativeLegal initiativeLegal = new InitiativeLegal("Tc Link", "Tc Link", "Regulation Link", "Dpia Link");
        assertNotEquals(initiativeLegal, new InitiativeLegal("Privacy Link", "Tc Link", "Regulation Link", "Dpia Link"));
    }

    @Test
    void testEquals4() {
        InitiativeLegal initiativeLegal = new InitiativeLegal(null, "Tc Link", "Regulation Link", "Dpia Link");
        InitiativeLegal initiativeLegal1 = new InitiativeLegal(null, "Tc Link", "Regulation Link", "Dpia Link");

        assertEquals(initiativeLegal, initiativeLegal1);
        int expectedHashCodeResult = initiativeLegal.hashCode();
        assertEquals(expectedHashCodeResult, initiativeLegal1.hashCode());
    }
}

