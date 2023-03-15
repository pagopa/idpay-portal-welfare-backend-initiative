package it.gov.pagopa.initiative.connector.email_notification;

import it.gov.pagopa.initiative.dto.email_notification.EmailMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class EmailNotificationRestConnectorImpl implements EmailNotificationRestConnector {

    private final EmailNotificationFeignRestClient emailNotificationFeignRestClient;

    public EmailNotificationRestConnectorImpl(
            EmailNotificationFeignRestClient emailNotificationFeignRestClient) {
        this.emailNotificationFeignRestClient = emailNotificationFeignRestClient;
    }

    @Override
    public void notifyInitiativeToEmailNotification(String templateName, Map<String, String> templateValues, String subject, String sender, String recipients) {
        log.info("[NOTIFICATION-EMAIL] Sending email is about to start...");
        EmailMessageDTO emailMessageDTO = EmailMessageDTO.builder()
                .templateName(templateName)
                .templateValues(templateValues)
                .subject(subject)
                .senderEmail(sender)
                .recipientEmail(recipients)
                .build();
        emailNotificationFeignRestClient.notifyInitiativeInfo(emailMessageDTO);
        log.info("[NOTIFICATION-EMAIL] Email SENT");
    }

}
