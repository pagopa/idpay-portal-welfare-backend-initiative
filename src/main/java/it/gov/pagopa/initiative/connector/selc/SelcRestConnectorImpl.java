package it.gov.pagopa.initiative.connector.selc;

import it.gov.pagopa.initiative.dto.selc.UserResource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SelcRestConnectorImpl implements SelcRestConnector {

  private final String subscriptionKey;
  private final String selfCareUid;
  private final SelcFeignRestClient selcRestClient;

  public SelcRestConnectorImpl(
          @Value("${rest-client.selc.service.subscriptionKey}") String subscriptionKey,
          @Value("${rest-client.selc.service.selfCareUid}") String selfCareUid,
          SelcFeignRestClient selcRestClient) {
    this.subscriptionKey = subscriptionKey;
    this.selfCareUid = selfCareUid;
    this.selcRestClient = selcRestClient;
  }

  @Override
  public List<UserResource> getInstitutionProductUsers(String organizationId) {
    log.info("[SELC] Retrieve Institution Product Users is about to start...");
    return selcRestClient.getInstitutionProductUsers(organizationId, subscriptionKey, selfCareUid);
  }

}
