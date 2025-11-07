package it.gov.pagopa.assistance.controller;

import it.gov.pagopa.assistance.dto.response.OnboardingStatusDTO;
import it.gov.pagopa.assistance.dto.response.VouchersStatusDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
@RequestMapping("idpay/assistance")
public interface AssistanceController {

    @GetMapping(value = "/onboardings/status/{initiativeId}/{userId}", produces = "application/json")
    ResponseEntity<OnboardingStatusDTO> onboardingStatus(
            @PathVariable("initiativeId") String initiativeId,
            @PathVariable("userId") String userId
    );

    @GetMapping(value = "/vouchers/status/{initiativeId}/{userId}", produces = "application/json")
    ResponseEntity<VouchersStatusDTO> vouchersStatus(
            @PathVariable("initiativeId") String initiativeId,
            @PathVariable("userId") String userId
    );
}