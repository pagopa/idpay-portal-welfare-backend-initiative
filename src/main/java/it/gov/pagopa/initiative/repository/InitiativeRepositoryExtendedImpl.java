package it.gov.pagopa.initiative.repository;

import it.gov.pagopa.initiative.dto.OrganizationDTO;
import it.gov.pagopa.initiative.model.Initiative;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class InitiativeRepositoryExtendedImpl implements InitiativeRepositoryExtended {

    private final MongoTemplate mongoTemplate;

    public InitiativeRepositoryExtendedImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<OrganizationDTO> findAllBy(List<String> statusList) {
        Query query = new Query().addCriteria(getCriteria(statusList));
        query = query.with(Pageable.unpaged());

        return mongoTemplate.findDistinct(query, "organizationName", Initiative.class, OrganizationDTO.class);
    }

    private Criteria getCriteria(List<String> statusList) {
        return Criteria.where("status").in(statusList);
    }
}
