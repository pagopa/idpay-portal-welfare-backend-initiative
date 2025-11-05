package it.gov.pagopa.assistance.service;

import it.gov.pagopa.assistance.dto.response.OnboardingStatusDTO;
import it.gov.pagopa.assistance.dto.response.VouchersStatusDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


public interface AssistanceService {

    OnboardingStatusDTO onboardingStatus(String initiativeId, String userId);

    VouchersStatusDTO vouchersStatus(String initiativeId, String userId);

}