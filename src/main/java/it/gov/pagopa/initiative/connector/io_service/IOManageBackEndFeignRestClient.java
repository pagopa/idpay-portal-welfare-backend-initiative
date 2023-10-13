package it.gov.pagopa.initiative.connector.io_service;

import it.gov.pagopa.initiative.dto.LogoIODTO;
import it.gov.pagopa.initiative.dto.io.service.KeysDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceRequestDTO;
import it.gov.pagopa.initiative.dto.io.service.ServiceResponseDTO;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * <a href="https://developer.io.italia.it/openapi.html">Digital Citizenship API</a>
 */
@FeignClient(
    name = "${rest-client.backend-io-manage.service.name}",
    url = "${rest-client.backend-io-manage.service.base-url}")
public interface IOManageBackEndFeignRestClient {

  @PostMapping(
          value = "/manage/services",
          produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  ResponseEntity<ServiceResponseDTO> createService(
          @RequestBody @Valid ServiceRequestDTO serviceRequestDTO,
          @RequestHeader("Ocp-Apim-Subscription-Key") String subscriptionKey);

  @PutMapping(
          value = "/manage/services/{serviceId}/logo",
          consumes = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  ResponseEntity<Void> sendLogo(
          @PathVariable("serviceId") String serviceId,
          @RequestBody @Valid LogoIODTO logoDTO,
          @RequestHeader("Ocp-Apim-Subscription-Key") String subscriptionKey);

  @PutMapping(
          value = "/manage/services/{serviceId}",
          produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  ResponseEntity<ServiceResponseDTO> updateService(
          @PathVariable("serviceId") String serviceId,
          @RequestBody ServiceRequestDTO serviceRequestDTO,
          @RequestHeader("Ocp-Apim-Subscription-Key") String subscriptionKey);

  @DeleteMapping(
          value = "/manage/services/{serviceId}",
          produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  ResponseEntity<Void> deleteService(
          @PathVariable("serviceId") String serviceId,
          @RequestHeader("Ocp-Apim-Subscription-Key") String subscriptionKey);

  @GetMapping(
          value = "/manage/services/{serviceId}/keys",
          produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  ResponseEntity<KeysDTO> getServiceKeys(
          @PathVariable("serviceId") String serviceId,
          @RequestHeader("Ocp-Apim-Subscription-Key") String subscriptionKey);
}
