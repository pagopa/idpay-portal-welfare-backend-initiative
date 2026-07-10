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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Slf4j
public class InitiativeRepositoryExtendedImpl implements InitiativeRepositoryExtended {

    private static final String STATUS = "status";
    private static final String PUBLISHED = "PUBLISHED";

    private static final String ID = "_id";
    private static final String INITIATIVE_NAME = "initiativeName";
    private static final String ORGANIZATION_NAME = "organizationName";
    private static final String ATECO_CODES = "atecoCodes";

    private static final String END_DATE = "$general.endDate";

    private static final String ONBOARD_STATUS = "onboardStatus";
    private static final String ONBOARD_STATUS_ORDER = "onboardStatusOrder";

    private static final String ONBOARDABLE = "ONBOARDABLE";
    private static final String NOT_ONBOARDABLE = "NOT_ONBOARDABLE";

    private static final String INITIATIVE_COLLECTION = "initiative";

    private final MongoTemplate mongoTemplate;

    public InitiativeRepositoryExtendedImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<OrganizationDTO> findAllBy(List<String> statusList) {
        log.trace("Building Aggregation Query...");

        AggregationOperation match =
                Aggregation.match(getCriteria(statusList));

        AggregationOperation group =
                Aggregation.group(
                        Fields.fields(
                                Initiative.Fields.organizationId,
                                Initiative.Fields.organizationName
                        )
                );

        AggregationOperation project =
                Aggregation.project(
                        Fields.fields(
                                Initiative.Fields.organizationId,
                                Initiative.Fields.organizationName
                        )
                );

        Aggregation aggregation =
                Aggregation.newAggregation(
                        match,
                        group,
                        project
                );

        log.trace("Aggregation Query built. Starting to query DB");

        AggregationResults<OrganizationDTO> results =
                mongoTemplate.aggregate(
                        aggregation,
                        Initiative.class,
                        OrganizationDTO.class
                );

        log.trace("Query has been done");

        return results.getMappedResults();
    }

    private Criteria getCriteria(List<String> statusList) {
        log.trace("Building Criteria...");

        return Criteria.where(Initiative.Fields.enabled)
                .is(true)
                .andOperator(
                        Criteria.where(Initiative.Fields.status)
                                .in(statusList)
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

        List<String> safeAtecoCodes =
                atecoCodes != null
                        ? atecoCodes
                        : Collections.emptyList();

        Criteria criteria = Criteria.where(STATUS)
                .is(PUBLISHED)
                .and(ID)
                .not()
                .in(safeOnboardedIds);

        if (initiativeName != null && !initiativeName.isBlank()) {
            Pattern pattern = Pattern.compile(
                    Pattern.quote(initiativeName),
                    Pattern.CASE_INSENSITIVE
            );

            criteria = criteria.and(INITIATIVE_NAME)
                    .regex(pattern);
        }

        MatchOperation match = Aggregation.match(criteria);

        AggregationExpression hasAtecoMatch =
                context -> new Document(
                        "$gt",
                        List.of(
                                new Document(
                                        "$size",
                                        new Document(
                                                "$setIntersection",
                                                List.of(
                                                        "$" + ATECO_CODES,
                                                        safeAtecoCodes
                                                )
                                        )
                                ),
                                0
                        )
                );

        AggregationExpression isExpired =
                context -> new Document(
                        "$and",
                        List.of(
                                new Document(
                                        "$ne",
                                        Arrays.asList(END_DATE, null)
                                ),
                                new Document(
                                        "$lt",
                                        List.of(
                                                END_DATE,
                                                "$$NOW"
                                        )
                                )
                        )
                );

        ProjectionOperation project = Aggregation.project()
                .andInclude(
                        ID,
                        INITIATIVE_NAME,
                        STATUS,
                        ORGANIZATION_NAME,
                        ATECO_CODES
                )

                .and(
                        ConditionalOperators.switchCases(
                                        ConditionalOperators.Switch.CaseOperator
                                                .when(isExpired)
                                                .then(NOT_ONBOARDABLE),

                                        ConditionalOperators.Switch.CaseOperator
                                                .when(hasAtecoMatch)
                                                .then(ONBOARDABLE)
                                )
                                .defaultTo(NOT_ONBOARDABLE)
                )
                .as(ONBOARD_STATUS)

                .and(
                        ConditionalOperators.switchCases(
                                        ConditionalOperators.Switch.CaseOperator
                                                .when(isExpired)
                                                .then(1),

                                        ConditionalOperators.Switch.CaseOperator
                                                .when(hasAtecoMatch)
                                                .then(0)
                                )
                                .defaultTo(1)
                )
                .as(ONBOARD_STATUS_ORDER);

        Sort userSort = pageable.getSort().isUnsorted()
                ? Sort.by(Sort.Order.asc(INITIATIVE_NAME))
                : Sort.by(
                pageable.getSort().stream()
                        .filter(order ->
                                !order.getProperty()
                                        .equals(ONBOARD_STATUS))
                        .map(order -> {
                            String field = switch (order.getProperty()) {
                                case INITIATIVE_NAME -> INITIATIVE_NAME;
                                case ORGANIZATION_NAME -> ORGANIZATION_NAME;
                                default -> throw new IllegalArgumentException(
                                        "Unsupported sort property: "
                                                + order.getProperty()
                                );
                            };

                            return new Sort.Order(
                                    order.getDirection(),
                                    field
                            );
                        })
                        .toList()
        );

        Sort sort = Sort.by(
                Sort.Order.asc(ONBOARD_STATUS_ORDER)
        ).and(userSort);

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
                                INITIATIVE_COLLECTION,
                                InitiativePageItem.class
                        )
                        .getMappedResults();

        long total = mongoTemplate.count(
                Query.query(criteria),
                Initiative.class
        );

        return new PageImpl<>(content, pageable, total);
    }
}