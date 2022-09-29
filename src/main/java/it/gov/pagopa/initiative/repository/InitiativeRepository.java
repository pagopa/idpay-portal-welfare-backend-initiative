package it.gov.pagopa.initiative.repository;

import it.gov.pagopa.initiative.model.Initiative;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InitiativeRepository extends MongoRepository<Initiative, String> {

    @Query(value="{organizationId : ?0, deleted: ?1}", fields="{initiativeId : 1, initiativeName : 1, status : 1, 'additionalInfo.serviceName' : 1, creationDate : 1, updateDate : 1}")
    List<Initiative> retrieveInitiativeSummary(String organizationId, Boolean deleted);

    Optional<Initiative> findByOrganizationIdAndInitiativeIdAndDeleted(String organizationId, String initiativeId, Boolean deleted);

    @Query(
            value="{initiativeId : ?0, deleted: ?1}",
            fields="{initiativeId : 1, " +
                    "initiativeName : 1, " +
                    "status : 1, " +
                    "'additionalInfo.serviceId' : 1, " +
                    "'additionalInfo.serviceScope' : 1, " +
                    "'general' : 1, " +
                    "'beneficiaryRule' : 1}")
    Optional<Initiative> retrieveInitiativeBeneficiaryView(String initiativeId, Boolean deleted);
}
