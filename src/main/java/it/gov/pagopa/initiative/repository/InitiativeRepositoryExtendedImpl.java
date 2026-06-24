package it.gov.pagopa.initiative.repository;

import it.gov.pagopa.initiative.dto.InitiativePageItem;
import it.gov.pagopa.initiative.dto.OrganizationDTO;
import it.gov.pagopa.initiative.model.Initiative;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
public class InitiativeRepositoryExtendedImpl implements InitiativeRepositoryExtended {

    private final MongoTemplate mongoTemplate;

    public InitiativeRepositoryExtendedImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<OrganizationDTO> findAllBy(List<String> statusList) {
        log.trace("Building Aggregation Query...");
        AggregationOperation match = Aggregation.match(getCriteria(statusList));
        AggregationOperation group = Aggregation.group(Fields.fields(Initiative.Fields.organizationId, Initiative.Fields.organizationName));
        AggregationOperation project = Aggregation.project(Fields.fields(Initiative.Fields.organizationId, Initiative.Fields.organizationName));

        Aggregation aggregation = Aggregation.newAggregation(match, group, project);
        log.trace("Aggregation Query built. Starting to query DB");
        AggregationResults<OrganizationDTO> results = mongoTemplate.aggregate(aggregation, Initiative.class, OrganizationDTO.class);
        log.trace("Query has been done");

        return results.getMappedResults();
    }

    private Criteria getCriteria(List<String> statusList) {
        log.trace("Building Criteria...");
        return Criteria.where(Initiative.Fields.enabled).is(true)
                .andOperator(
                        Criteria.where(Initiative.Fields.status).in(statusList)
                );
    }

    @Override
    public Page<InitiativePageItem> findInitiatives(
            Set<String> onboardedIds,
            List<String> atecoCodes,
            Pageable pageable) {

        Set<String> safeOnboardedIds = onboardedIds != null ? onboardedIds : Collections.emptySet();
        List<String> safeAtecoCodes = atecoCodes != null ? atecoCodes : Collections.emptyList();

        // MATCH
        MatchOperation match = Aggregation.match(
                Criteria.where("status").is("PUBLISHED")
        );

        // "initiativeId" è contenuto in onboardedIds  ->  { $in: ["$initiativeId", [...]] }
        AggregationExpression isOnboarded = context -> new Document("$in",
                List.of("$initiativeId", safeOnboardedIds));

        // size( $setIntersection: ["$atecoCodes", [...]] ) > 0
        AggregationExpression hasAtecoMatch = context -> new Document("$gt",
                List.of(
                        new Document("$size",
                                new Document("$setIntersection",
                                        List.of("$atecoCodes", safeAtecoCodes))),
                        0
                ));

        // PROJECT
        ProjectionOperation project = Aggregation.project()
                .and("general.startDate").as("startDate")
                .and("general.endDate").as("endDate")
                .andInclude("_id","initiativeName", "status")

                .and(
                        ConditionalOperators.switchCases(

                                        ConditionalOperators.Switch.CaseOperator
                                                .when(isOnboarded)
                                                .then("ONBOARDATO"),

                                        ConditionalOperators.Switch.CaseOperator
                                                .when(hasAtecoMatch)
                                                .then("ONBOARDABILE")
                                )
                                .defaultTo("NON_ONBOARDABILE")
                ).as("onboardStatus")

                .and(
                        ConditionalOperators.switchCases(

                                        ConditionalOperators.Switch.CaseOperator
                                                .when(isOnboarded)
                                                .then(0),

                                        ConditionalOperators.Switch.CaseOperator
                                                .when(hasAtecoMatch)
                                                .then(1)
                                )
                                .defaultTo(2)
                ).as("onboardStatusOrder");

        // SORT MAPPING
        Sort sort = pageable.getSort().isUnsorted()
                ? Sort.by("initiativeName")
                : Sort.by(
                pageable.getSort().stream()
                        .map(order -> {
                            String field = switch (order.getProperty()) {
                                case "initiativeName" -> "initiativeName";
                                case "status" -> "status";
                                case "startDate" -> "startDate";
                                case "onboardStatus" -> "onboardStatusOrder";
                                default -> throw new IllegalArgumentException(
                                        "Unsupported sort property: " + order.getProperty());
                            };
                            return new Sort.Order(order.getDirection(), field);
                        }).toList()
        );

        SortOperation sortOp = Aggregation.sort(sort);
        SkipOperation skip = Aggregation.skip(pageable.getOffset());
        LimitOperation limit = Aggregation.limit(pageable.getPageSize());

        Aggregation aggregation = Aggregation.newAggregation(
                match,
                project,
                sortOp,
                skip,
                limit
        );

        List<InitiativePageItem> content =
                mongoTemplate.aggregate(
                        aggregation,
                        "initiative",
                        InitiativePageItem.class
                ).getMappedResults();

        long total = mongoTemplate.count(
                Query.query(Criteria.where("status").is("PUBLISHED")),
                Initiative.class
        );

        return new PageImpl<>(content, pageable, total);
    }
}
