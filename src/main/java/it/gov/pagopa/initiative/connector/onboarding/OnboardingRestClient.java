package it.gov.pagopa.initiative.connector.onboarding;

import it.gov.pagopa.initiative.dto.ResponseOnboardingDTO;
import java.time.LocalDateTime;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(
    name = "${rest-client.onboarding.serviceCode}",
    url = "${onboarding.uri}")
public interface OnboardingRestClient {

  @GetMapping(
      value = "/idpay/onboarding/{initiativeId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  ResponseOnboardingDTO getOnboarding(
      @PathVariable("initiativeId") String initiativeId,
      @RequestParam(required = false) Pageable pageable,
      @RequestParam(required = false) String userId,
      @RequestParam(required = false) LocalDateTime startDate,
      @RequestParam(required = false) LocalDateTime endDate,
      @RequestParam(required = false) String status);

}