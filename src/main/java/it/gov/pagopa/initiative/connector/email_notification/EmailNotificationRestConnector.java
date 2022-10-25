package it.gov.pagopa.initiative.connector.email_notification;

import it.gov.pagopa.initiative.model.Initiative;

import java.util.Map;

public interface EmailNotificationRestConnector {

    void notifyInitiativeToEmailNotification(Initiative initiative, String templateName, Map<String, String> templateValues, String subject, String recipients);

}
