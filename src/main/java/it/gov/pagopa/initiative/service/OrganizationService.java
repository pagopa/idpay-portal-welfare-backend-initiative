package it.gov.pagopa.initiative.service;

import it.gov.pagopa.initiative.dto.OrganizationDTO;

import java.util.List;

public interface OrganizationService {

    List<OrganizationDTO> getOrganizationList(String role);

    OrganizationDTO getOrganization(String organizationId);
}
