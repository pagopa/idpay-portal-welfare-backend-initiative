package it.gov.pagopa.initiative.event;

import it.gov.pagopa.initiative.model.Initiative;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {InitiativeProducer.class})
@ExtendWith(SpringExtension.class)
class InitiativeProducerTest {
    @Autowired
    private InitiativeProducer initiativeProducer;

    @Test
    @Disabled("TODO: Complete this test")
    void testSendPublishInitiative() {

        // Arrange
        // TODO: Populate arranged inputs
        Initiative initiative = null;

        // Act
        boolean actualSendPublishInitiativeResult = this.initiativeProducer.sendPublishInitiative(initiative);

        // Assert
        // TODO: Add assertions on result
    }
}

