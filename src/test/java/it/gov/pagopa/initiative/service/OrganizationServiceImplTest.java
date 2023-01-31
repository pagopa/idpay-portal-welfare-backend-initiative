package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.dto.OrganizationDTO;
import it.gov.pagopa.initiative.repository.InitiativeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceImplTest {

    @Mock
    private InitiativeRepository initiativeRepository;

    private OrganizationService organizationService;

    @BeforeEach
    void init() {
        organizationService = new OrganizationServiceImpl(initiativeRepository);
    }

    @ParameterizedTest
    @ValueSource(strings = {InitiativeConstants.Role.ADMIN, InitiativeConstants.Role.OPE_BASE, "default"})
    void getListOrganization(String role) {
        List<String> statusList = switch (role) {
            case InitiativeConstants.Role.ADMIN ->
                    InitiativeConstants.Status.INITIATIVE_STATUS_LIST_FOR_ADMIN_OPERATOR;
            case InitiativeConstants.Role.OPE_BASE ->
                    InitiativeConstants.Status.INITIATIVE_STATUS_LIST_FOR_PAGO_PA_OPERATOR;
            default -> null;
        };

        List<OrganizationDTO> organizationDTOList = statusList != null
                ? createOrganizationDTOList()
                : null;
        Mockito.lenient().when(initiativeRepository.findAllBy(statusList)).thenReturn(organizationDTOList);

        List<OrganizationDTO> result = organizationService.getOrganizationList(role);

        Assertions.assertEquals(organizationDTOList, result);
    }

    @Test
    void getSingleOrganization() {
        OrganizationDTO organizationDTO = createOrganizationDTO(1);
        Mockito.when(initiativeRepository.findFirstByOrganizationId("orgId")).thenReturn(organizationDTO);

        OrganizationDTO result = organizationService.getOrganization("orgId");

        Assertions.assertEquals(organizationDTO, result);
    }

    private List<OrganizationDTO> createOrganizationDTOList() {
        return IntStream.range(0, 4).mapToObj(this::createOrganizationDTO).toList();
    }

    private OrganizationDTO createOrganizationDTO(int bias) {
        return OrganizationDTO.builder()
                .organizationId("organizationId_%d".formatted(bias))
                .organizationName("organizationName_%d".formatted(bias))
                .build();
    }
}