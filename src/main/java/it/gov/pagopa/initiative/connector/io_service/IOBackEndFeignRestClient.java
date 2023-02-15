package it.gov.pagopa.initiative.connector.io_service;

import it.gov.pagopa.initiative.dto.LogoIODTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceRequestDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <a href="https://developer.io.italia.it/openapi.html">Digital Citizenship API</a>
 */
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

  @PutMapping(
          value = "/services/{serviceId}/logo",
          produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  ResponseEntity<Void> sendLogo(
          @PathVariable("serviceId") String serviceId,
          @RequestBody @Valid LogoIODTO logo,
          @RequestHeader("Ocp-Apim-Subscription-Key") String subscriptionKey);

  @PutMapping(
          value = "/services/{serviceId}",
          produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
    ResponseEntity<ServiceResponseDTO> updateService(
            @PathVariable("serviceId") String serviceId,
            @RequestBody ServiceRequestDTO serviceRequestDTO,
            @RequestHeader("Ocp-Apim-Subscription-Key") String subscriptionKey);
}
