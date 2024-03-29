package it.gov.pagopa.initiative.repository;

import it.gov.pagopa.common.mongo.singleinstance.AutoConfigureSingleInstanceMongodb;
import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.dto.OrganizationDTO;
import it.gov.pagopa.initiative.model.Initiative;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

@DataMongoTest
@TestPropertySource(properties = {
        "de.flapdoodle.mongodb.embedded.version=4.2.24",
})
@AutoConfigureSingleInstanceMongodb
class InitiativeRepositoryExtendedImplTest {

    private static final int DATA_LIST_SIZE = 4;

    @Autowired
    private InitiativeRepository initiativeRepository;

    private final List<Initiative> testData = createInitiativeList();

    @BeforeEach
    void prepareData() {
        initiativeRepository.saveAll(testData);
    }

    @AfterEach
    void cleanData() {
        initiativeRepository.deleteAll(testData);
    }


    @ParameterizedTest
    @ValueSource(strings = {InitiativeConstants.Role.ADMIN, InitiativeConstants.Role.PAGOPA_ADMIN, "default"})
    void test(String role) {
        List<OrganizationDTO> result = initiativeRepository.findAllBy(createStatusList(role));

        List<OrganizationDTO> expectedResult = role.equals("default")
                ? Collections.emptyList()
                : createOrganizationDTOList(role);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(new HashSet<>(expectedResult), new HashSet<>(result));
    }

    private List<Initiative> createInitiativeList() {
        return IntStream.range(0, DATA_LIST_SIZE).mapToObj(this::createInitiative).toList();
    }

    private Initiative createInitiative(int bias) {
        LocalDateTime now = LocalDateTime.now();

        return Initiative.builder()
                .initiativeId("initiativeid_%d".formatted(bias))
                .initiativeName("initiativeName_%d".formatted(bias))
                .organizationId("organizationId_%d".formatted(bias))
                .organizationName("organizationName_%d".formatted(bias))
                .creationDate(now.minusWeeks(2))
                .updateDate(now)
                .status(bias % 2 == 0
                        ? InitiativeConstants.Status.APPROVED
                        : InitiativeConstants.Status.DRAFT)
                .enabled(true)
                .build();
    }

    private List<String> createStatusList(String role) {
        return switch (role) {
            case InitiativeConstants.Role.ADMIN -> InitiativeConstants.Status.INITIATIVE_STATUS_LIST_FOR_ADMIN_OPERATOR;
            case InitiativeConstants.Role.PAGOPA_ADMIN ->
                    InitiativeConstants.Status.INITIATIVE_STATUS_LIST_FOR_PAGOPA_ADMIN_OPERATOR;
            default -> Collections.emptyList();
        };
    }

    private List<OrganizationDTO> createOrganizationDTOList(String role) {
        List<OrganizationDTO> list = new ArrayList<>(
                IntStream.range(0, DATA_LIST_SIZE).mapToObj(this::createOrganizationDTO).toList()
        );

        // Remove results that only ADMINs can see
        for (int i = list.size()-1; i >= 0; i--) {
            if(role.equals(InitiativeConstants.Role.PAGOPA_ADMIN) && i%2!=0) {
                list.remove(list.get(i));
            }
        }

        //Reverse list order to match Aggregation result
        for(int i = 0, j = list.size() - 1; i < j; i++) {
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