package it.gov.pagopa.initiative.model.rule.refund;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

class InitiativeRefundRuleTest {
    @Test
    void testCanEqual() {
        assertFalse((new InitiativeRefundRule()).canEqual("Other"));
    }

    @Test
    void testCanEqual2() {
        InitiativeRefundRule initiativeRefundRule = new InitiativeRefundRule();
        assertTrue(initiativeRefundRule.canEqual(new InitiativeRefundRule()));
    }

    @Test
    void testConstructor() {
        InitiativeRefundRule actualInitiativeRefundRule = new InitiativeRefundRule();
        AccumulatedAmount accumulatedAmount = new AccumulatedAmount();
        actualInitiativeRefundRule.setAccumulatedAmount(accumulatedAmount);
        AdditionalInfo additionalInfo = new AdditionalInfo("Identification Code");
        actualInitiativeRefundRule.setAdditionalInfo(additionalInfo);
        TimeParameter timeParameter = new TimeParameter(TimeParameter.TimeTypeEnum.CLOSED);
        actualInitiativeRefundRule.setTimeParameter(timeParameter);
        String actualToStringResult = actualInitiativeRefundRule.toString();
        assertSame(accumulatedAmount, actualInitiativeRefundRule.getAccumulatedAmount());
        assertSame(additionalInfo, actualInitiativeRefundRule.getAdditionalInfo());
        assertSame(timeParameter, actualInitiativeRefundRule.getTimeParameter());
        assertEquals("InitiativeRefundRule(accumulatedAmount=AccumulatedAmount(accumulatedType=null, refundThresholdCents=null),"
                + " timeParameter=TimeParameter(timeType=CLOSED), additionalInfo=AdditionalInfo(identificationCode"
                + "=Identification Code))", actualToStringResult);
    }

    @Test
    void testConstructor2() {
        AccumulatedAmount accumulatedAmount = new AccumulatedAmount();
        TimeParameter timeParameter = new TimeParameter(TimeParameter.TimeTypeEnum.CLOSED);
        AdditionalInfo additionalInfo = new AdditionalInfo("Identification Code");
        InitiativeRefundRule actualInitiativeRefundRule = new InitiativeRefundRule(accumulatedAmount, timeParameter,
                additionalInfo);
        AccumulatedAmount accumulatedAmount1 = new AccumulatedAmount();
        actualInitiativeRefundRule.setAccumulatedAmount(accumulatedAmount1);
        AdditionalInfo additionalInfo1 = new AdditionalInfo("Identification Code");
        actualInitiativeRefundRule.setAdditionalInfo(additionalInfo1);
        TimeParameter timeParameter1 = new TimeParameter(TimeParameter.TimeTypeEnum.CLOSED);
        actualInitiativeRefundRule.setTimeParameter(timeParameter1);
        String actualToStringResult = actualInitiativeRefundRule.toString();
        AccumulatedAmount accumulatedAmount2 = actualInitiativeRefundRule.getAccumulatedAmount();
        assertSame(accumulatedAmount1, accumulatedAmount2);
        assertEquals(accumulatedAmount, accumulatedAmount2);
        AdditionalInfo additionalInfo2 = actualInitiativeRefundRule.getAdditionalInfo();
        assertSame(additionalInfo1, additionalInfo2);
        assertEquals(additionalInfo, additionalInfo2);
        TimeParameter timeParameter2 = actualInitiativeRefundRule.getTimeParameter();
        assertSame(timeParameter1, timeParameter2);
        assertEquals(timeParameter, timeParameter2);
        assertEquals(
                "InitiativeRefundRule(accumulatedAmount=AccumulatedAmount(accumulatedType=null, refundThresholdCents=null),"
                        + " timeParameter=TimeParameter(timeType=CLOSED), additionalInfo=AdditionalInfo(identificationCode"
                        + "=Identification Code))",
                actualToStringResult);
    }

    @Test
    void testEquals() {
        assertNotEquals(null, new InitiativeRefundRule());
        assertNotEquals("Different type to InitiativeRefundRule", new InitiativeRefundRule());
    }

    @Test
    void testEquals2() {
        InitiativeRefundRule initiativeRefundRule = new InitiativeRefundRule();
        assertEquals(initiativeRefundRule, initiativeRefundRule);
        int expectedHashCodeResult = initiativeRefundRule.hashCode();
        assertEquals(expectedHashCodeResult, initiativeRefundRule.hashCode());
    }

    @Test
    void testEquals3() {
        InitiativeRefundRule initiativeRefundRule = new InitiativeRefundRule();
        InitiativeRefundRule initiativeRefundRule1 = new InitiativeRefundRule();
        assertEquals(initiativeRefundRule, initiativeRefundRule1);
        int expectedHashCodeResult = initiativeRefundRule.hashCode();
        assertEquals(expectedHashCodeResult, initiativeRefundRule1.hashCode());
    }

    @Test
    void testEquals4() {
        AccumulatedAmount accumulatedAmount = new AccumulatedAmount();
        TimeParameter timeParameter = new TimeParameter(TimeParameter.TimeTypeEnum.CLOSED);
        InitiativeRefundRule initiativeRefundRule = new InitiativeRefundRule(accumulatedAmount, timeParameter,
                new AdditionalInfo("Identification Code"));
        assertNotEquals(initiativeRefundRule, new InitiativeRefundRule());
    }

    @Test
    void testEquals5() {
        InitiativeRefundRule initiativeRefundRule = new InitiativeRefundRule();
        AccumulatedAmount accumulatedAmount = new AccumulatedAmount();
        TimeParameter timeParameter = new TimeParameter(TimeParameter.TimeTypeEnum.CLOSED);
        assertNotEquals(initiativeRefundRule,
                new InitiativeRefundRule(accumulatedAmount, timeParameter, new AdditionalInfo("Identification Code")));
    }

    @Test
    void testEquals6() {
        InitiativeRefundRule initiativeRefundRule = new InitiativeRefundRule();
        initiativeRefundRule.setTimeParameter(new TimeParameter(TimeParameter.TimeTypeEnum.CLOSED));
        assertNotEquals(initiativeRefundRule, new InitiativeRefundRule());
    }

    @Test
    void testEquals7() {
        InitiativeRefundRule initiativeRefundRule = new InitiativeRefundRule();
        initiativeRefundRule.setAdditionalInfo(new AdditionalInfo("Identification Code"));
        assertNotEquals(initiativeRefundRule, new InitiativeRefundRule());
    }

    @Test
    void testEquals8() {
        AccumulatedAmount accumulatedAmount = mock(AccumulatedAmount.class);
        TimeParameter timeParameter = new TimeParameter(TimeParameter.TimeTypeEnum.CLOSED);
        InitiativeRefundRule initiativeRefundRule = new InitiativeRefundRule(accumulatedAmount, timeParameter,
                new AdditionalInfo("Identification Code"));
        assertNotEquals(initiativeRefundRule, new InitiativeRefundRule());
    }

    @Test
    void testEquals9() {
        AccumulatedAmount accumulatedAmount = new AccumulatedAmount();
        TimeParameter timeParameter = new TimeParameter(TimeParameter.TimeTypeEnum.CLOSED);
        InitiativeRefundRule initiativeRefundRule = new InitiativeRefundRule(accumulatedAmount, timeParameter,
                new AdditionalInfo("Identification Code"));
        AccumulatedAmount accumulatedAmount1 = new AccumulatedAmount();
        TimeParameter timeParameter1 = new TimeParameter(TimeParameter.TimeTypeEnum.CLOSED);
        InitiativeRefundRule initiativeRefundRule1 = new InitiativeRefundRule(accumulatedAmount1, timeParameter1,
                new AdditionalInfo("Identification Code"));

        assertEquals(initiativeRefundRule, initiativeRefundRule1);
        int expectedHashCodeResult = initiativeRefundRule.hashCode();
        assertEquals(expectedHashCodeResult, initiativeRefundRule1.hashCode());
    }

    @Test
    void testEquals10() {
        InitiativeRefundRule initiativeRefundRule = new InitiativeRefundRule();

        InitiativeRefundRule initiativeRefundRule1 = new InitiativeRefundRule();
        initiativeRefundRule1.setTimeParameter(new TimeParameter(TimeParameter.TimeTypeEnum.CLOSED));
        assertNotEquals(initiativeRefundRule, initiativeRefundRule1);
    }

    @Test
    void testEquals11() {
        InitiativeRefundRule initiativeRefundRule = new InitiativeRefundRule();

        InitiativeRefundRule initiativeRefundRule1 = new InitiativeRefundRule();
        initiativeRefundRule1.setAdditionalInfo(new AdditionalInfo("Identification Code"));
        assertNotEquals(initiativeRefundRule, initiativeRefundRule1);
    }
}

