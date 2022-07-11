package it.gov.pagopa.repository;

import it.gov.pagopa.model.Initiative;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface InitiativeRepository extends MongoRepository<Initiative, String> {

    @Query(value="{organizationId : ?0}", fields="{initiativeId : 1, initiativeName : 1, status : 1, 'additionalInfo.serviceName' : 1}")
    public List<Initiative> retrieveInitiativeSummary(String organizationId);
}
