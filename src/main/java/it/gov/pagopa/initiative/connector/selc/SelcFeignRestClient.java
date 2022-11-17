package it.gov.pagopa.initiative.connector.selc;

import it.gov.pagopa.initiative.dto.selc.UserResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(
    name = "${rest-client.selc.service.name}",
    url = "${rest-client.selc.service.base-url}")
public interface SelcFeignRestClient {

  @GetMapping(
      value = "/institutions/{organizationId}/products/prod-idpay/users",
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  List<UserResource> getInstitutionProductUsers(
      @PathVariable("organizationId") String organizationId,
      @RequestHeader("Ocp-Apim-Subscription-Key") String subscriptionKey,
      @RequestHeader("x-selfcare-uid") String selfCareUid);
}
