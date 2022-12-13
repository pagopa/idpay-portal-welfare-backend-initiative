package it.gov.pagopa.initiative.model.rule.reward;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class RewardValueTest {

    @Test
    void testCanEqual() {
        assertFalse((new RewardValue()).canEqual("Other"));
    }

    @Test
    void testCanEqual2() {
        RewardValue rewardValue = new RewardValue();
        assertTrue(rewardValue.canEqual(new RewardValue()));
    }

    @Test
    void testConstructor() {
        RewardValue actualRewardValue = new RewardValue();
        BigDecimal valueOfResult = BigDecimal.valueOf(42L);
        actualRewardValue.setRewardValue(valueOfResult);
        actualRewardValue.setType("Type");
        String actualToStringResult = actualRewardValue.toString();
        assertSame(valueOfResult, actualRewardValue.getRewardValue());
        assertEquals("Type", actualRewardValue.getType());
        assertEquals("RewardValue(type=Type, rewardValue=42)", actualToStringResult);
    }

    @Test
    void testConstructor2() {
        BigDecimal valueOfResult = BigDecimal.valueOf(42L);
        RewardValue actualRewardValue = new RewardValue("Type", valueOfResult);
        BigDecimal valueOfResult1 = BigDecimal.valueOf(42L);
        actualRewardValue.setRewardValue(valueOfResult1);
        actualRewardValue.setType("Type");
        String actualToStringResult = actualRewardValue.toString();
        BigDecimal rewardValue = actualRewardValue.getRewardValue();
        assertSame(valueOfResult1, rewardValue);
        assertEquals(valueOfResult, rewardValue);
        assertEquals("Type", actualRewardValue.getType());
        assertEquals("RewardValue(type=Type, rewardValue=42)", actualToStringResult);
    }

    @Test
    void testEquals() {
        assertNotEquals(null, new RewardValue());
    }

    @Test
    void testEquals2() {
        RewardValue rewardValue = new RewardValue();
        RewardValue rewardValue1 = new RewardValue();
        assertEquals(rewardValue, rewardValue1);
        int expectedHashCodeResult = rewardValue.hashCode();
        assertEquals(expectedHashCodeResult, rewardValue1.hashCode());
    }

    @Test
    void testEquals3() {
        RewardValue rewardValue = new RewardValue();
        rewardValue.setRewardValue(BigDecimal.valueOf(42L));
        assertNotEquals(rewardValue, new RewardValue());
    }
}

