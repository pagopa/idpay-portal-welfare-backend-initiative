package it.gov.pagopa.initiative.model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class AutomatedCriteriaTest {

    @Test
    void testOrderDirectionIsAscending() {
        assertTrue(AutomatedCriteria.OrderDirection.ASC.isAscending());
        assertFalse(AutomatedCriteria.OrderDirection.DESC.isAscending());
    }

    @Test
    void testOrderDirectionIsDescending() {
        assertFalse(AutomatedCriteria.OrderDirection.ASC.isDescending());
        assertTrue(AutomatedCriteria.OrderDirection.DESC.isDescending());
    }
}

