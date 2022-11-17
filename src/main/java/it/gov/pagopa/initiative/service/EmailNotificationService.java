package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.model.Initiative;

public interface EmailNotificationService extends NotificationService{

    void sendInitiativeToCurrentOrganization(Initiative initiative, String templateName, String subject);
    void sendInitiativeToPagoPA(Initiative initiative, String templateName, String subject);

}
