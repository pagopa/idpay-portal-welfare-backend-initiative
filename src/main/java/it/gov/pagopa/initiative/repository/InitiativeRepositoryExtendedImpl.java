package it.gov.pagopa.initiative.repository;

import it.gov.pagopa.initiative.dto.OrganizationDTO;
import it.gov.pagopa.initiative.model.Initiative;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

public class InitiativeRepositoryExtendedImpl implements InitiativeRepositoryExtended {

    private final MongoTemplate mongoTemplate;

    public InitiativeRepositoryExtendedImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<OrganizationDTO> findAllBy(List<String> statusList) {
        AggregationOperation match = Aggregation.match(getCriteria(statusList));
        AggregationOperation group = Aggregation.group(Fields.fields(Initiative.Fields.organizationId, Initiative.Fields.organizationName));
        AggregationOperation project = Aggregation.project(Fields.fields(Initiative.Fields.organizationId, Initiative.Fields.organizationName));

        Aggregation aggregation = Aggregation.newAggregation(match, group, project);
        AggregationResults<OrganizationDTO> results = mongoTemplate.aggregate(aggregation, Initiative.class, OrganizationDTO.class);

        return results.getMappedResults();
    }

    private Criteria getCriteria(List<String> statusList) {
        return Criteria.where(Initiative.Fields.enabled).is(true)
                .andOperator(
                        Criteria.where(Initiative.Fields.status).in(statusList)
                );
    }
}
