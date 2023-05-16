package it.gov.pagopa.initiative.connector.email_notification;

import java.util.Map;

public interface EmailNotificationRestConnector {

    void notifyInitiativeToEmailNotification(String templateName, Map<String, String> templateValues, String subject, String sender, String recipients);

}
