package it.gov.pagopa.initiative.connector.group;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(
    name = "${rest-client.group.service.name}",
    url = "${rest-client.group.service.base-url}")
public interface GroupFeignRestClient {

  @PostMapping(
      value = "/group/initiative/{initiativeId}/notify",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  ResponseEntity<Void> notifyInitiativeForCitizen(
          @PathVariable("initiativeId") String initiativeId,
          @RequestParam String initiativeName,
          @RequestParam String serviceId);
}
