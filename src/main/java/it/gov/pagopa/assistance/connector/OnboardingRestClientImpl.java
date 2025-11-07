package it.gov.pagopa.assistance.connector;

import it.gov.pagopa.assistance.costants.AssistanceConstants;
import it.gov.pagopa.assistance.dto.request.OnboardingDTO;
import it.gov.pagopa.common.web.exception.ServiceException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Slf4j
@Service
@RequiredArgsConstructor
public class OnboardingRestClientImpl {

  private final OnboardingRestClient onboardingRestClient;

  public OnboardingDTO getOnboardingStatus(String initiativeId, String userId) {
    log.debug("Calling Onboarding MS for initiativeId={} userId={}", initiativeId, userId);
    try {
      ResponseEntity<OnboardingDTO> response = onboardingRestClient.onboardingStatus(initiativeId, userId);

      if (response == null || !response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
        log.warn("Empty or invalid response from Onboarding MS for initiativeId={} userId={}", initiativeId, userId);
        throw new ServiceException(
                AssistanceConstants.ConnectorError.ASSISTANCE_MERCHANT_ERROR,
                "Empty or invalid response from Merchant MS"
        );
      }

      return response.getBody();
    } catch (RestClientException e) {
      log.error("Error while calling Onboarding MS for initiativeId={} userId={}", initiativeId, userId);
      throw new ServiceException(
              AssistanceConstants.ConnectorError.ASSISTANCE_MERCHANT_ERROR,
              "Error while calling Merchant MS"
      );
    }
  }
}
