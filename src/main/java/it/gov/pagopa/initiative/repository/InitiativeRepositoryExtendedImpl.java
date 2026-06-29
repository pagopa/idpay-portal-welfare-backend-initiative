package it.gov.pagopa.initiative.repository;

import it.gov.pagopa.initiative.dto.InitiativePageItem;
import it.gov.pagopa.initiative.dto.OrganizationDTO;
import it.gov.pagopa.initiative.model.Initiative;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;
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
import java.util.regex.Pattern;

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
            String initiativeName,
            Pageable pageable) {

        List<ObjectId> safeOnboardedIds = onboardedIds.stream()
                .map(ObjectId::new)
                .toList();

        List<String> safeAtecoCodes = atecoCodes != null ? atecoCodes : Collections.emptyList();

        Criteria criteria = Criteria.where("status").is("PUBLISHED");

        if (initiativeName != null && !initiativeName.isBlank()) {
            Pattern pattern = Pattern.compile(
                    Pattern.quote(initiativeName),
                    Pattern.CASE_INSENSITIVE
            );
            criteria = criteria.and("initiativeName").regex(pattern);
        }

        MatchOperation match = Aggregation.match(criteria);

        AggregationExpression isOnboarded =
                ArrayOperators.In.arrayOf(safeOnboardedIds)
                        .containsValue(Fields.field("_id"));

        AggregationExpression hasAtecoMatch = context -> new Document("$gt",
                List.of(
                        new Document("$size",
                                new Document("$setIntersection",
                                        List.of("$atecoCodes", safeAtecoCodes))),
                        0
                ));


        AggregationExpression isExpired = context -> new Document("$and",
                List.of(
                        new Document("$ne", List.of("$general.endDate", null)),
                        new Document("$lt", List.of("$general.endDate", "$$NOW"))
                ));

        ProjectionOperation project = Aggregation.project()
                .and("general.startDate").as("startDate")
                .and("general.endDate").as("endDate")
                .andInclude("_id", "initiativeName", "status","organizationName","atecoCodes")


                .and(
                        ConditionalOperators.switchCases(

                                        ConditionalOperators.Switch.CaseOperator
                                                .when(isExpired)
                                                .then("NON_ONBOARDABILE"),

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
                                                .when(isExpired)
                                                .then(1),

                                        ConditionalOperators.Switch.CaseOperator
                                                .when(isOnboarded)
                                                .then(2),

                                        ConditionalOperators.Switch.CaseOperator
                                                .when(hasAtecoMatch)
                                                .then(0)
                                )
                                .defaultTo(1)
                ).as("onboardStatusOrder");

        Sort userSort = pageable.getSort().isUnsorted()
                ? Sort.by(Sort.Order.asc("initiativeName"))
                : Sort.by(
                pageable.getSort().stream()
                        .filter(order -> !order.getProperty().equals("onboardStatus"))
                        .map(order -> {
                            String field = switch (order.getProperty()) {
                                case "initiativeName" -> "initiativeName";
                                case "organizationName" -> "organizationName";
                                default -> throw new IllegalArgumentException(
                                        "Unsupported sort property: " + order.getProperty());
                            };
                            return new Sort.Order(order.getDirection(), field);
                        }).toList()
        );

        Sort sort = Sort.by(Sort.Order.asc("onboardStatusOrder")).and(userSort);

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
                Query.query(criteria),
                Initiative.class
        );

        return new PageImpl<>(content, pageable, total);
    }
}