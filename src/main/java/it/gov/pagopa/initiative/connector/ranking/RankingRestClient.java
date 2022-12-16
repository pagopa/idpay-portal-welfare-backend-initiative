package it.gov.pagopa.initiative.connector.ranking;

import it.gov.pagopa.initiative.dto.RankingPageDTO;
import it.gov.pagopa.initiative.dto.RankingRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(
    name = "${rest-client.ranking.serviceCode}",
    url = "${rest-client.ranking.uri}")
public interface RankingRestClient {

  @GetMapping(
      value = "/idpay/ranking/organization/{organizationId}/initiative/{initiativeId}/paged",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  RankingPageDTO getRankingList(
      @PathVariable("organizationId") String organizationId,
      @PathVariable("initiativeId") String initiativeId,
      Pageable pageable,
      @RequestParam(required = false) String beneficiaryRankingStatus,
      @RequestParam(required = false) String userId);

}