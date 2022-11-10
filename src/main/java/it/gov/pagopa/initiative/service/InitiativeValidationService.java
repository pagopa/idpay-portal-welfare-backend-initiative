package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.model.Initiative;

public interface InitiativeValidationService {

    Initiative getInitiative(String organizationId, String initiativeId, String role);

    void checkPermissionBeforeInsert(String role);
}