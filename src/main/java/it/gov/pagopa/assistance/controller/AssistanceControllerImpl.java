package it.gov.pagopa.assistance.controller;

import it.gov.pagopa.assistance.dto.response.OnboardingStatusDTO;
import it.gov.pagopa.assistance.dto.response.VouchersStatusDTO;
import it.gov.pagopa.assistance.service.AssistanceService;
import org.springframework.http.ResponseEntity;


public class AssistanceControllerImpl implements AssistanceController{

    private AssistanceService assistanceService;
    @Override
    public ResponseEntity<OnboardingStatusDTO> onboardingStatus(String initiativeId, String userId) {
        return null;
    }

    @Override
    public ResponseEntity<VouchersStatusDTO> vouchersStatus(String initiativeId, String userId) {
        return ResponseEntity.ok().body(assistanceService.vouchersStatus(initiativeId,userId));
    }
}
