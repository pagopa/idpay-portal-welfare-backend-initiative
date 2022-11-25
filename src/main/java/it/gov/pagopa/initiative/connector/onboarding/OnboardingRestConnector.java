package it.gov.pagopa.initiative.connector.onboarding;

import it.gov.pagopa.initiative.dto.ResponseOnboardingDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

public interface OnboardingRestConnector {

  ResponseOnboardingDTO getOnboarding(
      @PathVariable("initiativeId") String initiativeId,
      @RequestParam(required = false) Pageable pageable,
      @RequestParam(required = false) String userId,
      @RequestParam(required = false) LocalDateTime startDate,
      @RequestParam(required = false) LocalDateTime endDate,
      @RequestParam(required = false) String status);

}
