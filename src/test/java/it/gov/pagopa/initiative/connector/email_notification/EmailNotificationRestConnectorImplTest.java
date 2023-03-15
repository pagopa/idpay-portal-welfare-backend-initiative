package it.gov.pagopa.initiative.connector.email_notification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {EmailNotificationRestConnectorImpl.class})
@ExtendWith(SpringExtension.class)
class EmailNotificationRestConnectorImplTest {
    @MockBean
    private EmailNotificationFeignRestClient emailNotificationFeignRestClient;

    @Autowired
    private EmailNotificationRestConnectorImpl emailNotificationRestConnectorImpl;

    /**
     * Method under test: {@link EmailNotificationRestConnectorImpl#notifyInitiativeToEmailNotification(String, Map, String, String, String)}
     */
    @Test
    void testNotifyInitiativeToEmailNotification() {
        when(emailNotificationFeignRestClient.notifyInitiativeInfo(any()))
                .thenReturn(new ResponseEntity<>(HttpStatus.CONTINUE));

        emailNotificationRestConnectorImpl.notifyInitiativeToEmailNotification("Template Name", new HashMap<>(),
                "Hello from the Dreaming Spires", "Sender", "Recipients");
        verify(emailNotificationFeignRestClient).notifyInitiativeInfo(any());
    }
}

