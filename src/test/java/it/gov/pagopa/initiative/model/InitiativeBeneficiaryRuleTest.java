package it.gov.pagopa.initiative.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

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
        ArrayList<AutomatedCriteria> automatedCriteriaList = new ArrayList<>();
        actualInitiativeBeneficiaryRule.setAutomatedCriteria(automatedCriteriaList);
        ArrayList<ISelfDeclarationCriteria> iSelfDeclarationCriteriaList = new ArrayList<>();
        actualInitiativeBeneficiaryRule.setSelfDeclarationCriteria(iSelfDeclarationCriteriaList);
        String actualToStringResult = actualInitiativeBeneficiaryRule.toString();
        List<AutomatedCriteria> automatedCriteria = actualInitiativeBeneficiaryRule.getAutomatedCriteria();
        assertSame(automatedCriteriaList, automatedCriteria);
        assertEquals(iSelfDeclarationCriteriaList, automatedCriteriaList);
        List<ISelfDeclarationCriteria> selfDeclarationCriteria = actualInitiativeBeneficiaryRule
                .getSelfDeclarationCriteria();
        assertSame(iSelfDeclarationCriteriaList, selfDeclarationCriteria);
        assertEquals(automatedCriteria, selfDeclarationCriteria);
        assertEquals("InitiativeBeneficiaryRule(selfDeclarationCriteria=[], automatedCriteria=[])", actualToStringResult);
    }

    @Test
    void testConstructor2() {
        ArrayList<ISelfDeclarationCriteria> iSelfDeclarationCriteriaList = new ArrayList<>();
        ArrayList<AutomatedCriteria> automatedCriteriaList = new ArrayList<>();
        InitiativeBeneficiaryRule actualInitiativeBeneficiaryRule = new InitiativeBeneficiaryRule(
                iSelfDeclarationCriteriaList, automatedCriteriaList);
        ArrayList<AutomatedCriteria> automatedCriteriaList1 = new ArrayList<>();
        actualInitiativeBeneficiaryRule.setAutomatedCriteria(automatedCriteriaList1);
        ArrayList<ISelfDeclarationCriteria> iSelfDeclarationCriteriaList1 = new ArrayList<>();
        actualInitiativeBeneficiaryRule.setSelfDeclarationCriteria(iSelfDeclarationCriteriaList1);
        String actualToStringResult = actualInitiativeBeneficiaryRule.toString();
        List<AutomatedCriteria> automatedCriteria = actualInitiativeBeneficiaryRule.getAutomatedCriteria();
        assertSame(automatedCriteriaList1, automatedCriteria);
        assertEquals(iSelfDeclarationCriteriaList, automatedCriteria);
        assertEquals(automatedCriteriaList, automatedCriteria);
        assertEquals(iSelfDeclarationCriteriaList1, automatedCriteria);
        List<ISelfDeclarationCriteria> selfDeclarationCriteria = actualInitiativeBeneficiaryRule
                .getSelfDeclarationCriteria();
        assertSame(iSelfDeclarationCriteriaList1, selfDeclarationCriteria);
        assertEquals(iSelfDeclarationCriteriaList, selfDeclarationCriteria);
        assertEquals(automatedCriteriaList, selfDeclarationCriteria);
        assertEquals(automatedCriteria, selfDeclarationCriteria);
        assertEquals("InitiativeBeneficiaryRule(selfDeclarationCriteria=[], automatedCriteria=[])", actualToStringResult);
    }

    @Test
    void testEquals() {
        assertNotEquals(null, new InitiativeBeneficiaryRule());}

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
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = new InitiativeBeneficiaryRule();
        ArrayList<ISelfDeclarationCriteria> selfDeclarationCriteria = new ArrayList<>();
        assertNotEquals(initiativeBeneficiaryRule,
                new InitiativeBeneficiaryRule(selfDeclarationCriteria, new ArrayList<>()));
    }

    @Test
    void testEquals5() {
        ArrayList<ISelfDeclarationCriteria> selfDeclarationCriteria = new ArrayList<>();
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = new InitiativeBeneficiaryRule(selfDeclarationCriteria,
                new ArrayList<>());
        ArrayList<ISelfDeclarationCriteria> selfDeclarationCriteria1 = new ArrayList<>();
        InitiativeBeneficiaryRule initiativeBeneficiaryRule1 = new InitiativeBeneficiaryRule(selfDeclarationCriteria1,
                new ArrayList<>());

        assertEquals(initiativeBeneficiaryRule, initiativeBeneficiaryRule1);
        int expectedHashCodeResult = initiativeBeneficiaryRule.hashCode();
        assertEquals(expectedHashCodeResult, initiativeBeneficiaryRule1.hashCode());
    }

    @Test
    void testEquals6() {
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = new InitiativeBeneficiaryRule();

        InitiativeBeneficiaryRule initiativeBeneficiaryRule1 = new InitiativeBeneficiaryRule();
        initiativeBeneficiaryRule1.setAutomatedCriteria(new ArrayList<>());
        assertNotEquals(initiativeBeneficiaryRule, initiativeBeneficiaryRule1);
    }

    @Test
    void testEquals7() {
        ArrayList<ISelfDeclarationCriteria> iSelfDeclarationCriteriaList = new ArrayList<>();
        iSelfDeclarationCriteriaList.add(mock(SelfCriteriaBool.class));
        InitiativeBeneficiaryRule initiativeBeneficiaryRule = new InitiativeBeneficiaryRule(iSelfDeclarationCriteriaList,
                new ArrayList<>());
        assertNotEquals(initiativeBeneficiaryRule, new InitiativeBeneficiaryRule());
    }
}

