package it.gov.pagopa.initiative.model.rule.trx;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DayOfWeekTest {

    @Test
    void testConstructor() {
        assertTrue((new DayOfWeek()).isEmpty());
    }
}

