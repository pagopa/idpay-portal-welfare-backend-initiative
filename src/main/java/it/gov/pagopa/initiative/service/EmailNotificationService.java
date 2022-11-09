package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.model.Initiative;

public interface EmailNotificationService extends NotificationService{

    void sendInitiativeEntePagoPA(Initiative initiative, String organizationName);
    void sendInitiativeEnte(Initiative initiative, String organizationName);
    void sendInitiativeEnteCreated(Initiative initiative, String organizationName);
}
