package it.gov.pagopa.initiative.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class InitiativeBeneficiaryRuleTest {

    @Test
    void testCanEqual() {
        assertFalse((new InitiativeBeneficiaryRule()).canEqual("Other"));
    }

    @Test
    void testCanEqual2() {
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = new InitiativeBeneficiaryRule();
        assertTrue(initiativeBeneficiaryRule.canEqual(new InitiativeBeneficiaryRule()));
    }

    @Test
    void testConstructor() {
        InitiativeBeneficiaryRule actualInitiativeBeneficiaryRule = new InitiativeBeneficiaryRule();
        actualInitiativeBeneficiaryRule.setApiKeyClientAssertion("Api Key Client Assertion");
        actualInitiativeBeneficiaryRule.setApiKeyClientId("42");
        ArrayList<AutomatedCriteria> automatedCriteriaList = new ArrayList<>();
        actualInitiativeBeneficiaryRule.setAutomatedCriteria(automatedCriteriaList);
        ArrayList<ISelfDeclarationCriteria> iSelfDeclarationCriteriaList = new ArrayList<>();
        actualInitiativeBeneficiaryRule.setSelfDeclarationCriteria(iSelfDeclarationCriteriaList);
        String actualToStringResult = actualInitiativeBeneficiaryRule.toString();
        assertEquals("Api Key Client Assertion", actualInitiativeBeneficiaryRule.getApiKeyClientAssertion());
        assertEquals("42", actualInitiativeBeneficiaryRule.getApiKeyClientId());
        List<AutomatedCriteria> automatedCriteria = actualInitiativeBeneficiaryRule.getAutomatedCriteria();
        assertSame(automatedCriteriaList, automatedCriteria);
        List<ISelfDeclarationCriteria> selfDeclarationCriteria = actualInitiativeBeneficiaryRule
                .getSelfDeclarationCriteria();
        assertEquals(selfDeclarationCriteria, automatedCriteria);
        assertSame(iSelfDeclarationCriteriaList, selfDeclarationCriteria);
        assertEquals(automatedCriteriaList, selfDeclarationCriteria);
        assertEquals("InitiativeBeneficiaryRule(selfDeclarationCriteria=[], automatedCriteria=[], apiKeyClientId=42,"
                + " apiKeyClientAssertion=Api Key Client Assertion)", actualToStringResult);
    }

    @Test
    void testConstructor2() {
        ArrayList<ISelfDeclarationCriteria> iSelfDeclarationCriteriaList = new ArrayList<>();
        ArrayList<AutomatedCriteria> automatedCriteriaList = new ArrayList<>();
        InitiativeBeneficiaryRule actualInitiativeBeneficiaryRule = new InitiativeBeneficiaryRule(
                iSelfDeclarationCriteriaList, automatedCriteriaList, "42", "Api Key Client Assertion");
        actualInitiativeBeneficiaryRule.setApiKeyClientAssertion("Api Key Client Assertion");
        actualInitiativeBeneficiaryRule.setApiKeyClientId("42");
        ArrayList<AutomatedCriteria> automatedCriteriaList1 = new ArrayList<>();
        actualInitiativeBeneficiaryRule.setAutomatedCriteria(automatedCriteriaList1);
        ArrayList<ISelfDeclarationCriteria> iSelfDeclarationCriteriaList1 = new ArrayList<>();
        actualInitiativeBeneficiaryRule.setSelfDeclarationCriteria(iSelfDeclarationCriteriaList1);
        String actualToStringResult = actualInitiativeBeneficiaryRule.toString();
        assertEquals("Api Key Client Assertion", actualInitiativeBeneficiaryRule.getApiKeyClientAssertion());
        assertEquals("42", actualInitiativeBeneficiaryRule.getApiKeyClientId());
        List<AutomatedCriteria> automatedCriteria = actualInitiativeBeneficiaryRule.getAutomatedCriteria();
        assertSame(automatedCriteriaList1, automatedCriteria);
        assertEquals(iSelfDeclarationCriteriaList, automatedCriteria);
        assertEquals(automatedCriteriaList, automatedCriteria);
        List<ISelfDeclarationCriteria> selfDeclarationCriteria = actualInitiativeBeneficiaryRule
                .getSelfDeclarationCriteria();
        assertEquals(selfDeclarationCriteria, automatedCriteria);
        assertSame(iSelfDeclarationCriteriaList1, selfDeclarationCriteria);
        assertEquals(iSelfDeclarationCriteriaList, selfDeclarationCriteria);
        assertEquals(automatedCriteriaList, selfDeclarationCriteria);
        assertEquals(automatedCriteriaList1, selfDeclarationCriteria);
        assertEquals("InitiativeBeneficiaryRule(selfDeclarationCriteria=[], automatedCriteria=[], apiKeyClientId=42,"
                + " apiKeyClientAssertion=Api Key Client Assertion)", actualToStringResult);
    }

    @Test
    void testEquals() {
        assertNotEquals(null, new InitiativeBeneficiaryRule());
        assertNotEquals("Different type to InitiativeBeneficiaryRule", new InitiativeBeneficiaryRule());
    }

    @Test
    void testEquals2() {
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = new InitiativeBeneficiaryRule();
        assertEquals(initiativeBeneficiaryRule, initiativeBeneficiaryRule);
        int expectedHashCodeResult = initiativeBeneficiaryRule.hashCode();
        assertEquals(expectedHashCodeResult, initiativeBeneficiaryRule.hashCode());
    }

    @Test
    void testEquals3() {
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = new InitiativeBeneficiaryRule();
        InitiativeBeneficiaryRule initiativeBeneficiaryRule1 = new InitiativeBeneficiaryRule();
        assertEquals(initiativeBeneficiaryRule, initiativeBeneficiaryRule1);
        int expectedHashCodeResult = initiativeBeneficiaryRule.hashCode();
        assertEquals(expectedHashCodeResult, initiativeBeneficiaryRule1.hashCode());
    }

    @Test
    void testEquals4() {
        ArrayList<ISelfDeclarationCriteria> selfDeclarationCriteria = new ArrayList<>();
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = new InitiativeBeneficiaryRule(selfDeclarationCriteria,
                new ArrayList<>(), "42", "Api Key Client Assertion");
        assertNotEquals(initiativeBeneficiaryRule, new InitiativeBeneficiaryRule());
    }

    @Test
    void testEquals5() {
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = new InitiativeBeneficiaryRule();
        ArrayList<ISelfDeclarationCriteria> selfDeclarationCriteria = new ArrayList<>();
        assertNotEquals(initiativeBeneficiaryRule,
                new InitiativeBeneficiaryRule(selfDeclarationCriteria, new ArrayList<>(), "42", "Api Key Client Assertion"));
    }

    @Test
    void testEquals6() {
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = new InitiativeBeneficiaryRule();
        initiativeBeneficiaryRule.setAutomatedCriteria(new ArrayList<>());
        assertNotEquals(initiativeBeneficiaryRule, new InitiativeBeneficiaryRule());
    }

    @Test
    void testEquals7() {
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = new InitiativeBeneficiaryRule();
        initiativeBeneficiaryRule.setApiKeyClientId("42");
        assertNotEquals(initiativeBeneficiaryRule, new InitiativeBeneficiaryRule());
    }

    @Test
    void testEquals8() {
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = new InitiativeBeneficiaryRule();
        initiativeBeneficiaryRule.setApiKeyClientAssertion("Api Key Client Assertion");
        assertNotEquals(initiativeBeneficiaryRule, new InitiativeBeneficiaryRule());
    }

    @Test
    void testEquals9() {
        ArrayList<ISelfDeclarationCriteria> selfDeclarationCriteria = new ArrayList<>();
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = new InitiativeBeneficiaryRule(selfDeclarationCriteria,
                new ArrayList<>(), "42", "Api Key Client Assertion");
        ArrayList<ISelfDeclarationCriteria> selfDeclarationCriteria1 = new ArrayList<>();
        InitiativeBeneficiaryRule initiativeBeneficiaryRule1 = new InitiativeBeneficiaryRule(selfDeclarationCriteria1,
                new ArrayList<>(), "42", "Api Key Client Assertion");

        assertEquals(initiativeBeneficiaryRule, initiativeBeneficiaryRule1);
        int expectedHashCodeResult = initiativeBeneficiaryRule.hashCode();
        assertEquals(expectedHashCodeResult, initiativeBeneficiaryRule1.hashCode());
    }

    @Test
    void testEquals10() {
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = new InitiativeBeneficiaryRule();

        InitiativeBeneficiaryRule initiativeBeneficiaryRule1 = new InitiativeBeneficiaryRule();
        initiativeBeneficiaryRule1.setAutomatedCriteria(new ArrayList<>());
        assertNotEquals(initiativeBeneficiaryRule, initiativeBeneficiaryRule1);
    }

    @Test
    void testEquals11() {
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = new InitiativeBeneficiaryRule();

        InitiativeBeneficiaryRule initiativeBeneficiaryRule1 = new InitiativeBeneficiaryRule();
        initiativeBeneficiaryRule1.setApiKeyClientId("42");
        assertNotEquals(initiativeBeneficiaryRule, initiativeBeneficiaryRule1);
    }

    @Test
    void testEquals12() {
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = new InitiativeBeneficiaryRule();

        InitiativeBeneficiaryRule initiativeBeneficiaryRule1 = new InitiativeBeneficiaryRule();
        initiativeBeneficiaryRule1.setApiKeyClientAssertion("Api Key Client Assertion");
        assertNotEquals(initiativeBeneficiaryRule, initiativeBeneficiaryRule1);
    }
}

