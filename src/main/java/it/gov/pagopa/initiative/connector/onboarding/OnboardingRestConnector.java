package it.gov.pagopa.initiative.connector.onboarding;

import it.gov.pagopa.initiative.dto.ResponseOnboardingDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;

public interface OnboardingRestConnector {

  ResponseOnboardingDTO getOnboarding(
      @PathVariable("initiativeId") String initiativeId,
      @RequestParam(required = false) Pageable pageable,
      @RequestParam(required = false) String userId,
      @RequestParam(required = false) Instant startDate,
      @RequestParam(required = false) Instant endDate,
      @RequestParam(required = false) String status);

}
