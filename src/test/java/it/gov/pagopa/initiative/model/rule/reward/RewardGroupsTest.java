package it.gov.pagopa.initiative.model.rule.reward;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RewardGroupsTest {

    @Test
    void testCanEqual() {
        assertFalse((new RewardGroups()).canEqual("Other"));
    }

    @Test
    void testCanEqual2() {
        RewardGroups rewardGroups = new RewardGroups();
        assertTrue(rewardGroups.canEqual(new RewardGroups()));
    }

    @Test
    void testConstructor() {
        RewardGroups actualRewardGroups = new RewardGroups();
        ArrayList<RewardGroups.RewardGroup> rewardGroupList = new ArrayList<>();
        actualRewardGroups.setRewardGroups(rewardGroupList);
        actualRewardGroups.setType("Type");
        String actualToStringResult = actualRewardGroups.toString();
        assertSame(rewardGroupList, actualRewardGroups.getRewardGroups());
        assertEquals("Type", actualRewardGroups.getType());
        assertEquals("RewardGroups(type=Type, rewardGroups=[])", actualToStringResult);
    }

    @Test
    void testConstructor2() {
        ArrayList<RewardGroups.RewardGroup> rewardGroupList = new ArrayList<>();
        RewardGroups actualRewardGroups = new RewardGroups("Type", rewardGroupList);
        ArrayList<RewardGroups.RewardGroup> rewardGroupList1 = new ArrayList<>();
        actualRewardGroups.setRewardGroups(rewardGroupList1);
        actualRewardGroups.setType("Type");
        String actualToStringResult = actualRewardGroups.toString();
        List<RewardGroups.RewardGroup> rewardGroups = actualRewardGroups.getRewardGroups();
        assertSame(rewardGroupList1, rewardGroups);
        assertEquals(rewardGroupList, rewardGroups);
        assertEquals("Type", actualRewardGroups.getType());
        assertEquals("RewardGroups(type=Type, rewardGroups=[])", actualToStringResult);
    }

    @Test
    void testEquals() {
        assertNotEquals(new RewardGroups(), null);
    }

    @Test
    void testEquals2() {
        RewardGroups rewardGroups = new RewardGroups();
        assertEquals(rewardGroups, rewardGroups);
        int expectedHashCodeResult = rewardGroups.hashCode();
        assertEquals(expectedHashCodeResult, rewardGroups.hashCode());
    }

    @Test
    void testEquals3() {
        RewardGroups rewardGroups = new RewardGroups();
        RewardGroups rewardGroups1 = new RewardGroups();
        assertEquals(rewardGroups, rewardGroups1);
        int expectedHashCodeResult = rewardGroups.hashCode();
        assertEquals(expectedHashCodeResult, rewardGroups1.hashCode());
    }

    @Test
    void testEquals4() {
        RewardGroups rewardGroups = new RewardGroups("Type", new ArrayList<>());
        assertNotEquals(rewardGroups, new RewardGroups());
    }

    @Test
    void testEquals5() {
        RewardGroups rewardGroups = new RewardGroups("Type", new ArrayList<>());
        RewardGroups rewardGroups1 = new RewardGroups("Type", new ArrayList<>());

        assertEquals(rewardGroups, rewardGroups1);
        int expectedHashCodeResult = rewardGroups.hashCode();
        assertEquals(expectedHashCodeResult, rewardGroups1.hashCode());
    }

    @Test
    void testEquals6() {
        RewardGroups rewardGroups = new RewardGroups();

        RewardGroups rewardGroups1 = new RewardGroups();
        rewardGroups1.setRewardGroups(new ArrayList<>());
        assertNotEquals(rewardGroups, rewardGroups1);
    }
}

