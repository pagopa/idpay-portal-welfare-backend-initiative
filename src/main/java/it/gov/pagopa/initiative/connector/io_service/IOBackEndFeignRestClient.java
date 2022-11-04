package it.gov.pagopa.initiative.connector.io_service;

import it.gov.pagopa.initiative.dto.io.service.ServiceRequestDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@FeignClient(
    name = "${rest-client.backend-io.service.name}",
    url = "${rest-client.backend-io.service.base-url}")
public interface IOBackEndFeignRestClient {

  @PostMapping(
      value = "/services",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  ResponseEntity<ServiceResponseDTO> createService(
      @RequestBody @Valid ServiceRequestDTO serviceRequestDTO,
      @RequestHeader("Ocp-Apim-Subscription-Key") String subscriptionKey);
}
