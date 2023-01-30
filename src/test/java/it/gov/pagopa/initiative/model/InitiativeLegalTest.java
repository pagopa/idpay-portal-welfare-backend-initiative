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
        assertNotEquals(null, new InitiativeLegal("Privacy Link", "Tc Link", "Regulation Link", "Dpia Link"));
        assertNotEquals("Different type to InitiativeLegal",
                new InitiativeLegal("Privacy Link", "Tc Link", "Regulation Link", "Dpia Link"));
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

    @Test
    void testEquals5() {
        InitiativeLegal initiativeLegal = new InitiativeLegal("Privacy Link", "Tc Link", "Regulation Link", "Dpia Link");
        assertEquals(initiativeLegal, initiativeLegal);
        int expectedHashCodeResult = initiativeLegal.hashCode();
        assertEquals(expectedHashCodeResult, initiativeLegal.hashCode());
    }

    @Test
    void testEquals6() {
        InitiativeLegal initiativeLegal = new InitiativeLegal(null, "Tc Link", "Regulation Link", "Dpia Link");
        assertNotEquals(initiativeLegal, new InitiativeLegal("Privacy Link", "Tc Link", "Regulation Link", "Dpia Link"));
    }

    @Test
    void testEquals7() {
        InitiativeLegal initiativeLegal = new InitiativeLegal("Privacy Link", "Privacy Link", "Regulation Link",
                "Dpia Link");
        assertNotEquals(initiativeLegal, new InitiativeLegal("Privacy Link", "Tc Link", "Regulation Link", "Dpia Link"));
    }

    @Test
    void testEquals8() {
        InitiativeLegal initiativeLegal = new InitiativeLegal("Privacy Link", null, "Regulation Link", "Dpia Link");
        assertNotEquals(initiativeLegal, new InitiativeLegal("Privacy Link", "Tc Link", "Regulation Link", "Dpia Link"));
    }

    @Test
    void testEquals9() {
        InitiativeLegal initiativeLegal = new InitiativeLegal("Privacy Link", "Tc Link", "Privacy Link", "Dpia Link");
        assertNotEquals(initiativeLegal, new InitiativeLegal("Privacy Link", "Tc Link", "Regulation Link", "Dpia Link"));
    }

    @Test
    void testEquals10() {
        InitiativeLegal initiativeLegal = new InitiativeLegal("Privacy Link", "Tc Link", null, "Dpia Link");
        assertNotEquals(initiativeLegal, new InitiativeLegal("Privacy Link", "Tc Link", "Regulation Link", "Dpia Link"));
    }

    @Test
    void testEquals11() {
        InitiativeLegal initiativeLegal = new InitiativeLegal("Privacy Link", "Tc Link", "Regulation Link",
                "Privacy Link");
        assertNotEquals(initiativeLegal, new InitiativeLegal("Privacy Link", "Tc Link", "Regulation Link", "Dpia Link"));
    }

    @Test
    void testEquals12() {
        InitiativeLegal initiativeLegal = new InitiativeLegal("Privacy Link", "Tc Link", "Regulation Link", null);
        assertNotEquals(initiativeLegal, new InitiativeLegal("Privacy Link", "Tc Link", "Regulation Link", "Dpia Link"));
    }

    @Test
    void testEquals13() {
        InitiativeLegal initiativeLegal = new InitiativeLegal("Privacy Link", null, "Regulation Link", "Dpia Link");
        InitiativeLegal initiativeLegal1 = new InitiativeLegal("Privacy Link", null, "Regulation Link", "Dpia Link");

        assertEquals(initiativeLegal, initiativeLegal1);
        int expectedHashCodeResult = initiativeLegal.hashCode();
        assertEquals(expectedHashCodeResult, initiativeLegal1.hashCode());
    }

    @Test
    void testEquals14() {
        InitiativeLegal initiativeLegal = new InitiativeLegal("Privacy Link", "Tc Link", null, "Dpia Link");
        InitiativeLegal initiativeLegal1 = new InitiativeLegal("Privacy Link", "Tc Link", null, "Dpia Link");

        assertEquals(initiativeLegal, initiativeLegal1);
        int expectedHashCodeResult = initiativeLegal.hashCode();
        assertEquals(expectedHashCodeResult, initiativeLegal1.hashCode());
    }

    @Test
    void testEquals15() {
        InitiativeLegal initiativeLegal = new InitiativeLegal("Privacy Link", "Tc Link", "Regulation Link", null);
        InitiativeLegal initiativeLegal1 = new InitiativeLegal("Privacy Link", "Tc Link", "Regulation Link", null);

        assertEquals(initiativeLegal, initiativeLegal1);
        int expectedHashCodeResult = initiativeLegal.hashCode();
        assertEquals(expectedHashCodeResult, initiativeLegal1.hashCode());
    }
}

