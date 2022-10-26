package it.gov.pagopa.initiative.connector.email_notification;

import it.gov.pagopa.initiative.dto.email_notification.EmailMessageDTO;
import it.gov.pagopa.initiative.model.Initiative;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class EmailNotificationRestConnectorImpl implements EmailNotificationRestConnector {

    private final EmailNotificationFeignRestClient emailNotificationFeignRestClient;

    public EmailNotificationRestConnectorImpl(
            EmailNotificationFeignRestClient emailNotificationFeignRestClient) {
        this.emailNotificationFeignRestClient = emailNotificationFeignRestClient;
    }

    @Override
    public void notifyInitiativeToEmailNotification(Initiative initiative, String templateName, Map<String, String> templateValues, String subject, String sender, String recipients) {
        EmailMessageDTO emailMessageDTO = EmailMessageDTO.builder()
                .templateName(templateName)
                .templateValues(templateValues)
                .subject(subject)
                .senderEmail(sender)
                .recipientEmail(recipients)
                .build();
        emailNotificationFeignRestClient.notifyInitiativeInfo(emailMessageDTO);
    }

}
