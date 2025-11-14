package it.gov.pagopa.assistance.service;

import it.gov.pagopa.assistance.dto.response.OnboardingStatusDTO;
import it.gov.pagopa.assistance.dto.response.VouchersStatusDTO;


public interface AssistanceService {

    OnboardingStatusDTO onboardingStatus(String initiativeId, String userId);

    VouchersStatusDTO vouchersStatus(String initiativeId, String userId);

}