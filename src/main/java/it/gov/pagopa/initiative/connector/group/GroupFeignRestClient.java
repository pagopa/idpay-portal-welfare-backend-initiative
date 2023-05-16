package it.gov.pagopa.initiative.connector.group;

import it.gov.pagopa.initiative.dto.group.InitiativeNotificationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
          @RequestBody InitiativeNotificationDTO initiativeNotificationDTO);

  @PostMapping(
      value = "/group/initiative/{initiativeId}/validate",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  ResponseEntity<Void> setGroupStatusToValidated(
      @PathVariable("initiativeId") String initiativeId);
}
