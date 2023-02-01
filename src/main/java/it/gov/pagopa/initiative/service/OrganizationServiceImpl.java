package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.constants.InitiativeConstants;
import it.gov.pagopa.initiative.dto.OrganizationDTO;
import it.gov.pagopa.initiative.repository.InitiativeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.gov.pagopa.initiative.constants.InitiativeConstants.Status.INITIATIVE_STATUS_LIST_FOR_ADMIN_OPERATOR;
import static it.gov.pagopa.initiative.constants.InitiativeConstants.Status.INITIATIVE_STATUS_LIST_FOR_PAGOPA_ADMIN_OPERATOR;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    private final InitiativeRepository initiativeRepository;

    public OrganizationServiceImpl(
            InitiativeRepository initiativeRepository
    ) {
        this.initiativeRepository = initiativeRepository;
    }

    @Override
    public List<OrganizationDTO> getOrganizationList(String role) {
        return switch (role) {
            case InitiativeConstants.Role.PAGOPA_ADMIN ->
                    initiativeRepository.findAllBy(INITIATIVE_STATUS_LIST_FOR_PAGOPA_ADMIN_OPERATOR);
            case InitiativeConstants.Role.ADMIN ->
                    initiativeRepository.findAllBy(INITIATIVE_STATUS_LIST_FOR_ADMIN_OPERATOR);
            default -> null;
        };
    }

    @Override
    public OrganizationDTO getOrganization(String organizationId) {
        return initiativeRepository.findFirstByOrganizationId(organizationId);
    }
}
