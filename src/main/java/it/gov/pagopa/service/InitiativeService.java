package it.gov.pagopa.service;

import it.gov.pagopa.model.Initiative;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface InitiativeService {

    public List<Initiative> retrieveInitiativeSummary(String organizationId);
}
