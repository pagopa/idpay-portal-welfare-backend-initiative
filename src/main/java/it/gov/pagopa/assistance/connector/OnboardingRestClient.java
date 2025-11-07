package it.gov.pagopa.assistance.connector;


import it.gov.pagopa.assistance.dto.request.OnboardingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
    name = "${assistance.rest-client.onboarding.serviceCode}",
    url = "${assistance.rest-client.onboarding.uri}")
public interface OnboardingRestClient {

  @GetMapping(
      value = "/idpay/onboarding/{initiativeId}/{userId}/assistance",
      produces = MediaType.APPLICATION_JSON_VALUE)
  ResponseEntity<OnboardingDTO> onboardingStatus(
          @PathVariable("initiativeId") String initiativeId, @PathVariable("userId") String userId);


}