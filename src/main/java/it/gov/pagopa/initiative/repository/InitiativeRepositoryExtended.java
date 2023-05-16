package it.gov.pagopa.initiative.repository;

import it.gov.pagopa.initiative.dto.OrganizationDTO;

import java.util.List;

public interface InitiativeRepositoryExtended {

    List<OrganizationDTO> findAllBy(List<String> statusList);

}
