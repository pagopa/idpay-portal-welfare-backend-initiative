package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.model.Initiative;

public interface EmailNotificationService extends NotificationService{

    void sendInitiativeInRevision(Initiative initiative, String organizationName);
    void sendInitiativeApprovedAndRejected(Initiative initiative, String organizationName);

}
