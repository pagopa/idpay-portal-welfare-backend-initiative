package it.gov.pagopa.initiative.repository;

import it.gov.pagopa.initiative.dto.InitiativePageItem;
import it.gov.pagopa.initiative.dto.OrganizationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface InitiativeRepositoryExtended {

    List<OrganizationDTO> findAllBy(List<String> statusList);

    Page<InitiativePageItem> findInitiatives(
            Set<String> onboardedIds,
            List<String> atecoCodes,
            String initiativeName,
            Pageable pageable);

}
