package it.gov.pagopa.initiative.repository;

import it.gov.pagopa.initiative.dto.OrganizationDTO;
import it.gov.pagopa.initiative.model.Initiative;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InitiativeRepository extends MongoRepository<Initiative, String>, InitiativeRepositoryExtended {

    @Query(value="{organizationId : ?0, enabled: ?1}", fields="{initiativeId : 1, initiativeName : 1, status : 1, 'additionalInfo.serviceName' : 1, creationDate : 1, updateDate : 1, 'general.rankingEnabled' : 1}")
    List<Initiative> retrieveInitiativeSummary(String organizationId, Boolean enabled);

    List<Initiative> findByEnabledAndStatus(Boolean enabled, String status);

    Optional<Initiative> findByOrganizationIdAndInitiativeIdAndEnabled(String organizationId, String initiativeId, Boolean enabled);

    Optional<Initiative> findByInitiativeIdAndEnabled(String initiativeId, Boolean enabled);

    @Query(
            value="{initiativeId : ?0, enabled: ?1}",
            fields="{initiativeId : 1, " +
                    "organizationId : 1, " +
                    "initiativeName : 1, " +
                    "status : 1, " +
                    "'additionalInfo.serviceId' : 1, " +
                    "'additionalInfo.serviceScope' : 1, " +
                    "'general' : 1, " +
                    "'beneficiaryRule' : 1}")
    Optional<Initiative> retrieveInitiativeBeneficiaryView(String initiativeId, Boolean enabled);

    @Query(
            value = "{'additionalInfo.serviceId' : ?0}",
            fields = "{initiativeId : 1, 'general.descriptionMap' : 1}"
    )
    Optional<Initiative> retrieveServiceId(String serviceId);

    OrganizationDTO findFirstByOrganizationId(String organizationId);
}
