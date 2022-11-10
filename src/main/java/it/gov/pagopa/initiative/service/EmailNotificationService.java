package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.model.Initiative;

public interface EmailNotificationService extends NotificationService{

    void sendInitiativeToCurrentOrganizationAndPagoPA(Initiative initiative, String templateName);
    void sendInitiativeToCurrentOrganization(Initiative initiative, String templateName);

}
