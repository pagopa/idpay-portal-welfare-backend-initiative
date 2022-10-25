package it.gov.pagopa.initiative.connector.email_notification;

import it.gov.pagopa.initiative.dto.email_notification.EmailMessageDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(
    name = "${rest-client.email-notification.service.name}",
    url = "${rest-client.email-notification.service.base-url}")
public interface EmailNotificationFeignRestClient {

  @PostMapping(
      value = "/email-notification/notify",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  ResponseEntity<Void> notifyInitiativeInfo(
          @RequestBody EmailMessageDTO emailMessageDTO);
}
