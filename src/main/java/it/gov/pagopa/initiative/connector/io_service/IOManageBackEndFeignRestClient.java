package it.gov.pagopa.initiative.connector.io_service;

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
  @DeleteMapping(
          value = "/manage/services/{serviceId}",
          produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  ResponseEntity<Void> deleteService(
          @PathVariable("serviceId") String serviceId,
          @RequestHeader("Ocp-Apim-Subscription-Key") String subscriptionKey);
}
