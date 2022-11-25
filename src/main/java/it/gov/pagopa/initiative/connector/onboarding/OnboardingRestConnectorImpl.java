package it.gov.pagopa.initiative.connector.onboarding;

import it.gov.pagopa.initiative.dto.ResponseOnboardingDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OnboardingRestConnectorImpl implements OnboardingRestConnector {

  private final OnboardingRestClient onboardingRestClient;

  public OnboardingRestConnectorImpl(
      OnboardingRestClient onboardingRestClient) {
    this.onboardingRestClient = onboardingRestClient;
  }

  @Override
  public ResponseOnboardingDTO getOnboarding(String initiativeId, Pageable pageable, String userId,
      LocalDateTime startDate, LocalDateTime endDate, String status) {
    return onboardingRestClient.getOnboarding(initiativeId,pageable,userId,startDate,endDate,status);
  }
}
