package it.gov.pagopa.service;

import it.gov.pagopa.model.Initiative;
import it.gov.pagopa.repository.InitiativeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InitiativeServiceImpl implements InitiativeService {

    @Autowired
    private InitiativeRepository initiativeRepository;

    public List<Initiative> retrieveInitiativeSummary(String organizationId){
        return initiativeRepository.retrieveInitiativeSummary(organizationId);
    }

}
