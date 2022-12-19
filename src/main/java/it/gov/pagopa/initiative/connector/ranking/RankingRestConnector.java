package it.gov.pagopa.initiative.connector.ranking;

import it.gov.pagopa.initiative.dto.RankingPageDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public interface RankingRestConnector {

  RankingPageDTO getRankingList(
      @PathVariable("organizationId") String organizationId,
      @PathVariable("initiativeId") String initiativeId,
      @RequestParam(required = false) Pageable pageable,
      @RequestParam(required = false) String beneficiaryRankingStatus,
      @RequestParam(required = false) String userId);

}
