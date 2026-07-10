package it.gov.pagopa.initiative.repository;

import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.dto.InitiativePageItem;
import it.gov.pagopa.initiative.dto.OrganizationDTO;
import it.gov.pagopa.initiative.model.Initiative;
import it.gov.pagopa.initiative.model.InitiativeGeneral;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.IntStream;

@DataMongoTest(properties = {
        "spring.data.mongodb.database=test",
        "spring.data.mongodb.port=0",
        "de.flapdoodle.mongodb.embedded.version=4.2.24"
})
class InitiativeRepositoryExtendedImplTest {

    private static final int DATA_LIST_SIZE = 4;

    @Autowired
    private InitiativeRepository initiativeRepository;

    @AfterEach
    void cleanData() {
        initiativeRepository.deleteAll();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            InitiativeConstants.Role.ADMIN,
            InitiativeConstants.Role.PAGOPA_ADMIN,
            "default"
    })
    void test(String role) {

        initiativeRepository.saveAll(createInitiativeList());

        List<OrganizationDTO> result =
                initiativeRepository.findAllBy(createStatusList(role));

        List<OrganizationDTO> expectedResult = role.equals("default")
                ? Collections.emptyList()
                : createOrganizationDTOList(role);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(
                new HashSet<>(expectedResult),
                new HashSet<>(result)
        );
    }

    @Test
    void shouldReturnOnlyPublishedInitiatives() {

        initiativeRepository.saveAll(List.of(
                createInitiative(
                        100,
                        "PUBLISHED",
                        List.of("1111"),
                        LocalDate.now().plusYears(1)
                ),
                createInitiative(
                        101,
                        InitiativeConstants.Status.DRAFT,
                        List.of("1111"),
                        LocalDate.now().plusYears(1)
                )
        ));

        Page<InitiativePageItem> result = initiativeRepository.findInitiatives(
                Collections.emptySet(),
                List.of("1111"),
                null,
                PageRequest.of(0, 10)
        );

        Assertions.assertEquals(1, result.getTotalElements());

        InitiativePageItem item = result.getContent().get(0);

        Assertions.assertEquals(
                "initiativeId_100",
                item.getInitiativeId()
        );
        Assertions.assertEquals(
                "PUBLISHED",
                item.getStatus()
        );
    }

    @Test
    void shouldExcludeAlreadyOnboardedInitiatives() {

        Initiative first = createInitiativeForFindInitiatives(
                200,
                "PUBLISHED",
                List.of("1111"),
                LocalDate.now().plusYears(1)
        );

        Initiative second = createInitiativeForFindInitiatives(
                201,
                "PUBLISHED",
                List.of("1111"),
                LocalDate.now().plusYears(1)
        );

        initiativeRepository.saveAll(List.of(first, second));

        Page<InitiativePageItem> result = initiativeRepository.findInitiatives(
                Set.of(first.getInitiativeId()),
                List.of("1111"),
                null,
                PageRequest.of(0, 10)
        );

        Assertions.assertEquals(1, result.getContent().size());

        Assertions.assertEquals(
                second.getInitiativeId(),
                result.getContent().get(0).getInitiativeId()
        );
    }

    @Test
    void shouldFilterByInitiativeNameIgnoringCase() {

        initiativeRepository.saveAll(List.of(
                createInitiative(
                        300,
                        "PUBLISHED",
                        List.of("1111"),
                        LocalDate.now().plusYears(1)
                ),
                createInitiative(
                        301,
                        "PUBLISHED",
                        List.of("1111"),
                        LocalDate.now().plusYears(1)
                )
        ));

        Page<InitiativePageItem> result = initiativeRepository.findInitiatives(
                Collections.emptySet(),
                List.of("1111"),
                "NAME_300",
                PageRequest.of(0, 10)
        );

        Assertions.assertEquals(1, result.getContent().size());

        Assertions.assertEquals(
                "initiativeId_300",
                result.getContent().get(0).getInitiativeId()
        );
    }

    @Test
    void shouldMarkInitiativeAsOnboardable() {

        initiativeRepository.save(
                createInitiative(
                        400,
                        "PUBLISHED",
                        List.of("ATECO1", "ATECO2"),
                        LocalDate.now().plusYears(1)
                )
        );

        Page<InitiativePageItem> result = initiativeRepository.findInitiatives(
                Collections.emptySet(),
                List.of("ATECO2"),
                null,
                PageRequest.of(0, 10)
        );

        Assertions.assertEquals(1, result.getContent().size());

        InitiativePageItem item = result.getContent().get(0);

        Assertions.assertEquals(
                "ONBOARDABLE",
                item.getOnboardStatus()
        );

        Assertions.assertEquals(
                0,
                item.getOnboardStatusOrder()
        );
    }

    @Test
    void shouldMarkExpiredInitiativeAsNotOnboardable() {

        initiativeRepository.save(
                createInitiative(
                        500,
                        "PUBLISHED",
                        List.of("ATECO1"),
                        LocalDate.now().minusYears(1)
                )
        );

        Page<InitiativePageItem> result = initiativeRepository.findInitiatives(
                Collections.emptySet(),
                List.of("ATECO1"),
                null,
                PageRequest.of(0, 10)
        );

        Assertions.assertEquals(1, result.getContent().size());

        InitiativePageItem item = result.getContent().get(0);

        Assertions.assertEquals(
                "NOT_ONBOARDABLE",
                item.getOnboardStatus()
        );

        Assertions.assertEquals(
                1,
                item.getOnboardStatusOrder()
        );
    }

    @Test
    void shouldPrioritizeExpiredOverAtecoMatch() {

        initiativeRepository.save(
                createInitiative(
                        600,
                        "PUBLISHED",
                        List.of("ATECO1", "ATECO2"),
                        LocalDate.now().minusYears(1)
                )
        );

        Page<InitiativePageItem> result = initiativeRepository.findInitiatives(
                Collections.emptySet(),
                List.of("ATECO2"),
                null,
                PageRequest.of(0, 10)
        );

        Assertions.assertEquals(1, result.getContent().size());

        InitiativePageItem item = result.getContent().get(0);

        Assertions.assertEquals(
                "NOT_ONBOARDABLE",
                item.getOnboardStatus()
        );

        Assertions.assertEquals(
                1,
                item.getOnboardStatusOrder()
        );
    }
    @Test
    void shouldHandleNullAtecoCodes() {

        initiativeRepository.save(
                createInitiative(
                        700,
                        InitiativeConstants.Status.PUBLISHED,
                        List.of("ATECO1"),
                        LocalDate.now().plusYears(1)
                )
        );

        Page<InitiativePageItem> result =
                initiativeRepository.findInitiatives(
                        Collections.emptySet(),
                        null,
                        null,
                        PageRequest.of(0, 10)
                );

        Assertions.assertEquals(1, result.getTotalElements());
    }

    @Test
    void shouldIgnoreBlankInitiativeNameFilter() {

        initiativeRepository.save(
                createInitiative(
                        701,
                        InitiativeConstants.Status.PUBLISHED,
                        List.of("1111"),
                        LocalDate.now().plusYears(1)
                )
        );

        Page<InitiativePageItem> result =
                initiativeRepository.findInitiatives(
                        Collections.emptySet(),
                        List.of("1111"),
                        "   ",
                        PageRequest.of(0, 10)
                );

        Assertions.assertEquals(1, result.getTotalElements());
    }

    @Test
    void shouldSortByOrganizationName() {

        initiativeRepository.saveAll(List.of(
                createInitiative(
                        702,
                        InitiativeConstants.Status.PUBLISHED,
                        List.of("1111"),
                        LocalDate.now().plusYears(1)
                ),
                createInitiative(
                        703,
                        InitiativeConstants.Status.PUBLISHED,
                        List.of("1111"),
                        LocalDate.now().plusYears(1)
                )
        ));

        Page<InitiativePageItem> result =
                initiativeRepository.findInitiatives(
                        Collections.emptySet(),
                        List.of("1111"),
                        null,
                        PageRequest.of(
                                0,
                                10,
                                org.springframework.data.domain.Sort.by("organizationName")
                        )
                );

        Assertions.assertEquals(2, result.getTotalElements());
    }

    @Test
    void shouldThrowExceptionForUnsupportedSortProperty() {

        PageRequest pageRequest = PageRequest.of(
                0,
                10,
                Sort.by("unsupportedField")
        );

        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> initiativeRepository.findInitiatives(
                        Collections.emptySet(),
                        List.of(),
                        null,
                        pageRequest
                )
        );
    }

    private Initiative createInitiativeForFindInitiatives(
            int bias,
            String status,
            List<String> atecoCodes,
            LocalDate endDate) {

        LocalDateTime now = LocalDateTime.now();

        return Initiative.builder()
                .initiativeId(new ObjectId().toHexString())
                .initiativeName("initiativeName_%d".formatted(bias))
                .organizationId("organizationId_%d".formatted(bias))
                .organizationName("organizationName_%d".formatted(bias))
                .creationDate(now.minusWeeks(2))
                .updateDate(now)
                .status(status)
                .enabled(true)
                .atecoCodes(atecoCodes)
                .general(
                        InitiativeGeneral.builder()
                                .endDate(endDate)
                                .build()
                )
                .build();
    }

    private Initiative createInitiative(
            int bias,
            String status,
            List<String> atecoCodes,
            LocalDate endDate) {

        LocalDateTime now = LocalDateTime.now();

        return Initiative.builder()
                .initiativeId("initiativeId_%d".formatted(bias))
                .initiativeName("initiativeName_%d".formatted(bias))
                .organizationId("organizationId_%d".formatted(bias))
                .organizationName("organizationName_%d".formatted(bias))
                .creationDate(now.minusWeeks(2))
                .updateDate(now)
                .status(status)
                .enabled(true)
                .atecoCodes(atecoCodes)
                .general(
                        InitiativeGeneral.builder()
                                .endDate(endDate)
                                .build()
                )
                .build();
    }

    private List<Initiative> createInitiativeList() {
        return IntStream.range(0, DATA_LIST_SIZE)
                .mapToObj(this::createInitiative)
                .toList();
    }

    private Initiative createInitiative(int bias) {

        LocalDateTime now = LocalDateTime.now();

        return Initiative.builder()
                .initiativeId("initiativeId_%d".formatted(bias))
                .initiativeName("initiativeName_%d".formatted(bias))
                .organizationId("organizationId_%d".formatted(bias))
                .organizationName("organizationName_%d".formatted(bias))
                .creationDate(now.minusWeeks(2))
                .updateDate(now)
                .status(
                        bias % 2 == 0
                                ? InitiativeConstants.Status.APPROVED
                                : InitiativeConstants.Status.DRAFT
                )
                .enabled(true)
                .build();
    }

    private List<String> createStatusList(String role) {
        return switch (role) {
            case InitiativeConstants.Role.ADMIN ->
                    InitiativeConstants.Status.INITIATIVE_STATUS_LIST_FOR_ADMIN_OPERATOR;
            case InitiativeConstants.Role.PAGOPA_ADMIN ->
                    InitiativeConstants.Status.INITIATIVE_STATUS_LIST_FOR_PAGOPA_ADMIN_OPERATOR;
            default -> Collections.emptyList();
        };
    }

    private List<OrganizationDTO> createOrganizationDTOList(String role) {

        List<OrganizationDTO> list = new ArrayList<>(
                IntStream.range(0, DATA_LIST_SIZE)
                        .mapToObj(this::createOrganizationDTO)
                        .toList()
        );

        for (int i = list.size() - 1; i >= 0; i--) {
            if (role.equals(InitiativeConstants.Role.PAGOPA_ADMIN) && i % 2 != 0) {
                list.remove(list.get(i));
            }
        }

        for (int i = 0, j = list.size() - 1; i < j; i++) {
            list.add(i, list.remove(j));
        }

        return list;
    }

    private OrganizationDTO createOrganizationDTO(int bias) {

        return OrganizationDTO.builder()
                .organizationId("organizationId_%d".formatted(bias))
                .organizationName("organizationName_%d".formatted(bias))
                .build();
    }
}